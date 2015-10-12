package com.evanzeimet.queryinfo.jpa.jpacontext;

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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;

public class DefaultQueryInfoJPAContextFactory<RootEntity>
		implements QueryInfoJPAContextFactory<RootEntity> {

	@Override
	public <CriteriaQueryResultType> QueryInfoJPAContext<RootEntity> createJpaContext(CriteriaBuilder criteriaBuilder,
			QueryInfoBeanContext<RootEntity, ?, ?> beanContext,
			CriteriaQuery<CriteriaQueryResultType> criteriaQuery) {
		Class<RootEntity> rootEntityClass = beanContext.getRootEntityClass();
		Root<RootEntity> root = criteriaQuery.from(rootEntityClass);

		QueryInfoJPAContext<RootEntity> result = new QueryInfoJPAContext<RootEntity>();

		result.setCriteriaBuilder(criteriaBuilder);
		result.setCriteriaQuery(criteriaQuery);
		result.setRoot(root);

		return result;
	}

}
