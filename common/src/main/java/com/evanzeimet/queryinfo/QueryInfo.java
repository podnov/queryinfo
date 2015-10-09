package com.evanzeimet.queryinfo;

import java.io.Serializable;
import java.util.List;

import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.sort.Sort;

public interface QueryInfo extends Serializable {

	ConditionGroup getConditionGroup();

	void setConditionGroup(ConditionGroup conditionGroup);

	PaginationInfo getPaginationInfo();

	void setPaginationInfo(PaginationInfo paginationInfo);

	List<String> getRequestedFieldNames();

	void setRequestedFieldNames(List<String> requestedFieldNames);

	List<Sort> getSorts();

	void setSorts(List<Sort> sorts);
}
