package com.evanzeimet.queryinfo.it;

/*
 * #%L
 * queryinfo-integration-tests
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
