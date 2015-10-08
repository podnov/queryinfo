package com.evanzeimet.queryinfo.pagination;

import java.util.List;

public interface PaginatedResult<T> {

	List<T> getPageResults();

	void setPageResults(List<T> pageResults);

	Long getTotalCount();

	void setTotalCount(Long totalCount);

}
