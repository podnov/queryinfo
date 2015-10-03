package com.evanzeimet.queryinfo.builder;

import com.evanzeimet.queryinfo.PaginationInfo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaginationInfoBuilderTest {

    private PaginationInfoBuilder builder;

    @Before
    public void setUp() {
        builder = new PaginationInfoBuilder();
    }

    @Test
    public void build() {
        Long givenPageIndex = 4L;
        Long givenPageSize = 2L;

        PaginationInfo actualPaginationInfo = builder.pageIndex(givenPageIndex)
                .pageSize(givenPageSize)
                .build();

        Long actualPageIndex = actualPaginationInfo.getPageIndex();
        assertEquals(givenPageIndex, actualPageIndex);

        Long actualPageSize = actualPaginationInfo.getPageSize();
        assertEquals(givenPageSize, actualPageSize);
    }
}
