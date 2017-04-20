package com.evanzeimet.queryinfo.jpa.predicate;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupOperator;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContexts;
import com.evanzeimet.queryinfo.jpa.predicate.converter.ConditionToPredicateConverter;
import com.evanzeimet.queryinfo.jpa.predicate.directive.DirectiveConditionGroupPredicateFactory;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class handles walking the {@link ConditionGroup} structure and then
 * farms off the heavy-lifting based on the specific task at hand (creating
 * conditions, processing condition group directives).
 */
public abstract class AbstractQueryInfoPredicateFactory<RootEntity>
		implements QueryInfoPredicateFactory<RootEntity> {

	private ConditionToPredicateConverter<RootEntity> conditionToPredicateConverter;
	private DirectiveConditionGroupPredicateFactory<RootEntity> directivePredicateFactory;

	public AbstractQueryInfoPredicateFactory(ConditionToPredicateConverter<RootEntity> conditionToPredicateConverter,
			DirectiveConditionGroupPredicateFactory<RootEntity> directivePredicateFactory) {
		this.conditionToPredicateConverter = conditionToPredicateConverter;
		this.directivePredicateFactory = directivePredicateFactory;
	}

	protected Predicate createPredicateForCondition(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition) throws QueryInfoException {
		Predicate result = conditionToPredicateConverter.convert(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				condition);

		if (result == null) {
			JsonNode fieldName = condition.getLeftHandSide();
			String operator = condition.getOperator();
			JsonNode fieldValue = condition.getRightHandSide();

			String message = String.format("Invalid condition supplied, fieldName: [%s], operator: [%s], fieldValue [%s]",
					fieldName,
					operator,
					fieldValue);
			throw new QueryInfoException(message);
		}

		return result;
	}

	public Predicate createPredicateForConditionGroup(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate result = null;
		String rawDirective = conditionGroup.getDirective();

		if (rawDirective == null) {
			result = createPredicateForNoDirectiveConditionGroup(entityContextRegistry,
					jpaContexts,
					currentJpaContext,
					conditionGroup);
		} else {
			result = createPredicateForDirectiveConditionGroup(entityContextRegistry,
					jpaContexts,
					currentJpaContext,
					conditionGroup);
		}

		return result;
	}

	protected Predicate createPredicateForDirectiveConditionGroup(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		return directivePredicateFactory.createPredicate(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				conditionGroup);
	}

	protected Predicate createPredicateForNoDirectiveConditionGroup(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate result;

		List<ConditionGroup> conditionGroups = conditionGroup.getConditionGroups();
		List<Predicate> conditionGroupsPredicates = createPredicatesForConditionGroups(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				conditionGroups);

		List<Condition> conditions = conditionGroup.getConditions();
		List<Predicate> conditionsPredicates = createPredicatesForConditions(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				conditions);

		int predicateCount = (conditionGroupsPredicates.size() + conditionsPredicates.size());

		List<Predicate> predicateList = new ArrayList<>(predicateCount);

		predicateList.addAll(conditionGroupsPredicates);
		predicateList.addAll(conditionsPredicates);

		if (predicateCount == 0) {
			result = null;
		} else {
			CriteriaBuilder criteriaBuilder = currentJpaContext.getCriteriaBuilder();
			String rawConditionGroupOperator = conditionGroup.getOperator();

			ConditionGroupOperator conditionGroupOperator = ConditionGroupOperator.fromText(rawConditionGroupOperator);

			Predicate[] predicateArray = predicateList.toArray(new Predicate[predicateCount]);

			if (ConditionGroupOperator.OR.equals(conditionGroupOperator)) {
				result = criteriaBuilder.or(predicateArray);
			} else {
				result = criteriaBuilder.and(predicateArray);
			}
		}

		return result;
	}

	@Override
	public Predicate[] createPredicates(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate[] result;
		Predicate predicate = createPredicateForConditionGroup(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				conditionGroup);

		if (predicate == null) {
			result = new Predicate[0];
		} else {
			result = new Predicate[] { predicate };
		}

		return result;
	}

	protected List<Predicate> createPredicatesForConditionGroups(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			List<ConditionGroup> conditionGroups) throws QueryInfoException {
		List<Predicate> result = new ArrayList<>();

		if (conditionGroups != null) {
			for (ConditionGroup conditionGroup : conditionGroups) {
				Predicate predicate = createPredicateForConditionGroup(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						conditionGroup);

				if (predicate != null) {
					result.add(predicate);
				}
			}
		}

		return result;
	}

	protected List<Predicate> createPredicatesForConditions(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			List<Condition> conditions) throws QueryInfoException {
		List<Predicate> result = new ArrayList<>();

		if (conditions != null) {
			for (Condition condition : conditions) {
				Predicate predicate = createPredicateForCondition(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition);
				result.add(predicate);
			}
		}

		return result;
	}

}
