package com.evanzeimet.queryinfo.pagination;

/*
 * #%L
 * queryinfo-common
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
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

public class DefaultPaginatedResult<T> implements PaginatedResult<T> {

	private List<T> pageResults;
	private Long totalCount;

	public DefaultPaginatedResult() {

	}

	@Override
	public List<T> getPageResults() {
		return pageResults;
	}

	@Override
	public void setPageResults(List<T> pageResults) {
		this.pageResults = pageResults;
	}

	@Override
	public Long getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
