package com.evanzeimet.queryinfo;

public class DefaultPaginationInfo implements PaginationInfo {

    private static final long serialVersionUID = -1948731101502690045L;

    private Long pageIndex;
    private Long pageSize;

    public DefaultPaginationInfo() {

    }

    @Override
    public Long getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
