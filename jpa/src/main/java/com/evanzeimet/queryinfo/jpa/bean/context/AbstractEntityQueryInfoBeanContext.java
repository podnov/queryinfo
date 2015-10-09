package com.evanzeimet.queryinfo.jpa.bean.context;

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


import java.util.Map;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.AbstractQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.field.DefaultEntityAnnotationsQueryInfoFieldResolver;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfoResolver;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.result.DefaultEntityQueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.selection.DefaultEntityQueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public abstract class AbstractEntityQueryInfoBeanContext<RootEntity>
		extends AbstractQueryInfoBeanContext<RootEntity, RootEntity, RootEntity> {

	private Map<String, QueryInfoFieldInfo> fieldInfos;
	private QueryInfoResultConverter<RootEntity, RootEntity> resultConveter;
	private QueryInfoSelectionSetter<RootEntity> selectionSetter;

	public AbstractEntityQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		super(beanContextRegistry);
		resultConveter = new DefaultEntityQueryInfoResultConverter<>();
		selectionSetter = new DefaultEntityQueryInfoSelectionSetter<>(beanContextRegistry);
	}

	@Override
	public Class<RootEntity> getCriteriaQueryResultClass() {
		return getRootEntityClass();
	}

	@Override
	public Map<String, QueryInfoFieldInfo> getFieldInfos() throws QueryInfoException {
		if (fieldInfos == null) {
			Class<RootEntity> rootEntityClass = getRootEntityClass();
			QueryInfoPathFactory<RootEntity> pathFactory = getPathFactory();

			QueryInfoFieldInfoResolver<RootEntity> fieldResolver = new DefaultEntityAnnotationsQueryInfoFieldResolver<>(rootEntityClass);
			fieldInfos = fieldResolver.resolve(pathFactory);
		}

		return fieldInfos;
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
