package com.evanzeimet.queryinfo.jpa.field;

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


public class QueryInfoFieldInfoBuilder {

	private QueryInfoFieldInfo builderRefrenceInstance = new DefaultQueryInfoFieldInfo();

	public QueryInfoFieldInfoBuilder() {

	}

	public QueryInfoFieldInfo build() {
		QueryInfoFieldInfo result = new DefaultQueryInfoFieldInfo();

		result.setEntityAttributeName(builderRefrenceInstance.getEntityAttributeName());
		result.setFieldName(builderRefrenceInstance.getFieldName());
		result.setIsQueryable(builderRefrenceInstance.getIsQueryable());
		result.setIsSelectable(builderRefrenceInstance.getIsSelectable());
		result.setIsSortable(builderRefrenceInstance.getIsSortable());

		return result;
	}

	public static QueryInfoFieldInfoBuilder create() {
		return new QueryInfoFieldInfoBuilder();
	}

	public QueryInfoFieldInfoBuilder entityAttributeName(String entityAttributeName) {
		builderRefrenceInstance.setEntityAttributeName(entityAttributeName);
		return this;
	}

	public QueryInfoFieldInfoBuilder fieldName(String fieldName) {
		builderRefrenceInstance.setFieldName(fieldName);
		return this;
	}

	public QueryInfoFieldInfoBuilder isQueryable(Boolean isQueryable) {
		builderRefrenceInstance.setIsQueryable(isQueryable);
		return this;
	}

	public QueryInfoFieldInfoBuilder isSelectable(Boolean isSelectable) {
		builderRefrenceInstance.setIsSelectable(isSelectable);
		return this;
	}

	public QueryInfoFieldInfoBuilder isSortable(boolean isSortable) {
		builderRefrenceInstance.setIsSortable(isSortable);
		return this;
	}

}
