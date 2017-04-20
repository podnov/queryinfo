package com.evanzeimet.queryinfo.jpa.predicate.directive;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import javax.persistence.criteria.Predicate;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.jpa.condition.directive.QueryInfoJPAConditionGroupDirective;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContexts;

public class DefaultDirectiveConditionGroupPredicateFactory<RootEntity>
		implements DirectiveConditionGroupPredicateFactory<RootEntity> {

	@Override
	public Predicate createPredicate(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate result = null;
		String rawDirective = conditionGroup.getDirective();
		QueryInfoJPAConditionGroupDirective directive = QueryInfoJPAConditionGroupDirective.valueOf(rawDirective);

		switch (directive) {
			case EXISTS:
				result = createPredicateForExistsConditionGroup(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						conditionGroup);
				break;

			case NOT_EXISTS:
				result = createPredicateForNotExistsConditionGroup(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						conditionGroup);
				break;
		}

		return result;
	}

	protected Predicate createPredicateForExistsConditionGroup(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		ExistsDirectiveConditionGroupPredicateFactory<RootEntity> factory = new ExistsDirectiveConditionGroupPredicateFactory<RootEntity>();
		return factory.createPredicate(entityContextRegistry, jpaContexts, currentJpaContext, conditionGroup);
	}

	protected Predicate createPredicateForNotExistsConditionGroup(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate predicate = createPredicateForExistsConditionGroup(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				conditionGroup);
		return predicate.not();
	}
}
