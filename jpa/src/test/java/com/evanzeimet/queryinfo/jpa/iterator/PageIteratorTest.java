package com.evanzeimet.queryinfo.jpa.iterator;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.DefaultPaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public class PageIteratorTest {

	private PaginatedResultIterator<Result> givenIterator;
	private DefaultPaginatedResult<Result> givenLastResult;
	private PaginationInfo givenPaginationInfo;
	private DefaultQueryInfo givenQueryInfo;
	private QueryInfoBean<?, ?, Result> givenQueryInfoBean;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		givenQueryInfoBean = mock(QueryInfoBean.class);

		givenPaginationInfo = new DefaultPaginationInfo();

		givenQueryInfo = new DefaultQueryInfo();
		givenQueryInfo.setPaginationInfo(givenPaginationInfo);

		givenIterator = new PaginatedResultIterator<Result>(givenQueryInfoBean, givenQueryInfo);
		givenIterator = spy(givenIterator);

		givenLastResult = new DefaultPaginatedResult<>();
		givenIterator.lastResult = givenLastResult;
	}

	@Test
	public void hasNext_true_hasLastResult() {
		Integer givenPageIndex = 0;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		DefaultPaginatedResult<Result> givenLastResult = new DefaultPaginatedResult<>();
		givenIterator.lastResult = givenLastResult;

		givenLastResult.setTotalCount(100L);

		boolean actual = givenIterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_true_hasLastResult_pageSizeOverTotalCount() {
		Integer givenPageIndex = 0;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(100);

		DefaultPaginatedResult<Result> givenLastResult = new DefaultPaginatedResult<>();
		givenIterator.lastResult = givenLastResult;

		givenLastResult.setTotalCount(20L);

		boolean actual = givenIterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_false_hasLastResult_exactCount() {
		Integer givenPageIndex = 1;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		DefaultPaginatedResult<Result> givenLastResult = new DefaultPaginatedResult<>();
		givenIterator.lastResult = givenLastResult;

		givenLastResult.setTotalCount(1L);

		boolean actual = givenIterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_false_hasLastResult_nextOverTotal() {
		Integer givenPageIndex = 2;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(5);

		givenLastResult.setTotalCount(7L);

		boolean actual = givenIterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_true_noLastResult() throws QueryInfoException {
		Integer givenPageIndex = 0;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		doReturn(100L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		givenIterator.lastResult = null;

		boolean actual = givenIterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_true_noLastResult_pageSizeOverTotalCount() throws QueryInfoException {
		Integer givenPageIndex = 0;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(100);

		givenIterator.lastResult = null;

		doReturn(20L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		boolean actual = givenIterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_false_noLastResult_exactCount() throws QueryInfoException {
		Integer givenPageIndex = 1;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		doReturn(1L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		givenIterator.lastResult = null;

		boolean actual = givenIterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_false_noLastResult_nextOverTotal() throws QueryInfoException {
		Integer givenPageIndex = 2;
		givenIterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(5);

		doReturn(7L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		givenIterator.lastResult = null;

		boolean actual = givenIterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void next_setsNextPageIndex() {
		givenIterator.nextPageIndex = 1;

		doReturn(true).when(givenIterator)
				.hasNext();

		givenIterator.next();

		Integer actual = givenIterator.nextPageIndex;
		Integer expected = 2;

		assertEquals(expected,
				actual);
	}

	private static class Result {

	}
}
