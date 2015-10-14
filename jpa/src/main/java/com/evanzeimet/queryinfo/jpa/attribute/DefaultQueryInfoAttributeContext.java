package com.evanzeimet.queryinfo.jpa.attribute;

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

import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;

public class DefaultQueryInfoAttributeContext implements QueryInfoAttributeContext {

	private Map<String, QueryInfoFieldInfo> fields;
	private Map<String, QueryInfoJoinInfo> joins;

	public DefaultQueryInfoAttributeContext() {

	}

	@Override
	public QueryInfoFieldInfo getField(String queryInfoFieldAttributeName) {
		QueryInfoFieldInfo result;

		if (fields == null) {
			result = null;
		} else {
			result = fields.get(queryInfoFieldAttributeName);
		}

		return result;
	}

	@Override
	public Map<String, QueryInfoFieldInfo> getFields() {
		return fields;
	}

	@Override
	public void setFields(Map<String, QueryInfoFieldInfo> fields) {
		this.fields = fields;
	}

	@Override
	public QueryInfoJoinInfo getJoin(String queryInfoJoinAttributeName) {
		QueryInfoJoinInfo result;

		if (fields == null) {
			result = null;
		} else {
			result = joins.get(queryInfoJoinAttributeName);
		}

		return result;
	}

	@Override
	public Map<String, QueryInfoJoinInfo> getJoins() {
		return joins;
	}

	@Override
	public void setJoins(Map<String, QueryInfoJoinInfo> joins) {
		this.joins = joins;
	}
}
