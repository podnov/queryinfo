package com.evanzeimet.queryinfo.jpa.predicate;

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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.condition.ConditionOperator;

public class QueryInfoPredicateUtils {

	public Predicate createPredicate(CriteriaBuilder criteriaBuilder,
			Object literalX,
			ConditionOperator conditionOperator,
			Object y)
			throws QueryInfoException {
		Expression<?> x = criteriaBuilder.literal(literalX);
		return createPredicate(criteriaBuilder, x, conditionOperator, y);
	}

	public Predicate createPredicate(CriteriaBuilder criteriaBuilder,
			Expression<?> x,
			ConditionOperator conditionOperator,
			Object literalY)
			throws QueryInfoException {
		Expression<?> y = criteriaBuilder.literal(literalY);
		return createPredicate(criteriaBuilder, x, conditionOperator, y);
	}

	public Predicate createPredicate(CriteriaBuilder criteriaBuilder,
			Object literalX,
			ConditionOperator conditionOperator,
			Expression<?> y)
			throws QueryInfoException {
		Expression<?> x = criteriaBuilder.literal(literalX);
		return createPredicate(criteriaBuilder, x, conditionOperator, y);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate createPredicate(CriteriaBuilder criteriaBuilder,
			Expression<?> x,
			ConditionOperator conditionOperator,
			Expression<?> y)
			throws QueryInfoException {
		Predicate result = null;

		switch (conditionOperator) {
			case EQUAL_TO:
				result = criteriaBuilder.equal(x, y);
				break;

			case GREATER_THAN:
				result = criteriaBuilder.greaterThan((Expression<Comparable>) x,
						(Expression<Comparable>) y);
				break;

			case GREATER_THAN_OR_EQUAL_TO:
				result = criteriaBuilder.greaterThanOrEqualTo((Expression<Comparable>) x,
						(Expression<Comparable>) y);
				break;

			case IN:
				result = x.in(y);
				break;

			case IS_NOT_NULL:
				result = criteriaBuilder.isNotNull(x);
				break;

			case IS_NULL:
				result = criteriaBuilder.isNull(x);
				break;

			case LESS_THAN:
				result = criteriaBuilder.lessThan((Expression<Comparable>) x,
						(Expression<Comparable>) y);
				break;

			case LESS_THAN_OR_EQUAL_TO:
				result = criteriaBuilder.lessThanOrEqualTo((Expression<Comparable>) x,
						(Expression<Comparable>) y);
				break;

			case LIKE:
				result = criteriaBuilder.like((Expression<String>) x, (Expression<String>) y);
				break;

			case NOT_EQUAL_TO:
				result = criteriaBuilder.notEqual(x, y);
				break;

			case NOT_IN:
				result = x.in(y).not();
				break;

			case NOT_LIKE:
				result = criteriaBuilder.notLike((Expression<String>) x,
						(Expression<String>) y);
				break;
		}

		return result;
	}

}
