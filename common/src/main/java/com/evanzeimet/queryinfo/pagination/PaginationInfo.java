package com.evanzeimet.queryinfo.pagination;

import java.io.Serializable;

public interface PaginationInfo extends Serializable {

	Integer getPageIndex();

	void setPageIndex(Integer pageIndex);

	Integer getPageSize();

	void setPageSize(Integer pageSize);

}
