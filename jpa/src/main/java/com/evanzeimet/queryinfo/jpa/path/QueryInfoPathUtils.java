package com.evanzeimet.queryinfo.jpa.path;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
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
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Bindable;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.selection.AggregateFunction;

public class QueryInfoPathUtils {

	public Expression<?> applyAggregate(QueryInfoJPAContext<?, ?> jpaContext,
			Expression<?> expression,
			AggregateFunction aggregateFunction) throws QueryInfoException {
		Expression<?> result = null;
		CriteriaBuilder criteriaBuilder = jpaContext.getCriteriaBuilder();

		@SuppressWarnings("unchecked")
		Expression<? extends Number> numericExpression = (Expression<? extends Number>) expression;

		switch (aggregateFunction) {
			case AVG:
				result = criteriaBuilder.avg(numericExpression);
				break;

			case COUNT:
				result = criteriaBuilder.count(numericExpression);
				break;

			case MAX:
				result = criteriaBuilder.max(numericExpression);
				break;

			case MIN:
				result = criteriaBuilder.min(numericExpression);
				break;

			case SUM:
				result = criteriaBuilder.sum(numericExpression);
				break;
		}

		if (result == null) {
			String message = String.format("Aggregate [%s] not implemented", aggregateFunction);
			throw new QueryInfoException(message);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getBindableJavaType(Path<T> path) {
		Class<T> result;
		Bindable<T> model = path.getModel();

		if (model == null) {
			result = ((Class<T>) path.getJavaType());
		} else {
			result = model.getBindableJavaType();
		}

		return result;
	}
}
