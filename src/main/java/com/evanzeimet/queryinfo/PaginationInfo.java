package com.evanzeimet.queryinfo;

import java.io.Serializable;

public interface PaginationInfo extends Serializable {

    Long getPageIndex();

    void setPageIndex(Long pageIndex);

    Long getPageSize();

    void setPageSize(Long pageSize);

}
