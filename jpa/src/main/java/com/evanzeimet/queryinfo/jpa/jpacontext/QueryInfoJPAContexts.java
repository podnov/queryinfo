package com.evanzeimet.queryinfo.jpa.jpacontext;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;

import com.evanzeimet.queryinfo.QueryInfoRuntimeException;

public class QueryInfoJPAContexts<RootEntity, RootResultType> {

	public static final String ROOT_QUERY_NAME = "root";

	protected Map<String, QueryInfoJPAContext<?, ?>> namedSubqueryContexts = new HashMap<>();
	protected QueryInfoJPAContext<RootEntity, CriteriaQuery<RootResultType>> rootContext;

	public QueryInfoJPAContext<RootEntity, CriteriaQuery<RootResultType>> getRootContext() {
		return rootContext;
	}

	public void setRootContext(QueryInfoJPAContext<RootEntity, CriteriaQuery<RootResultType>> rootContext) {
		this.rootContext = rootContext;
	}

	public void putNamedSubqueryContext(String name, QueryInfoJPAContext<?, ?> context) {
		if (ROOT_QUERY_NAME.equals(name)) {
			String message = String.format("Subquery name [%s] is reserved", ROOT_QUERY_NAME);
			throw new QueryInfoRuntimeException(message);
		}

		namedSubqueryContexts.put(name, context);
	}

	@SuppressWarnings("unchecked")
	public <T extends QueryInfoJPAContext<?, ?>> T getNamedSubqueryContext(String name) {
		return (T) namedSubqueryContexts.get(name);
	}

}
