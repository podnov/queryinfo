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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.AbstractQueryInfoBean;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.DefaultPaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public class PaginatedResultIteratorTest {

	private DefaultPaginatedResult<Result> givenLastResult;
	private PaginationInfo givenPaginationInfo;
	private DefaultQueryInfo givenQueryInfo;
	private QueryInfoBean<?, ?, Result> givenQueryInfoBean;
	private PaginatedResultIterator<Result> iterator;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		givenQueryInfoBean = mock(QueryInfoBean.class);

		givenPaginationInfo = new DefaultPaginationInfo();

		givenQueryInfo = new DefaultQueryInfo();
		givenQueryInfo.setPaginationInfo(givenPaginationInfo);

		iterator = new PaginatedResultIterator<Result>(givenQueryInfoBean, givenQueryInfo);
		iterator = spy(iterator);

		givenLastResult = new DefaultPaginatedResult<>();
		iterator.lastResult = givenLastResult;
	}

	@Test
	public void hasNext_true_hasLastResult() throws QueryInfoException {
		Integer givenPageIndex = 0;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		DefaultPaginatedResult<Result> givenLastResult = new DefaultPaginatedResult<>();
		iterator.lastResult = givenLastResult;

		givenLastResult.setTotalCount(100L);

		boolean actual = iterator.hasNext();

		assertTrue(actual);

		verify(givenQueryInfoBean, never()).count(any(QueryInfo.class));
	}

	@Test
	public void hasNext_true_hasLastResult_pageSizeOverTotalCount() {
		Integer givenPageIndex = 0;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(100);

		DefaultPaginatedResult<Result> givenLastResult = new DefaultPaginatedResult<>();
		iterator.lastResult = givenLastResult;

		givenLastResult.setTotalCount(20L);

		boolean actual = iterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_false_hasLastResult_exactCount() {
		Integer givenPageIndex = 1;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		DefaultPaginatedResult<Result> givenLastResult = new DefaultPaginatedResult<>();
		iterator.lastResult = givenLastResult;

		givenLastResult.setTotalCount(1L);

		boolean actual = iterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_false_hasLastResult_nextOverTotal() {
		Integer givenPageIndex = 2;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(5);

		givenLastResult.setTotalCount(7L);

		boolean actual = iterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_true_noLastResult() throws QueryInfoException {
		Integer givenPageIndex = 0;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		doReturn(100L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		iterator.lastResult = null;

		boolean actual = iterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_true_noLastResult_pageSizeOverTotalCount() throws QueryInfoException {
		Integer givenPageIndex = 0;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(100);

		iterator.lastResult = null;

		doReturn(20L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		boolean actual = iterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_false_noLastResult_exactCount() throws QueryInfoException {
		Integer givenPageIndex = 1;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(1);

		doReturn(1L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		iterator.lastResult = null;

		boolean actual = iterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_false_noLastResult_nextOverTotal() throws QueryInfoException {
		Integer givenPageIndex = 2;
		iterator.nextPageIndex = givenPageIndex;

		givenPaginationInfo.setPageIndex(givenPageIndex);
		givenPaginationInfo.setPageSize(5);

		doReturn(7L).when(givenQueryInfoBean)
				.count(givenQueryInfo);

		iterator.lastResult = null;

		boolean actual = iterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void next_setsNextPageIndex() {
		iterator.nextPageIndex = 1;

		doReturn(true).when(iterator)
				.hasNext();

		iterator.next();

		Integer actual = iterator.nextPageIndex;
		Integer expected = 2;

		assertEquals(expected,
				actual);
	}

	@Test
	public void queryForNextPage_hasLastResult_dontCount() throws QueryInfoException {
		givenQueryInfoBean = new TestQueryInfoBean();
		givenQueryInfoBean = spy(givenQueryInfoBean);

		doReturn(42L).when(givenQueryInfoBean).count(any(QueryInfo.class));
		doReturn(Collections.emptyList()).when(givenQueryInfoBean).query(any(QueryInfo.class));

		iterator = new PaginatedResultIterator<Result>(givenQueryInfoBean, givenQueryInfo);
		iterator.lastResult = givenLastResult;

		iterator.queryForNextPage();

		verify(givenQueryInfoBean, never()).count(any(QueryInfo.class));
	}

	private static class Result {

	}

	private static class TestQueryInfoBean extends AbstractQueryInfoBean<Result, Result, Result> {

	}
}
