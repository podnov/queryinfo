package com.evanzeimet.queryinfo.builder;

import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfoBuilder;

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
		Integer givenPageIndex = 4;
		Integer givenPageSize = 2;

		PaginationInfo actualPaginationInfo = builder.pageIndex(givenPageIndex)
				.pageSize(givenPageSize)
				.build();

		Integer actualPageIndex = actualPaginationInfo.getPageIndex();
		assertEquals(givenPageIndex, actualPageIndex);

		Integer actualPageSize = actualPaginationInfo.getPageSize();
		assertEquals(givenPageSize, actualPageSize);
	}
}
