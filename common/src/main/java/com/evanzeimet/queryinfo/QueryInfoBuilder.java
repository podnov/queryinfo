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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupBuilder;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfoBuilder;
import com.evanzeimet.queryinfo.sort.Sort;

public class QueryInfoBuilder {

	private QueryInfo builderReferenceInstance = new DefaultQueryInfo();

	public QueryInfoBuilder() {

	}

	public QueryInfo build() {
		return SerializationUtils.clone(builderReferenceInstance);
	}

	public QueryInfoBuilder builderReferenceInstance(QueryInfo builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public QueryInfoBuilder conditionGroup(ConditionGroup conditionGroup) {
		builderReferenceInstance.setConditionGroup(conditionGroup);
		return this;
	}

	public static QueryInfoBuilder create() {
		return new QueryInfoBuilder();
	}

	public static QueryInfoBuilder createForCondition(Condition condition) {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(condition);
		return createForConditions(conditions);
	}

	public static QueryInfoBuilder createForConditionGroups(List<ConditionGroup> conditionGroups) {
		ConditionGroup conditionGroup = ConditionGroupBuilder.create()
				.conditionGroups(conditionGroups)
				.build();
		return create()
				.conditionGroup(conditionGroup);
	}

	public static QueryInfoBuilder createForConditions(List<Condition> conditions) {
		ConditionGroup conditionGroup = ConditionGroupBuilder.create()
				.conditions(conditions)
				.build();
		return create()
				.conditionGroup(conditionGroup);
	}

	public QueryInfoBuilder paginationInfo(PaginationInfo paginationInfo) {
		builderReferenceInstance.setPaginationInfo(paginationInfo);
		return this;
	}

	public QueryInfoBuilder paginationInfoForAll() {
		PaginationInfo paginationInfo = PaginationInfoBuilder.createForAll().build();
		return paginationInfo(paginationInfo);
	}

	public QueryInfoBuilder paginationInfoForOne() {
		PaginationInfo paginationInfo = PaginationInfoBuilder.createForOne().build();
		return paginationInfo(paginationInfo);
	}

	public QueryInfoBuilder requestedFields(List<String> requestedFields) {
		builderReferenceInstance.setRequestedFields(requestedFields);
		return this;
	}

	public QueryInfoBuilder sorts(List<Sort> sorts) {
		builderReferenceInstance.setSorts(sorts);
		return this;
	}
}
