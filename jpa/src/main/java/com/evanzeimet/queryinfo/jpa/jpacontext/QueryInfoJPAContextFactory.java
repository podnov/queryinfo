package com.evanzeimet.queryinfo.jpa.jpacontext;

import javax.persistence.criteria.AbstractQuery;

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
import javax.persistence.criteria.Subquery;

public interface QueryInfoJPAContextFactory {

	<RootEntity, QueryType extends AbstractQuery<?>> QueryInfoJPAContext<RootEntity, QueryType> createJpaContext(CriteriaBuilder criteriaBuilder,
			Class<RootEntity> rootEntityClass,
			QueryType criteriaQuery);

	<RootEntity> QueryInfoJPAContext<RootEntity, Subquery<RootEntity>> createSubqueryJpaContext(CriteriaBuilder criteriaBuilder, 
			Class<RootEntity> rootEntityClass, 
			AbstractQuery<?> enclosingQuery);

}
