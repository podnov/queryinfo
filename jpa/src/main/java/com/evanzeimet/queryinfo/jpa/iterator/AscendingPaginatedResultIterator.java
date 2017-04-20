package com.evanzeimet.queryinfo.jpa.iterator;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public class AscendingPaginatedResultIterator<T> implements PaginatedResultIterator<T> {

	private final static Logger logger = Logger.getLogger(AscendingPaginatedResultIterator.class.getName());

	protected Integer nextPageIndex;
	protected QueryInfo queryInfo;
	protected QueryInfoBean<?, ?, T> queryInfoBean;
	protected Long totalCount;

	public AscendingPaginatedResultIterator(QueryInfoBean<?, ?, T> queryInfoBean, QueryInfo queryInfo) {
		this.queryInfoBean = queryInfoBean;
		this.queryInfo = queryInfo;
		this.nextPageIndex = queryInfo.getPaginationInfo()
				.getPageIndex();
	}

	protected Long getTotalCount() {
		if (totalCount == null) {
			try {
				totalCount = queryInfoBean.count(queryInfo);
			} catch (QueryInfoException e) {
				throw new QueryInfoRuntimeException(e);
			}
		}

		return totalCount;
	}

	@Override
	public boolean hasNext() {
		boolean result;
		Long totalCount = getTotalCount();

		PaginationInfo paginationInfo = queryInfo.getPaginationInfo();
		Integer pageSize = paginationInfo.getPageSize();

		Long pageStartIndex = (((long) nextPageIndex) * pageSize);

		if (pageStartIndex < totalCount) {
			result = true;
		} else {
			result = false;
		}

		return result;
	}

	@Override
	public PaginatedResult<T> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		PaginatedResult<T> result = queryForNextPage();
		nextPageIndex++;

		return result;
	}

	protected PaginatedResult<T> queryForNextPage() {
		PaginatedResult<T> result;
		PaginationInfo paginationInfo = queryInfo.getPaginationInfo();
		paginationInfo.setPageIndex(nextPageIndex);

		String message = String.format("Querying for page index [%s]", nextPageIndex);
		logger.log(Level.FINE, message);

		if (totalCount == null) {
			result = queryForPaginatedResult();
		} else {
			result = queryForPageResults();
		}

		return result;
	}

	protected PaginatedResult<T> queryForPageResults() {
		List<T> pageResults;

		try {
			pageResults = queryInfoBean.query(queryInfo);
		} catch (QueryInfoException e) {
			String message = String.format("Could not query for page index [%s]",
					nextPageIndex);
			throw new QueryInfoRuntimeException(message, e);
		}

		PaginatedResult<T> result = new DefaultPaginatedResult<>();

		result.setTotalCount(totalCount);
		result.setPageResults(pageResults);

		return result;
	}

	protected PaginatedResult<T> queryForPaginatedResult() {
		PaginatedResult<T> result;

		try {
			result = queryInfoBean.queryForPaginatedResult(queryInfo);
		} catch (QueryInfoException e) {
			String message = String.format("Could not query for page index [%s]",
					nextPageIndex);
			throw new QueryInfoRuntimeException(message, e);
		}

		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
