package com.evanzeimet.queryinfo.it;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;

@Singleton
public class BeanContextRegistryProducer {

	@Inject
	@Any
	// TODO will this work? doubt it
	private Instance<QueryInfoBeanContext<?, ?, ?>> beanContextInstances;

	private QueryInfoBeanContextRegistry beanContextRegistry;

	protected QueryInfoBeanContextRegistry createBeanContextRegistry() {
		Iterator<QueryInfoBeanContext<?, ?, ?>> iterator = beanContextInstances.iterator();

		List<QueryInfoBeanContext<?, ?, ?>> beanContexts = new ArrayList<>();

		while (iterator.hasNext()) {
			QueryInfoBeanContext<?, ?, ?> beanContext = iterator.next();
			beanContexts.add(beanContext);
		}

		return new QueryInfoBeanContextRegistry(beanContexts);
	}

	@Produces
	public QueryInfoBeanContextRegistry produceBeanContextRegistry() {
		return beanContextRegistry;
	}

	@PostConstruct
	protected void postConstruct() {
		beanContextRegistry = createBeanContextRegistry();
	}
}
