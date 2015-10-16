package com.evanzeimet.queryinfo.jpa.bean.entity;



import com.evanzeimet.queryinfo.jpa.bean.AbstractQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.DefaultQueryInfoEntityContextRegistry;


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


import com.evanzeimet.queryinfo.jpa.result.DefaultEntityQueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.selection.DefaultEntityQueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public abstract class AbstractEntityQueryInfoBeanContext<RootEntity>
		extends AbstractQueryInfoBeanContext<RootEntity, RootEntity, RootEntity> {

	private QueryInfoResultConverter<RootEntity, RootEntity> resultConveter = new DefaultEntityQueryInfoResultConverter<>();
	private QueryInfoSelectionSetter<RootEntity> selectionSetter = new DefaultEntityQueryInfoSelectionSetter<>();

	public AbstractEntityQueryInfoBeanContext() {
		super();
	}

	public AbstractEntityQueryInfoBeanContext(DefaultQueryInfoEntityContextRegistry entityContextRegistry) {
		super(entityContextRegistry);
	}

	@Override
	public Class<RootEntity> getCriteriaQueryResultClass() {
		return getRootEntityClass();
	}

	@Override
	public QueryInfoResultConverter<RootEntity, RootEntity> getResultConverter() {
		return resultConveter;
	}

	@Override
	public QueryInfoSelectionSetter<RootEntity> getSelectionSetter() {
		return selectionSetter;
	}

}
