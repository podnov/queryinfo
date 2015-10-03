package com.evanzeimet.queryinfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DefaultQueryInfo implements QueryInfo {

    private static final long serialVersionUID = 5902877613942546416L;

    @JsonDeserialize(as = DefaultConditionGroup.class)
    private ConditionGroup conditionGroup;
    @JsonDeserialize(as = DefaultPaginationInfo.class)
    private PaginationInfo paginationInfo;
    private List<String> requestedFields = new ArrayList<String>();
    @JsonDeserialize(contentAs = DefaultSort.class)
    private List<Sort> sorts = new ArrayList<Sort>();

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
