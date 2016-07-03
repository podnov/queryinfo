package com.evanzeimet.queryinfo.jpa.bean;

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


import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoProvided;
import com.evanzeimet.queryinfo.jpa.group.DefaultQueryInfoGroupByFactory;
import com.evanzeimet.queryinfo.jpa.group.QueryInfoGroupByFactory;
import com.evanzeimet.queryinfo.jpa.jpacontext.DefaultQueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.DefaultQueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.DefaultQueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;

public abstract class AbstractQueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult>
		implements QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	private QueryInfoEntityContextRegistry entityContextRegistry;
	private EntityManager entityManager;
	private QueryInfoGroupByFactory<RootEntity> groupByFactory = new DefaultQueryInfoGroupByFactory<>();
	private QueryInfoJPAContextFactory<RootEntity> jpaContextFactory = new DefaultQueryInfoJPAContextFactory<>();
	private QueryInfoOrderFactory<RootEntity> orderFactory = new DefaultQueryInfoOrderFactory<>();
	private QueryInfoPredicateFactory<RootEntity> predicateFactory = new DefaultQueryInfoPredicateFactory<>();
	private Boolean useDistinctSelections = false;

	public AbstractQueryInfoBeanContext() {
		super();
	}

	@Override
	public QueryInfoEntityContextRegistry getEntityContextRegistry() {
		return entityContextRegistry;
	}

	public void setEntityContextRegistry(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public QueryInfoGroupByFactory<RootEntity> getGroupByFactory() {
		return groupByFactory;
	}

	public void setGroupByFactory(QueryInfoGroupByFactory<RootEntity> groupByFactory) {
		this.groupByFactory = groupByFactory;
	}

	@Override
	public QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory() {
		return jpaContextFactory;
	}

	public void setJpaContextFactory(QueryInfoJPAContextFactory<RootEntity> jpaContextFactory) {
		this.jpaContextFactory = jpaContextFactory;
	}

	@Override
	public QueryInfoOrderFactory<RootEntity> getOrderFactory() {
		return orderFactory;
	}

	public void setOrderFactory(QueryInfoOrderFactory<RootEntity> orderFactory) {
		this.orderFactory = orderFactory;
	}

	@Override
	public QueryInfoPredicateFactory<RootEntity> getPredicateFactory() {
		return predicateFactory;
	}

	public void setPredicateFactory(QueryInfoPredicateFactory<RootEntity> predicateFactory) {
		this.predicateFactory = predicateFactory;
	}

	@Override
	public Boolean getUseDistinctSelections() {
		return useDistinctSelections;
	}

	public void setUseDistinctSelections(Boolean useDistinctSelections) {
		this.useDistinctSelections = useDistinctSelections;
	}

	@Inject
	protected void injectQueryInfoEntityContextFactory(@QueryInfoProvided Instance<QueryInfoEntityContextRegistry> contextRegistranceInstances) {
		if (!contextRegistranceInstances.isUnsatisfied()) {
			QueryInfoEntityContextRegistry entityContextRegistry = contextRegistranceInstances.iterator().next();
			setEntityContextRegistry(entityContextRegistry);
		}
	}

	@Inject
	protected void injectQueryInfoEntityManager(@QueryInfoProvided Instance<EntityManager> entityManagerInstance) {
		if (!entityManagerInstance.isUnsatisfied()) {
			EntityManager entityManager = entityManagerInstance.iterator().next();
			setEntityManager(entityManager);
		}
	}
}
