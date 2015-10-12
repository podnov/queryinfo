package com.evanzeimet.queryinfo.builder;

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
