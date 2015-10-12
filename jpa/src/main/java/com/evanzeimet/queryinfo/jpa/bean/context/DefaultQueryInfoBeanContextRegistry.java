package com.evanzeimet.queryinfo.jpa.bean.context;

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

import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultQueryInfoBeanContextRegistry implements QueryInfoBeanContextRegistry {

	private List<QueryInfoBeanContext<?, ?, ?>> beanContexts;

	public DefaultQueryInfoBeanContextRegistry(List<QueryInfoBeanContext<?, ?, ?>> beanContexts) {
		this.beanContexts = beanContexts;
	}

	@SuppressWarnings("unchecked")
	public <RootEntity, CriteriaQueryResultType, QueryInfoResultType> QueryInfoBeanContext<RootEntity, CriteriaQueryResultType, QueryInfoResultType> getContext(
			Class<RootEntity> rootEntityClass) {
		QueryInfoBeanContext<RootEntity, CriteriaQueryResultType, QueryInfoResultType> result = null;

		Iterator<QueryInfoBeanContext<?, ?, ?>> iterator = beanContexts.iterator();

		while (iterator.hasNext()) {
			QueryInfoBeanContext<?, ?, ?> currentBeanContext = iterator.next();

			Class<?> beanContextRootEntityClass = currentBeanContext.getRootEntityClass();

			if (rootEntityClass.equals(beanContextRootEntityClass)) {
				result = (QueryInfoBeanContext<RootEntity, CriteriaQueryResultType, QueryInfoResultType>) currentBeanContext;
				break;
			}
		}

		return result;
	}

	public <T> QueryInfoBeanContext<T, ?, ?> getContextForRoot(QueryInfoJPAContext<T> jpaContext) {
		Class<T> rootEntityClass = jpaContext.getRoot().getModel().getBindableJavaType();
		return getContext(rootEntityClass);
	}
}
