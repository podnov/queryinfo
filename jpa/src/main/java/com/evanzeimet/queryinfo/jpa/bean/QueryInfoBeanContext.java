package com.evanzeimet.queryinfo.jpa.bean;

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

import javax.persistence.EntityManager;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public interface QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	Class<CriteriaQueryResult> getCriteriaQueryResultClass();

	QueryInfoEntityContext<RootEntity> getEntityContext();

	EntityManager getEntityManager();

	QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory();

	QueryInfoOrderFactory<RootEntity> getOrderFactory();

	QueryInfoPredicateFactory<RootEntity> getPredicateFactory();

	QueryInfoResultConverter<CriteriaQueryResult, QueryInfoResult> getResultConverter();

	Class<RootEntity> getRootEntityClass();

	QueryInfoSelectionSetter<RootEntity> getSelectionSetter();

	Boolean getUseDistinctSelections();

}
