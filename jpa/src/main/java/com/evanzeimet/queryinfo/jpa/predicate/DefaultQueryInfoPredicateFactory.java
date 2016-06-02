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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupOperator;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.fasterxml.jackson.databind.JsonNode;

public class DefaultQueryInfoPredicateFactory<RootEntity> implements QueryInfoPredicateFactory<RootEntity> {

	private QueryInfoEntityContextRegistry entityContextRegistry;
	private final FieldValueParser fieldValueParser = new FieldValueParser();

	public DefaultQueryInfoPredicateFactory(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;
	}

	@Override
	public void setEntityContextRegistry(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Predicate createPredicate(QueryInfoJPAContext jpaContext,
			String fieldName,
			ConditionOperator conditionOperator,
			JsonNode fieldValue) throws QueryInfoException {
		Predicate result = null;

		QueryInfoEntityContext entityContext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory pathFactory = entityContext.getPathFactory();

		CriteriaBuilder criteriaBuilder = jpaContext.getCriteriaBuilder();
		Root<RootEntity> root = jpaContext.getRoot();

		Expression<?> path = pathFactory.getPathForField(jpaContext,
				root,
				fieldName,
				QueryInfoAttributePurpose.PREDICATE);

		Object parsedFieldValue = fieldValueParser.parse(path, conditionOperator, fieldValue);

		switch (conditionOperator) {
			case EQUAL_TO:
				result = criteriaBuilder.equal(path, parsedFieldValue);
				break;

			case GREATER_THAN:
				result = criteriaBuilder.greaterThan((Expression<Comparable>) path,
						(Comparable<?>) parsedFieldValue);
				break;

			case GREATER_THAN_OR_EQUAL_TO:
				result = criteriaBuilder.greaterThanOrEqualTo((Expression<Comparable>) path,
						(Comparable<?>) parsedFieldValue);
				break;

			case IN:
				result = path.in((Object[]) parsedFieldValue);
				break;

			case IS_NOT_NULL:
				result = criteriaBuilder.isNotNull(path);
				break;

			case IS_NULL:
				result = criteriaBuilder.isNull(path);
				break;

			case LESS_THAN:
				result = criteriaBuilder.lessThan((Expression<Comparable>) path,
						(Comparable<?>) parsedFieldValue);
				break;

			case LESS_THAN_OR_EQUAL_TO:
				result = criteriaBuilder.lessThanOrEqualTo((Expression<Comparable>) path,
						(Comparable<?>) parsedFieldValue);
				break;

			case LIKE:
				result = criteriaBuilder.like((Expression<String>) path,
						(String) parsedFieldValue);
				break;

			case NOT_EQUAL_TO:
				result = criteriaBuilder.notEqual(path, parsedFieldValue);
				break;

			case NOT_IN:
				result = path.in((Object[]) parsedFieldValue);
				result = result.not();
				break;

			case NOT_LIKE:
				result = criteriaBuilder.notLike((Expression<String>) path,
						(String) parsedFieldValue);
				break;
		}

		return result;
	}

	protected Predicate createPredicateForCondition(QueryInfoJPAContext<RootEntity> jpaContext,
			Condition condition) throws QueryInfoException {
		Predicate result = null;
		String fieldName = condition.getLeftHandSide();
		String operator = condition.getOperator();
		JsonNode fieldValue = condition.getRightHandSide();

		ConditionOperator conditionOperator = ConditionOperator.fromText(operator);

		boolean hasFieldName = StringUtils.isNotBlank(fieldName);
		boolean hasOperator = (conditionOperator != null);

		if (hasFieldName && hasOperator) {
			result = createPredicate(jpaContext,
					fieldName,
					conditionOperator,
					fieldValue);
		}

		if (result == null) {
			String message = String.format(
					"Invalid condition supplied, fieldName: [%s], operator: [%s], fieldValue [%s]",
					fieldName,
					operator,
					fieldValue);
			throw new QueryInfoException(message);
		}

		return result;
	}

	protected Predicate createPredicateForConditionGroup(QueryInfoJPAContext<RootEntity> jpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate result;
		List<Predicate> predicateList = new ArrayList<>();

		List<ConditionGroup> conditionGroups = conditionGroup.getConditionGroups();
		List<Predicate> conditionGroupsPredicates = createPredicatesForConditionGroups(jpaContext,
				conditionGroups);
		predicateList.addAll(conditionGroupsPredicates);

		List<Condition> conditions = conditionGroup.getConditions();
		List<Predicate> conditionsPredicates = createPredicatesForConditions(jpaContext,
				conditions);
		predicateList.addAll(conditionsPredicates);

		if (predicateList.isEmpty()) {
			result = null;
		} else {
			CriteriaBuilder criteriaBuilder = jpaContext.getCriteriaBuilder();
			String rawConditionGroupOperator = conditionGroup.getOperator();

			ConditionGroupOperator conditionGroupOperator = ConditionGroupOperator.fromText(
					rawConditionGroupOperator);

			int predicateCount = predicateList.size();
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
	public Predicate[] createPredicates(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		Predicate[] result;
		ConditionGroup conditionGroup = queryInfo.getConditionGroup();

		Predicate predicate = createPredicateForConditionGroup(jpaContext,
				conditionGroup);

		if (predicate == null) {
			result = new Predicate[0];
		} else {
			result = new Predicate[] { predicate };
		}

		return result;
	}

	protected List<Predicate> createPredicatesForConditionGroups(QueryInfoJPAContext<RootEntity> jpaContext,
			List<ConditionGroup> conditionGroups) throws QueryInfoException {
		List<Predicate> result = new ArrayList<>();

		if (conditionGroups != null) {
			for (ConditionGroup conditionGroup : conditionGroups) {
				Predicate predicate = createPredicateForConditionGroup(jpaContext,
						conditionGroup);

				if (predicate != null) {
					result.add(predicate);
				}
			}
		}

		return result;
	}

	protected List<Predicate> createPredicatesForConditions(QueryInfoJPAContext<RootEntity> jpaContext,
			List<Condition> conditions) throws QueryInfoException {
		List<Predicate> result = new ArrayList<>();

		if (conditions != null) {
			for (Condition condition : conditions) {
				Predicate predicate = createPredicateForCondition(jpaContext,
						condition);
				result.add(predicate);
			}
		}

		return result;
	}
}
