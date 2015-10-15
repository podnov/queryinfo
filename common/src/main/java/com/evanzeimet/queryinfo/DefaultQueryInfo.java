package com.evanzeimet.queryinfo;

/*
 * #%L
 * queryinfo-common
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

import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.DefaultConditionGroup;
import com.evanzeimet.queryinfo.pagination.DefaultPaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.sort.DefaultSort;
import com.evanzeimet.queryinfo.sort.Sort;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class DefaultQueryInfo implements QueryInfo {

	private static final long serialVersionUID = 5902877613942546416L;

	@JsonDeserialize(as = DefaultConditionGroup.class)
	private ConditionGroup conditionGroup;
	@JsonDeserialize(as = DefaultPaginationInfo.class)
	private PaginationInfo paginationInfo;
	private List<String> requestedFields;
	@JsonDeserialize(contentAs = DefaultSort.class)
	private List<Sort> sorts;

	public DefaultQueryInfo() {

	}

	@Override
	public ConditionGroup getConditionGroup() {
		return conditionGroup;
	}

	@Override
	public void setConditionGroup(ConditionGroup conditionGroup) {
		this.conditionGroup = conditionGroup;
	}

	@Override
	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	@Override
	public void setPaginationInfo(PaginationInfo paginationInfo) {
		this.paginationInfo = paginationInfo;
	}

	@Override
	public List<String> getRequestedFields() {
		return requestedFields;
	}

	@Override
	public void setRequestedFields(List<String> requestedFields) {
		this.requestedFields = requestedFields;
	}

	@Override
	public List<Sort> getSorts() {
		return sorts;
	}

	@Override
	public void setSorts(List<Sort> sorts) {
		this.sorts = sorts;
	}
}
