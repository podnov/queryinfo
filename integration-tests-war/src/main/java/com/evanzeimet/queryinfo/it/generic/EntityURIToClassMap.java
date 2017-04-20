package com.evanzeimet.queryinfo.it.generic;

/*
 * #%L
 * queryinfo-integration-tests-war
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoProvided;

@Singleton
@Lock(LockType.READ)
public class EntityURIToClassMap {

	@Inject
	@QueryInfoProvided
	protected QueryInfoEntityContextRegistry entityContextRegistry;

	protected Map<String, Class<?>> uriToClassMap;

	protected void cacheUriToClassMap() {
		List<QueryInfoEntityContext<?>> contexts = entityContextRegistry.getContexts();

		if (contexts == null) {
			uriToClassMap = Collections.emptyMap();
		} else {
			int contextCount = contexts.size();
			uriToClassMap = new HashMap<>(contextCount);

			for (QueryInfoEntityContext<?> context : contexts) {
				Class<?> entityClass = context.getEntityClass();
				String className = entityClass.getSimpleName();

				String uri = className.substring(0, 1).toLowerCase();
				uri += className.substring(1);

				uriToClassMap.put(uri, entityClass);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <E> Class<E> get(String uri) {
		return (Class<E>) uriToClassMap.get(uri);
	}

	@PostConstruct
	protected void postConstruct() {
		cacheUriToClassMap();
	}
}
