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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;

public class QueryInfoAttributeContextBuilder {

	private QueryInfoAttributeContext builderReferenceInstance = new DefaultQueryInfoAttributeContext();

	public QueryInfoAttributeContextBuilder() {

	}

	public QueryInfoAttributeContext build() {
		QueryInfoAttributeContext result = new DefaultQueryInfoAttributeContext();

		result.setFields(builderReferenceInstance.getFields());
		result.setJoins(builderReferenceInstance.getJoins());

		return result;
	}

	public static QueryInfoAttributeContextBuilder create() {
		return new QueryInfoAttributeContextBuilder();
	}

	public QueryInfoAttributeContextBuilder fields(List<QueryInfoFieldInfo> fields) {
		Map<String, QueryInfoFieldInfo> fieldsMap = mapAttributesToNames(fields);
		return fields(fieldsMap);
	}

	public QueryInfoAttributeContextBuilder fields(Map<String, QueryInfoFieldInfo> fields) {
		builderReferenceInstance.setFields(fields);
		return this;
	}

	public QueryInfoAttributeContextBuilder joins(List<QueryInfoJoinInfo> joins) {
		Map<String, QueryInfoJoinInfo> joinsMap = mapAttributesToNames(joins);
		return joins(joinsMap);
	}

	public QueryInfoAttributeContextBuilder joins(Map<String, QueryInfoJoinInfo> joins) {
		builderReferenceInstance.setJoins(joins);
		return this;
	}

	protected <T extends QueryInfoAttributeInfo> Map<String, T> mapAttributesToNames(List<T> attributes) {
		int attributeCount = attributes.size();
		Map<String, T> result = new HashMap<>(attributeCount);

		for (T attribute : attributes) {
			String attributeName = attribute.getName();
			result.put(attributeName, attribute);
		}

		return result;
	}
}
