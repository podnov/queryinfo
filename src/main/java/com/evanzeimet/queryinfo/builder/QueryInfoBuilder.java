package com.evanzeimet.queryinfo.builder;

import com.evanzeimet.queryinfo.*;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

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
