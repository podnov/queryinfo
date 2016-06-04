package com.evanzeimet.queryinfo.jpa.bean;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoProvided;
import com.evanzeimet.queryinfo.jpa.jpacontext.DefaultQueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.DefaultQueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.DefaultQueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
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


public abstract class AbstractQueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult>
		implements QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	private QueryInfoEntityContextRegistry entityContextRegistry;
	private EntityManager entityManager;
	protected final QueryInfoJPAContextFactory<RootEntity> jpaContextFactory = new DefaultQueryInfoJPAContextFactory<>();
	protected QueryInfoOrderFactory<RootEntity> orderFactory;
	protected QueryInfoPredicateFactory<RootEntity> predicateFactory;

	public AbstractQueryInfoBeanContext() {
		super();
	}

	public AbstractQueryInfoBeanContext(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public AbstractQueryInfoBeanContext(EntityManager entityManager,
			QueryInfoEntityContextRegistry entityContextRegistry) {
		super();
		this.entityManager = entityManager;
		setEntityContextRegistry(entityContextRegistry);
	}

	@Override
	public QueryInfoEntityContextRegistry getEntityContextRegistry() {
		return entityContextRegistry;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory() {
		return jpaContextFactory;
	}

	@Override
	public QueryInfoOrderFactory<RootEntity> getOrderFactory() {
		return orderFactory;
	}

	@Override
	public QueryInfoPredicateFactory<RootEntity> getPredicateFactory() {
		return predicateFactory;
	}

	@Override
	public Boolean getUseDistinctSelections() {
		return Boolean.FALSE;
	}

	@Inject
	protected void injectQueryInfoEntityContextFactory(@QueryInfoProvided Instance<QueryInfoEntityContextRegistry> contextRegistranceInstances) {
		if (!contextRegistranceInstances.isUnsatisfied()) {
			QueryInfoEntityContextRegistry entityContextRegistry = contextRegistranceInstances.iterator().next();
			setEntityContextRegistry(entityContextRegistry);
		}
	}

	protected void setEntityContextRegistry(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;

		if (orderFactory == null) {
			orderFactory = new DefaultQueryInfoOrderFactory<>(entityContextRegistry);
		} else {
			orderFactory.setEntityContextRegistry(entityContextRegistry);
		}

		if (predicateFactory == null) {
			predicateFactory = new DefaultQueryInfoPredicateFactory<>(entityContextRegistry);
		} else {
			predicateFactory.setEntityContextRegistry(entityContextRegistry);
		}
	}
}
