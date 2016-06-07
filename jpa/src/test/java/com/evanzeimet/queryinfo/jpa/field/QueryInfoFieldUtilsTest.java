package com.evanzeimet.queryinfo.jpa.field;

/*
 * #%L
 * queryinfo-jpa
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoBuilder;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoTestUtils;

public class QueryInfoFieldUtilsTest {

	private QueryInfoFieldUtils utils;

	@Before
	public void setUp() {
		utils = new QueryInfoFieldUtils();
	}

	@Test
	public void hasAnyFieldName_nestedConditionGroup_false() throws IOException, QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoFieldUtilsTest_hasAnyFieldName_nestedConditionGroup_false_given.json");
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(givenQueryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_nestedConditionGroup_true() throws IOException, QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoFieldUtilsTest_hasAnyFieldName_nestedConditionGroup_true_given.json");
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(givenQueryInfo, fieldNames);

		assertTrue(actual);
	}

	@Test
	public void hasAnyFieldName_requestedFields_true() {
		List<String> requestedFields = Arrays.asList("field3");

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.requestedFields(requestedFields)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertTrue(actual);
	}

	@Test
	public void hasAnyFieldName_requestedFields_false() {
		List<String> requestedFields = Arrays.asList("field2");

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.requestedFields(requestedFields)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_rootConditionGroup_false() throws IOException, QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoFieldUtilsTest_hasAnyFieldName_rootConditionGroup_false_given.json");
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(givenQueryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_rootConditionGroup_true() throws IOException, QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoFieldUtilsTest_hasAnyFieldName_rootConditionGroup_true_given.json");
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(givenQueryInfo, fieldNames);

		assertTrue(actual);
	}

	@Test
	public void hasAnyFieldName_sorts_false() throws IOException, QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoFieldUtilsTest_hasAnyFieldName_sorts_false_given.json");
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(givenQueryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_sorts_true() throws IOException, QueryInfoException {
		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoFieldUtilsTest_hasAnyFieldName_sorts_true_given.json");
		DefaultQueryInfo givenQueryInfo = QueryInfoTestUtils.objectify(givenJson, DefaultQueryInfo.class);

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(givenQueryInfo, fieldNames);

		assertTrue(actual);
	}

	@Test
	public void hasRequestedAllFields_emptyList() {
		QueryInfo queryInfo = new DefaultQueryInfo();

		List<String> requestedFields = Collections.emptyList();

		queryInfo.setRequestedFields(requestedFields);

		boolean actual = utils.hasRequestedAllFields(queryInfo);

		assertFalse(actual);
	}

	@Test
	public void hasRequestedAllFields_notEmptyList() {
		QueryInfo queryInfo = new DefaultQueryInfo();

		List<String> requestedFields = Arrays.asList("field1");

		queryInfo.setRequestedFields(requestedFields);

		boolean actual = utils.hasRequestedAllFields(queryInfo);

		assertFalse(actual);
	}

	@Test
	public void hasRequestedAllFields_null() {
		QueryInfo queryInfo = new DefaultQueryInfo();

		List<String> requestedFields = null;

		queryInfo.setRequestedFields(requestedFields);

		boolean actual = utils.hasRequestedAllFields(queryInfo);

		assertTrue(actual);
	}
}
