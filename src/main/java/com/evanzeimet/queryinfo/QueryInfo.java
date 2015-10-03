package com.evanzeimet.queryinfo;

import java.io.Serializable;
import java.util.List;

public interface QueryInfo extends Serializable {

    ConditionGroup getConditionGroup();

    void setConditionGroup(ConditionGroup conditionGroup);

    PaginationInfo getPaginationInfo();

    void setPaginationInfo(PaginationInfo paginationInfo);

    List<String> getRequestedFieldNames();

    void setRequestedFieldNames(List<String> requestedFields);

    List<Sort> getSorts();

    void setSorts(List<Sort> sorts);
}
