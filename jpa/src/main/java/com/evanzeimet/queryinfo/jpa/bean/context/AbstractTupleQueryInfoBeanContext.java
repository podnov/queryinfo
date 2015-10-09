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

import javax.persistence.Tuple;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.AbstractQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.selection.DefaultTupleQueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public abstract class AbstractTupleQueryInfoBeanContext<RootEntity, QueryInfoResult>
		extends AbstractQueryInfoBeanContext<RootEntity, Tuple, QueryInfoResult> {

	private DefaultTupleQueryInfoSelectionSetter<RootEntity> selectionSetter;

	public AbstractTupleQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		super(beanContextRegistry);
		selectionSetter = new DefaultTupleQueryInfoSelectionSetter<>(beanContextRegistry);
	}

	@Override
	public Class<Tuple> getCriteriaQueryResultClass() {
		return Tuple.class;
	}

	@Override
	public Map<String, QueryInfoFieldInfo> getFieldInfos() throws QueryInfoException {
		// TODO Walk all joins? Enum-based definitions?
		return null;
	}

	@Override
	public QueryInfoSelectionSetter<RootEntity> getSelectionSetter() {
		return selectionSetter;
	}

}
