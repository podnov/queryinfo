package com.evanzeimet.queryinfo;

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
		builderReferenceInstance.setRequestedFieldNames(requestedFields);
		return this;
	}

	public QueryInfoBuilder sorts(List<Sort> sorts) {
		builderReferenceInstance.setSorts(sorts);
		return this;
	}
}
