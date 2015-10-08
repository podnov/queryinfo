package com.evanzeimet.queryinfo.pagination;

import java.util.ArrayList;
import java.util.List;

public class DefaultPaginatedResult<T> implements PaginatedResult<T> {

	private List<T> pageResults = new ArrayList<>();
	private Long totalCount;

	public DefaultPaginatedResult() {

	}

	@Override
	public List<T> getPageResults() {
		return pageResults;
	}

	@Override
	public void setPageResults(List<T> pageResults) {
		this.pageResults = pageResults;
	}

	@Override
	public Long getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
