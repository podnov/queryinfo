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

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;

public class AllPaginatedResultsIterator<T> implements Iterator<T> {

	protected Iterator<T> currentPageResultsIterator;
	protected PaginatedResultIterator<T> pageIterator;

	public AllPaginatedResultsIterator(QueryInfoBean<?, ?, T> queryInfoBean, QueryInfo queryInfo) {
		pageIterator = new PaginatedResultIterator<T>(queryInfoBean, queryInfo);
	}

	@Override
	public boolean hasNext() {
		boolean result;
		boolean zeroPages = false;

		if (currentPageResultsIterator == null) {
			if (pageIterator.hasNext()) {
				currentPageResultsIterator = pageIterator.next()
						.getPageResults()
						.iterator();
			} else {
				zeroPages = true;
			}
		}

		if (zeroPages) {
			result = false;
		} else {
			result = currentPageResultsIterator.hasNext();

			if (!result && pageIterator.hasNext()) {
				currentPageResultsIterator = pageIterator.next()
						.getPageResults()
						.iterator();

				result = currentPageResultsIterator.hasNext();
			}
		}

		return result;
	}

	@Override
	public T next() {
		T result;

		if (hasNext()) {
			result = currentPageResultsIterator.next();
		} else {
			throw new NoSuchElementException();
		}

		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
