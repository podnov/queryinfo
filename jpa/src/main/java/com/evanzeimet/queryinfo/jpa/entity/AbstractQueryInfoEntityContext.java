package com.evanzeimet.queryinfo.jpa.entity;

/*
 * #%L
 * queryinfo-jpa
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


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolver;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAtrributeInfoResolver;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.path.DefaultQueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public abstract class AbstractQueryInfoEntityContext<Entity> implements QueryInfoEntityContext<Entity> {

	private DefaultQueryInfoPathFactory<Entity> pathFactory;
	private QueryInfoAttributeContext queryInfoAttributeContext;

	@Override
	public QueryInfoPathFactory<Entity> getPathFactory() {
		return pathFactory;
	}

	@Override
	public QueryInfoAttributeContext getQueryInfoAttributeContext() {
		return queryInfoAttributeContext;
	}

	protected void createAttributeContext() {
		Class<Entity> rootEntityClass = getEntityClass();
		QueryInfoPathFactory<Entity> pathFactory = getPathFactory();

		QueryInfoAtrributeInfoResolver<Entity> attribiteResolver = new DefaultEntityAnnotationsAttributeInfoResolver<>(rootEntityClass);
		queryInfoAttributeContext = attribiteResolver.resolve(pathFactory);
	}

	@PostConstruct
	@Inject
	protected void postConstruct(QueryInfoEntityContextRegistry entityContextRegistry) {
		if (pathFactory == null) {
			Class<Entity> rootEntityClass = getEntityClass();
			pathFactory = new DefaultQueryInfoPathFactory<>(entityContextRegistry, rootEntityClass);
		} else {
			pathFactory.setEntityContextRegistry(entityContextRegistry);
		}

		createAttributeContext();
	}
}
