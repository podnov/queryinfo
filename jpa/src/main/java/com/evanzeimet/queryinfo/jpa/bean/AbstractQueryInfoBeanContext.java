package com.evanzeimet.queryinfo.jpa.bean;

import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.DefaultQueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.DefaultQueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.path.DefaultQueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.DefaultQueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;


public abstract class AbstractQueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult>
		implements QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	private QueryInfoBeanContextRegistry beanContextRegistry;
	private QueryInfoJPAContextFactory<RootEntity> jpaContextFactory;
	private QueryInfoOrderFactory<RootEntity> orderFactory;
	private QueryInfoPathFactory<RootEntity> pathFactory;
	private QueryInfoPredicateFactory<RootEntity> predicateFactory;

	public AbstractQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		this.beanContextRegistry = beanContextRegistry;
		this.jpaContextFactory = new DefaultQueryInfoJPAContextFactory<>();
		this.orderFactory = new DefaultQueryInfoOrderFactory<>(beanContextRegistry);
		this.predicateFactory = new DefaultQueryInfoPredicateFactory<>();
	}

	protected QueryInfoBeanContextRegistry getBeanContextRegistry() {
		return beanContextRegistry;
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
	public Boolean getUseDistinctSelections() {
		return Boolean.FALSE;
	}

	@Override
	public QueryInfoPathFactory<RootEntity> getPathFactory() {
		if (pathFactory == null) {
			Class<RootEntity> rootEntityClass = getRootEntityClass();
			pathFactory = new DefaultQueryInfoPathFactory<>(beanContextRegistry, rootEntityClass);
		}

		return pathFactory;
	}

	@Override
	public QueryInfoPredicateFactory<RootEntity> getPredicateFactory() {
		return predicateFactory;
	}
}
