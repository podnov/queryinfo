package com.evanzeimet.queryinfo.jpa.field;

import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;

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

	private QueryInfoFieldInfo builderReferenceInstance = new DefaultQueryInfoFieldInfo();

	public QueryInfoFieldInfoBuilder() {

	}

	public QueryInfoFieldInfoBuilder annotation(QueryInfoField annotation) {
		String fieldName = annotation.name();
		boolean isPredicateable = annotation.isPredicateable();
		boolean isSelectable = annotation.isSelectable();
		boolean isOrderable = annotation.isOrderable();
		QueryInfoJoinType joinType = annotation.joinType();

		return name(fieldName)
				.isOrderable(isOrderable)
				.isPredicateable(isPredicateable)
				.isSelectable(isSelectable)
				.joinType(joinType);
	}

	public QueryInfoFieldInfo build() {
		QueryInfoFieldInfo result = new DefaultQueryInfoFieldInfo();

		result.setIsOrderable(builderReferenceInstance.getIsOrderable());
		result.setIsPredicateable(builderReferenceInstance.getIsPredicateable());
		result.setIsSelectable(builderReferenceInstance.getIsSelectable());
		result.setJpaAttributeName(builderReferenceInstance.getJpaAttributeName());
		result.setJoinType(builderReferenceInstance.getJoinType());
		result.setName(builderReferenceInstance.getName());

		return result;
	}

	public static QueryInfoFieldInfoBuilder create() {
		return new QueryInfoFieldInfoBuilder();
	}

	public QueryInfoFieldInfoBuilder isOrderable(Boolean isOrderable) {
		builderReferenceInstance.setIsOrderable(isOrderable);
		return this;
	}

	public QueryInfoFieldInfoBuilder isPredicateable(Boolean isPredicateable) {
		builderReferenceInstance.setIsPredicateable(isPredicateable);
		return this;
	}

	public QueryInfoFieldInfoBuilder isSelectable(Boolean isSelectable) {
		builderReferenceInstance.setIsSelectable(isSelectable);
		return this;
	}

	public QueryInfoFieldInfoBuilder joinType(QueryInfoJoinType joinType) {
		builderReferenceInstance.setJoinType(joinType);
		return this;
	}

	public QueryInfoFieldInfoBuilder jpaAttributeName(String jpaAttributeName) {
		builderReferenceInstance.setJpaAttributeName(jpaAttributeName);
		return this;
	}

	public QueryInfoFieldInfoBuilder name(String fieldName) {
		builderReferenceInstance.setName(fieldName);
		return this;
	}
}
