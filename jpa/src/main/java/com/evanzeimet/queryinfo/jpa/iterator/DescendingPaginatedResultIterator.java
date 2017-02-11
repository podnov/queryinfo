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

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public class DescendingPaginatedResultIterator<T> implements PaginatedResultIterator<T> {

	protected QueryInfo queryInfo;
	protected QueryInfoBean<?, ?, T> queryInfoBean;
	protected Integer nextPageIndex;
	protected Long totalCount;

	public DescendingPaginatedResultIterator(QueryInfoBean<?, ?, T> queryInfoBean, QueryInfo queryInfo) {
		this.queryInfoBean = queryInfoBean;
		this.queryInfo = queryInfo;
	}

	protected Integer getStartPageIndex() {
		Integer result;
		Long totalCount = getTotalCount();

		if (totalCount == 0) {
			result = -1;
		} else {
			PaginationInfo paginationInfo = queryInfo.getPaginationInfo();
			Integer pageSize = paginationInfo.getPageSize();
	
			result = (int) Math.floor(totalCount / pageSize);

			boolean hasNoRemainder = ((totalCount % pageSize) == 0);

			if (hasNoRemainder) {
				result--;
			}

			result -= queryInfo.getPaginationInfo()
					.getPageIndex();
		}

		return result;
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
	
		if (nextPageIndex == null) {
			Long totalCount = getTotalCount();
			result = (totalCount > 0);
		} else {
			result = (nextPageIndex > -1);
		}
	
		return result;
	}

	@Override
	public PaginatedResult<T> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		PaginatedResult<T> result = queryForNextPage();
		nextPageIndex--;

		return result;
	}

	protected PaginatedResult<T> queryForNextPage() {
		if (nextPageIndex == null) {
			nextPageIndex = getStartPageIndex();
		}

		PaginationInfo paginationInfo = queryInfo.getPaginationInfo();
		paginationInfo.setPageIndex(nextPageIndex);

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

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
