package com.evanzeimet.queryinfo.jpa.jpacontext;

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

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class DefaultQueryInfoJPAContextFactory
		implements QueryInfoJPAContextFactory {

	@Override
	public <RootEntity, QueryType extends AbstractQuery<?>> QueryInfoJPAContext<RootEntity, QueryType> createJpaContext(CriteriaBuilder criteriaBuilder,
					Class<RootEntity> rootEntityClass,
					QueryType criteriaQuery) {
		Root<RootEntity> root = criteriaQuery.from(rootEntityClass);

		QueryInfoJPAContext<RootEntity, QueryType> result = new QueryInfoJPAContext<>();

		result.setCriteriaBuilder(criteriaBuilder);
		result.setCriteriaQuery(criteriaQuery);
		result.setFactory(this);
		result.setRoot(root);

		return result;
	}

	@Override
	public <RootEntity> QueryInfoJPAContext<RootEntity, Subquery<RootEntity>> createSubqueryJpaContext(CriteriaBuilder criteriaBuilder,
			Class<RootEntity> rootEntityClass,
			AbstractQuery<?> enclosingQuery) {
		Subquery<RootEntity> joinTableSubquery = enclosingQuery.subquery(rootEntityClass);
		Root<RootEntity> root = joinTableSubquery.from(rootEntityClass);

		QueryInfoJPAContext<RootEntity, Subquery<RootEntity>> result = new QueryInfoJPAContext<>();

		result.setCriteriaBuilder(criteriaBuilder);
		result.setCriteriaQuery(joinTableSubquery);
		result.setFactory(this);
		result.setRoot(root);

		return result;
	}

}
