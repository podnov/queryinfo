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
	public <RootEntity, InitialTupleResultType, FinalTupleResultType> CriteriaQueryBeanContext<RootEntity, InitialTupleResultType, FinalTupleResultType> getContext(Class<RootEntity> rootEntityClass) {
		CriteriaQueryBeanContext<RootEntity, InitialTupleResultType, FinalTupleResultType> result = null;

		Iterator<CriteriaQueryBeanContext<?, ?, ?>> iterator = pathFactories.iterator();

		while (iterator.hasNext()) {
			CriteriaQueryBeanContext<?, ?, ?> currentBeanContext = iterator.next();

			Class<?> beanContextRootEntityClass = currentBeanContext.getRootEntityClass();

			if (rootEntityClass.equals(beanContextRootEntityClass)) {
				result = (CriteriaQueryBeanContext<RootEntity, InitialTupleResultType, FinalTupleResultType>) currentBeanContext;
				break;
			}
		}

		return result;
	}
}
