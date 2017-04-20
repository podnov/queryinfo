package com.evanzeimet.queryinfo.jpa.order;

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
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortDirection;

public class DefaultQueryInfoOrderFactory<RootEntity>
		implements QueryInfoOrderFactory<RootEntity> {

	public DefaultQueryInfoOrderFactory() {

	}

	protected Order createOrder(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity, ?> jpaContext,
			Sort sort) throws QueryInfoException {
		Order result;

		String attributePath = sort.getAttributePath();
		Expression<?> path = getPathForAttribute(entityContextRegistry, jpaContext, attributePath);

		CriteriaBuilder criteriaBuilder = jpaContext.getCriteriaBuilder();
		SortDirection direction = SortDirection.fromSort(sort);

		if (SortDirection.DESC.equals(direction)) {
			result = criteriaBuilder.desc(path);
		} else {
			result = criteriaBuilder.asc(path);
		}

		return result;
	}

	@Override
	public List<Order> createOrders(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity, ?> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<Sort> sorts = queryInfo.getSorts();
		return createOrders(entityContextRegistry, jpaContext, sorts);
	}

	protected List<Order> createOrders(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity, ?> jpaContext,
			List<Sort> sorts) throws QueryInfoException {
		List<Order> result;

		if (sorts == null) {
			result = Collections.emptyList();
		} else {
			int sortCount = sorts.size();

			result = new ArrayList<>(sortCount);

			for (Sort sort : sorts) {
				Order order = createOrder(entityContextRegistry, jpaContext, sort);
				result.add(order);
			}
		}

		return result;
	}

	protected Expression<?> getPathForAttribute(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity, ?> jpaContext,
			String attributePath) throws QueryInfoException {
		QueryInfoEntityContext<RootEntity> entityContext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory<RootEntity> pathFactory = entityContext.getPathFactory();
		Root<RootEntity> root = jpaContext.getRoot();

		return pathFactory.getPathForAttribute(entityContextRegistry,
				jpaContext,
				root,
				attributePath,
				QueryInfoAttributePurpose.ORDER);
	}
}
