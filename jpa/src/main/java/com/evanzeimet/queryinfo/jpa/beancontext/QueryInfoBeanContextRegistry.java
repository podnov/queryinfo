package com.evanzeimet.queryinfo.jpa.beancontext;

import java.util.Iterator;

import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

@Singleton
public class QueryInfoBeanContextRegistry {

	@Inject
	@Any
	// TODO will this work? doubt it
	private Instance<QueryInfoBeanContext<?, ?>> pathFactories;

	@SuppressWarnings("unchecked")
	public <RootEntity, InitialTupleResultType, FinalTupleResultType> QueryInfoBeanContext<RootEntity, FinalTupleResultType> getContext(Class<RootEntity> rootEntityClass) {
		QueryInfoBeanContext<RootEntity, FinalTupleResultType> result = null;

		Iterator<QueryInfoBeanContext<?, ?>> iterator = pathFactories.iterator();

		while (iterator.hasNext()) {
			QueryInfoBeanContext<?, ?> currentBeanContext = iterator.next();

			Class<?> beanContextRootEntityClass = currentBeanContext.getRootEntityClass();

			if (rootEntityClass.equals(beanContextRootEntityClass)) {
				result = (QueryInfoBeanContext<RootEntity, FinalTupleResultType>) currentBeanContext;
				break;
			}
		}

		return result;
	}

	public <T> QueryInfoBeanContext<T, ?> getContextForRoot(QueryInfoJPAContext<T> jpaContext) {
		Class<T> rootEntityClass = jpaContext.getRoot().getModel().getBindableJavaType();
		return getContext(rootEntityClass);
	}
}
