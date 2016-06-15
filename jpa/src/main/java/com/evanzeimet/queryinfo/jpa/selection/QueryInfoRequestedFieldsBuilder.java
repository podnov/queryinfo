package com.evanzeimet.queryinfo.jpa.selection;

/*
 * #%L
 * queryinfo-jpa
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;

public class QueryInfoRequestedFieldsBuilder {

	private final List<String> fieldNames = new ArrayList<String>();

	public QueryInfoRequestedFieldsBuilder() {

	}
	
	public QueryInfoRequestedFieldsBuilder addSelectableFields(QueryInfoEntityContext<?> entityContext) {
		List<String> selectableFieldNames = new ArrayList<String>();
		QueryInfoAttributeContext attributeContext = entityContext.getAttributeContext();
		Iterator<QueryInfoFieldInfo> fieldInfos = attributeContext.getFields().values().iterator();

		while (fieldInfos.hasNext()) {
			QueryInfoFieldInfo fieldInfo = fieldInfos.next();

			if (fieldInfo.getIsSelectable()) {
				String fieldName = fieldInfo.getName();
				selectableFieldNames.add(fieldName);
			}
		}

		return add(selectableFieldNames);
	}

	public QueryInfoRequestedFieldsBuilder add(String fieldName) {
		this.fieldNames.add(fieldName);
		return this;
	}

	public QueryInfoRequestedFieldsBuilder add(List<String> fieldNames) {
		this.fieldNames.addAll(fieldNames);
		return this;
	}

	public List<String> build() {
		return new ArrayList<String>(fieldNames);
	}
	
	public static QueryInfoRequestedFieldsBuilder create() {
		return new QueryInfoRequestedFieldsBuilder();
	}
}
