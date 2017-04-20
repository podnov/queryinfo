package com.evanzeimet.queryinfo.pagination;

/*
 * #%L
 * queryinfo-common
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

public class PaginationInfoBuilder {

	private PaginationInfo builderReferenceInstance = createDefaultInstance();

	public PaginationInfoBuilder() {

	}

	public PaginationInfo build() {
		DefaultPaginationInfo result = createDefaultInstance();

		result.setPageIndex(builderReferenceInstance.getPageIndex());
		result.setPageSize(builderReferenceInstance.getPageSize());

		return result;
	}

	public PaginationInfoBuilder builderReferenceInstance(PaginationInfo builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public static PaginationInfoBuilder create() {
		return new PaginationInfoBuilder();
	}

	protected DefaultPaginationInfo createDefaultInstance() {
		return new DefaultPaginationInfo();
	}

	public static PaginationInfoBuilder createForAll() {
		return create()
				.pageIndex(0)
				.pageSize(Integer.MAX_VALUE);
	}

	public static PaginationInfoBuilder createForOne() {
		return create()
				.pageIndex(0)
				.pageIndex(1);
	}

	public PaginationInfoBuilder pageIndex(Integer pageIndex) {
		builderReferenceInstance.setPageIndex(pageIndex);
		return this;
	}

	public PaginationInfoBuilder pageSize(Integer pageSize) {
		builderReferenceInstance.setPageSize(pageSize);
		return this;
	}
}
