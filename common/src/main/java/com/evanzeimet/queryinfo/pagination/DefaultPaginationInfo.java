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

public class DefaultPaginationInfo implements PaginationInfo {

	private static final long serialVersionUID = -1948731101502690045L;

	private Integer pageIndex;
	private Integer pageSize;

	public DefaultPaginationInfo() {

	}

	@Override
	public Integer getPageIndex() {
		return pageIndex;
	}

	@Override
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
