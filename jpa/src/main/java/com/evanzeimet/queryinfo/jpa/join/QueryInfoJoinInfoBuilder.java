package com.evanzeimet.queryinfo.jpa.join;

import javax.persistence.criteria.JoinType;

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


public class QueryInfoJoinInfoBuilder {

	private QueryInfoJoinInfo builderReferenceInstance = new DefaultQueryInfoJoinInfo();

	public QueryInfoJoinInfoBuilder() {

	}

	public QueryInfoJoinInfo build() {
		QueryInfoJoinInfo result = new DefaultQueryInfoJoinInfo();

		result.setJpaAttributeName(builderReferenceInstance.getJpaAttributeName());
		result.setJoinType(builderReferenceInstance.getJoinType());
		result.setName(builderReferenceInstance.getName());

		return result;
	}

	public QueryInfoJoinInfoBuilder annotation(QueryInfoJoin annotation) {
		JoinType joinType = annotation.joinType();
		String name = annotation.name();
		return joinType(joinType)
				.name(name);
	}

	public static QueryInfoJoinInfoBuilder create() {
		return new QueryInfoJoinInfoBuilder();
	}

	public QueryInfoJoinInfoBuilder jpaAttributeName(String jpaAttributeName) {
		builderReferenceInstance.setJpaAttributeName(jpaAttributeName);
		return this;
	}

	public QueryInfoJoinInfoBuilder joinType(JoinType joinType) {
		builderReferenceInstance.setJoinType(joinType);
		return this;
	}

	public QueryInfoJoinInfoBuilder name(String name) {
		builderReferenceInstance.setName(name);
		return this;
	}

}
