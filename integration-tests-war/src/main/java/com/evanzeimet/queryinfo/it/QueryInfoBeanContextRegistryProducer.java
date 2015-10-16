package com.evanzeimet.queryinfo.it;

/*
 * #%L
 * queryinfo-integration-tests-war
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

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.bean.DefaultQueryInfoBeanContextRegistry;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBeanContextRegistry;

@Stateless
public class QueryInfoBeanContextRegistryProducer {

	@Inject
	@Any
	private Instance<QueryInfoBeanContext<?, ?, ?>> beanContextInstances;

	@Produces
	public QueryInfoBeanContextRegistry produceBeanContextRegistry() {
		Iterator<QueryInfoBeanContext<?, ?, ?>> iterator = beanContextInstances.iterator();

		List<QueryInfoBeanContext<?, ?, ?>> beanContexts = new ArrayList<>();

		while (iterator.hasNext()) {
			QueryInfoBeanContext<?, ?, ?> beanContext = iterator.next();
			beanContexts.add(beanContext);
		}

		return new DefaultQueryInfoBeanContextRegistry(beanContexts);
	}

}
