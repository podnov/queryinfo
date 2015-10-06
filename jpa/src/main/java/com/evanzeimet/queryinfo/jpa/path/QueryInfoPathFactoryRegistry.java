package com.evanzeimet.queryinfo.jpa.path;

import java.util.Iterator;

import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
public class QueryInfoPathFactoryRegistry {

	@Inject
	@Any
	// TODO will this work?
	private Instance<QueryInfoPathFactory<?>> pathFactories;

	@SuppressWarnings("unchecked")
	public <Entity> QueryInfoPathFactory<Entity> getFactory(Class<Entity> entityClass) {
		QueryInfoPathFactory<Entity> result = null;

		Iterator<QueryInfoPathFactory<?>> iterator = pathFactories.iterator();

		while (iterator.hasNext()) {
			QueryInfoPathFactory<?> currentFactory = iterator.next();

			Class<?> factoryEntityClass = currentFactory.getEntityClass();

			if (entityClass.equals(factoryEntityClass)) {
				result = (QueryInfoPathFactory<Entity>) currentFactory;
				break;
			}
		}

		return result;
	}
}
