package com.evanzeimet.queryinfo.jpa.field;

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeType;
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


public class DefaultQueryInfoFieldInfo implements QueryInfoFieldInfo {

	private String jpaAttributeName;
	private Boolean isQueryable;
	private Boolean isSelectable;
	private Boolean isSortable;
	private QueryInfoJoinType joinType;
	private String name;

	public DefaultQueryInfoFieldInfo() {

	}

	@Override
	public QueryInfoAttributeType getAttributeType() {
		return QueryInfoAttributeType.FIELD;
	}

	@Override
	public Boolean getIsQueryable() {
		return isQueryable;
	}

	@Override
	public void setIsQueryable(Boolean isQueryable) {
		this.isQueryable = isQueryable;
	}

	@Override
	public Boolean getIsSelectable() {
		return isSelectable;
	}

	@Override
	public void setIsSelectable(Boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	@Override
	public Boolean getIsSortable() {
		return isSortable;
	}

	@Override
	public void setIsSortable(Boolean isSortable) {
		this.isSortable = isSortable;
	}

	@Override
	public String getJpaAttributeName() {
		return jpaAttributeName;
	}

	@Override
	public void setJpaAttributeName(String entityAttributeName) {
		this.jpaAttributeName = entityAttributeName;
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
