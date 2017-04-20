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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.selection.Selection;
import com.evanzeimet.queryinfo.selection.SelectionBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

public class QueryInfoUtilsTest {

	protected static Type STRING_LIST_TYPE = new TypeReference<ArrayList<String>>() {
	}.getType();

	private QueryInfoUtils utils;
	private final Integer defaultPageSize = 42;

	@Before
	public void setUp() {
		utils = new QueryInfoUtils();

		utils.setDefaultPageSize(defaultPageSize);
	}

	@Test
	public void coalesce_notNull() {
		String givenValue = "given";

		String actual = utils.coalesce(givenValue, "default");
		String expected = givenValue;

		assertEquals(expected, actual);
	}

	@Test
	public void coalesce_null() {
		String givenValue = null;
		String givenDefault = "default";

		String actual = utils.coalesce(givenValue, givenDefault);
		String expected = givenDefault;

		assertEquals(expected, actual);
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

	@Test
	public void createSelectionsForAttributePaths() throws IOException,
			QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoUtilsTest_createSelectionsForAttributePaths_given.json");

		List<String> givenAttributePaths = QueryInfoTestUtils.objectify(givenJson,
				STRING_LIST_TYPE);

		List<Selection> actualSelections = utils.createSelectionsForAttributePaths(givenAttributePaths);

		String actualJson = QueryInfoTestUtils.createActualJson(actualSelections);
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoUtilsTest_createSelectionsForAttributePaths_expected.json");

		assertEquals(expectedJson,
				actualJson);
	}

	@Test
	public void hasRequestedAllFields_emptyList() {
		QueryInfo queryInfo = new DefaultQueryInfo();

		List<Selection> selections = Collections.emptyList();

		queryInfo.setSelections(selections);

		boolean actual = utils.hasRequestedAllFields(queryInfo);

		assertFalse(actual);
	}

	@Test
	public void hasRequestedAllFields_notEmptyList() {
		QueryInfo queryInfo = new DefaultQueryInfo();

		List<Selection> selections = new ArrayList<>();

		Selection selection = SelectionBuilder.create()
				.attributePath("field1")
				.build();
		selections.add(selection);

		queryInfo.setSelections(selections);

		boolean actual = utils.hasRequestedAllFields(queryInfo);

		assertFalse(actual);
	}

	@Test
	public void hasRequestedAllFields_null() {
		QueryInfo queryInfo = new DefaultQueryInfo();

		List<Selection> selections = null;

		queryInfo.setSelections(selections);

		boolean actual = utils.hasRequestedAllFields(queryInfo);

		assertTrue(actual);
	}
}
