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

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.evanzeimet.queryinfo.jpa.bean.DefaultQueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.google.common.collect.ImmutableList;

@Singleton
@Startup
public class QueryInfoEntityContextRegistryProducer {

	@Inject
	@Any
	private Instance<QueryInfoEntityContext<?>> entityContextInstances;

	@Inject
	private Logger logger;

	private DefaultQueryInfoEntityContextRegistry contextRegistry;

	protected void createContextRegistry() {
		Iterator<QueryInfoEntityContext<?>> iterator = entityContextInstances.iterator();

		List<QueryInfoEntityContext<?>> entityContexts = new ArrayList<>();

		while (iterator.hasNext()) {
			QueryInfoEntityContext<?> entityContext = iterator.next();
			entityContexts.add(entityContext);
		}

		if (logger.isInfoEnabled()) {
			int entityContextCount = entityContexts.size();

			String message = String.format("Found [%s] entity contexts", entityContextCount);
			logger.info(message);
		}

		ImmutableList<QueryInfoEntityContext<?>> immutableList = ImmutableList.copyOf(entityContexts);

		contextRegistry = new DefaultQueryInfoEntityContextRegistry(immutableList);
	}

	@Produces
	public QueryInfoEntityContextRegistry produceBeanContextRegistry() {
		return contextRegistry;
	}

	@PostConstruct
	protected void postConstruct() {
		createContextRegistry();
	}
}
