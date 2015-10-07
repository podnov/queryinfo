package com.evanzeimet.queryinfo.jpa.beancontext;

import java.util.Iterator;

import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
public class QueryInfoBeanContextRegistry {

	@Inject
	@Any
	// TODO will this work? doubt it
	private Instance<CriteriaQueryBeanContext<?, ?, ?>> pathFactories;

	@SuppressWarnings("unchecked")
	public <RootEntity, InitialResultType, FinalResultType> CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType> getContext(Class<RootEntity> rootEntityClass) {
		CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType> result = null;

		Iterator<CriteriaQueryBeanContext<?, ?, ?>> iterator = pathFactories.iterator();

		while (iterator.hasNext()) {
			CriteriaQueryBeanContext<?, ?, ?> currentBeanContext = iterator.next();

			Class<?> beanContextRootEntityClass = currentBeanContext.getRootEntityClass();

			if (rootEntityClass.equals(beanContextRootEntityClass)) {
				result = (CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType>) currentBeanContext;
				break;
			}
		}

		return result;
	}
}
