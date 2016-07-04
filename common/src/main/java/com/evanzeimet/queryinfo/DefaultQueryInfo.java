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

import java.util.List;

import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.DefaultConditionGroup;
import com.evanzeimet.queryinfo.pagination.DefaultPaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.selection.DefaultSelection;
import com.evanzeimet.queryinfo.selection.Selection;
import com.evanzeimet.queryinfo.sort.DefaultSort;
import com.evanzeimet.queryinfo.sort.Sort;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultQueryInfo implements QueryInfo {

	@JsonDeserialize(as = DefaultConditionGroup.class)
	private ConditionGroup conditionGroup;
	private List<String> groupByAttributePaths;
	@JsonDeserialize(as = DefaultPaginationInfo.class)
	private PaginationInfo paginationInfo;
	@JsonDeserialize(contentAs = DefaultSelection.class)
	private List<Selection> selections;
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
	public List<String> getGroupByAttributePaths() {
		return groupByAttributePaths;
	}

	@Override
	public void setGroupByAttributePaths(List<String> groupByAttributePaths) {
		this.groupByAttributePaths = groupByAttributePaths;
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
	public List<Selection> getSelections() {
		return selections;
	}

	@Override
	public void setSelections(List<Selection> selections) {
		this.selections = selections;
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
