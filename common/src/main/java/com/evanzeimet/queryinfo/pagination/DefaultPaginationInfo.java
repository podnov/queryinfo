package com.evanzeimet.queryinfo.pagination;

public class DefaultPaginationInfo implements PaginationInfo {

	private static final long serialVersionUID = -1948731101502690045L;

	private Integer pageIndex;
	private Integer pageSize;

	public DefaultPaginationInfo() {

	}

	@Override
	public Integer getPageIndex() {
		return pageIndex;
	}

	@Override
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
