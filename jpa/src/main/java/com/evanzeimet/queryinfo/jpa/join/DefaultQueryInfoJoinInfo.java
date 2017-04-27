package com.evanzeimet.queryinfo.jpa.join;

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

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeType;

public class DefaultQueryInfoJoinInfo implements QueryInfoJoinInfo {

	private Class<?> entityClass;
	private String jpaAttributeName;
	private QueryInfoJoinType joinType;
	private String name;

	public DefaultQueryInfoJoinInfo() {

	}

	@Override
	public QueryInfoAttributeType getAttributeType() {
		return QueryInfoAttributeType.JOIN;
	}

	@Override
	public Class<?> getEntityClass() {
		return entityClass;
	}

	@Override
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public String getJpaAttributeName() {
		return jpaAttributeName;
	}

	@Override
	public void setJpaAttributeName(String jpaAttributeName) {
		this.jpaAttributeName = jpaAttributeName;
	}

	@Override
	public QueryInfoJoinType getJoinType() {
		return joinType;
	}

	@Override
	public void setJoinType(QueryInfoJoinType joinType) {
		this.joinType = joinType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
