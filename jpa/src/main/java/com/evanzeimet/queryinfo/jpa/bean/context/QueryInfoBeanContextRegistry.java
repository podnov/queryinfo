package com.evanzeimet.queryinfo.jpa.bean.context;

import java.util.Iterator;
import java.util.List;

import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class QueryInfoBeanContextRegistry {

	private List<QueryInfoBeanContext<?, ?, ?>> beanContexts;

	public QueryInfoBeanContextRegistry(List<QueryInfoBeanContext<?, ?, ?>> beanContexts) {
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
