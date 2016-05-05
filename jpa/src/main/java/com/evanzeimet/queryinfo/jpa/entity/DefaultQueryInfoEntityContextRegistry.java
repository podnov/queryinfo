package com.evanzeimet.queryinfo.jpa.entity;

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

import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.From;

import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultQueryInfoEntityContextRegistry implements QueryInfoEntityContextRegistry {

	protected List<QueryInfoEntityContext<?>> entityContexts;

	public DefaultQueryInfoEntityContextRegistry(List<QueryInfoEntityContext<?>> entityContexts) {
		this.entityContexts = entityContexts;
	}

	@Override
	public <T> QueryInfoEntityContext<T> getContext(From<?, T> from) {
		Class<T> entityClass = from.getModel().getBindableJavaType();
		return getContext(entityClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Entity> QueryInfoEntityContext<Entity> getContext(Class<Entity> entityClass) {
		QueryInfoEntityContext<Entity> result = null;

		Iterator<QueryInfoEntityContext<?>> iterator = entityContexts.iterator();

		while (iterator.hasNext()) {
			QueryInfoEntityContext<?> currentEntityContext = iterator.next();

			Class<?> currentEntityContextEntityClass = currentEntityContext.getEntityClass();

			if (entityClass.equals(currentEntityContextEntityClass)) {
				result = (QueryInfoEntityContext<Entity>) currentEntityContext;
				break;
			}
		}

		return result;
	}

	@Override
	public <T> QueryInfoEntityContext<T> getContextForRoot(QueryInfoJPAContext<T> jpaContext) {
		Class<T> rootEntityClass = jpaContext.getRoot().getModel().getBindableJavaType();
		return getContext(rootEntityClass);
	}
}
