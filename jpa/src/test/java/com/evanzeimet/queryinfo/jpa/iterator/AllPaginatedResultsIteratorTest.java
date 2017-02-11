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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoBuilder;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;

public class AllPaginatedResultsIteratorTest {

	private Iterator<Result> givenCurrentPageResultsIterator;
	private AscendingPaginatedResultIterator<Result> givenPageIterator;
	private QueryInfo givenQueryInfo;
	private QueryInfoBean<?, ?, Result> givenQueryInfoBean;
	private AllPaginatedResultsIterator<Result> iterator;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		givenQueryInfoBean = mock(QueryInfoBean.class);

		givenQueryInfo = QueryInfoBuilder.create()
				.paginationInfoForAll()
				.build();

		iterator = new AllPaginatedResultsIterator<Result>(givenQueryInfoBean, givenQueryInfo, PaginatedResultIteratorDirection.ASCENDING);
		iterator = spy(iterator);

		givenPageIterator = mock(AscendingPaginatedResultIterator.class);
		iterator.pageIterator = givenPageIterator;

		givenCurrentPageResultsIterator = mock(Iterator.class);
		iterator.currentPageResultsIterator = givenCurrentPageResultsIterator;
	}

	@Test
	public void hasNext_endOfPage_morePages() {
		Iterator<Result> givenNextPageResultsIterator = mockNextPageResultsIterator();

		doReturn(false).when(givenCurrentPageResultsIterator)
				.hasNext();
		doReturn(true).when(givenPageIterator)
				.hasNext();
		doReturn(true).when(givenNextPageResultsIterator)
				.hasNext();

		boolean actual = iterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_endOfPage_noMorePages() {
		doReturn(false).when(givenCurrentPageResultsIterator)
				.hasNext();
		doReturn(false).when(givenPageIterator)
				.hasNext();

		boolean actual = iterator.hasNext();

		assertFalse(actual);
	}

	@Test
	public void hasNext_midPage() {
		doReturn(true).when(givenCurrentPageResultsIterator)
				.hasNext();
		doReturn(false).when(givenPageIterator)
				.hasNext();

		boolean actual = iterator.hasNext();

		assertTrue(actual);
	}

	@Test
	public void hasNext_zeroPages() {
		iterator.currentPageResultsIterator = null;

		doReturn(false).when(givenPageIterator)
				.hasNext();

		boolean actual = iterator.hasNext();

		assertFalse(actual);
	}

	@SuppressWarnings("unchecked")
	protected Iterator<Result> mockNextPageResultsIterator() {
		PaginatedResult<Result> nextPage = new DefaultPaginatedResult<>();
		doReturn(nextPage).when(givenPageIterator)
				.next();

		List<Result> nextPageResults = mock(List.class);
		nextPage.setPageResults(nextPageResults);

		Iterator<Result> result = mock(Iterator.class);

		doReturn(result).when(nextPageResults)
				.iterator();

		return result;
	}

	private static class Result {

	}
}
