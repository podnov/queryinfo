package com.evanzeimet.queryinfo;

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


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class QueryInfoUtilsTest {

	private QueryInfoUtils utils;
	private final Integer defaultPageSize = 42;

	@Before
	public void setUp() {
		utils = new QueryInfoUtils();

		utils.setDefaultPageSize(defaultPageSize);
	}

	@Test
	public void coalesceQueryInfo_empty() throws QueryInfoException, IOException {
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify("{}", DefaultQueryInfo.class);

		QueryInfo actualQueryInfo = utils.coalesceQueryInfo(givenQueryInfo);

		String actualJson = QueryInfoTestUtils.createActualJson(actualQueryInfo);
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoUtilsTest_coalesceQueryInfo_empty_expected.json");

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void coalesceQueryInfo_null() throws QueryInfoException, IOException {
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify("null", DefaultQueryInfo.class);

		QueryInfo actualQueryInfo = utils.coalesceQueryInfo(givenQueryInfo);

		String actualJson = QueryInfoTestUtils.createActualJson(actualQueryInfo);
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoUtilsTest_coalesceQueryInfo_null_expected.json");

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void coalesceQueryInfo_populated() throws QueryInfoException, IOException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoUtilsTest_coalesceQueryInfo_populated_given.json");

		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		QueryInfo actualQueryInfo = utils.coalesceQueryInfo(givenQueryInfo);

		String actualJson = QueryInfoTestUtils.createActualJson(actualQueryInfo);
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoUtilsTest_coalesceQueryInfo_populated_expected.json");

		assertEquals(expectedJson, actualJson);
	}
}
