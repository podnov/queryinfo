package com.evanzeimet.queryinfo.jpa.predicate;

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
 *    http://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Expression;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.fasterxml.jackson.databind.JsonNode;

public class QueryInfoFieldValueParserTest {

	private QueryInfoFieldValueParser parser;
	private Expression<?> path;
	private QueryInfoUtils utils;

	@Before
	public void setUp() {
		parser = new QueryInfoFieldValueParser();

		path = mock(Expression.class);

		utils = new QueryInfoUtils();
	}

	@Test
	public void parseLiteral_array() throws QueryInfoException {
		Integer[] givenTypedFieldValue = new Integer[] { 1, 2, 3 };

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		@SuppressWarnings("unchecked")
		List<Integer> actual = (List<Integer>) parser.parseLiteral(givenFieldValue);

		List<Integer> expected = Arrays.asList(givenTypedFieldValue);

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteral_boolean() throws QueryInfoException {
		Boolean givenTypedFieldValue = false;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		Boolean actual = (Boolean) parser.parseLiteral(givenFieldValue);

		assertFalse(actual);
	}

	@Test
	public void parseLiteral_integer() throws QueryInfoException {
		Integer givenTypedFieldValue = 1;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		Integer actual = (Integer) parser.parseLiteral(givenFieldValue);

		Integer expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteral_null() throws QueryInfoException {
		Integer givenTypedFieldValue = null;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		Integer actual = (Integer) parser.parseLiteral(givenFieldValue);

		Integer expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteral_string() throws QueryInfoException {
		String givenTypedFieldValue = "abc";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		String actual = (String) parser.parseLiteral(givenFieldValue);

		String expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteralForExpression_boolean_false() throws QueryInfoException {
		Boolean givenTypedFieldValue = false;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parseLiteralForExpression(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		assertFalse(actual);
	}

	@Test
	public void parseLiteralForExpression_boolean_somethingElse() {
		String givenTypedFieldValue = "somethingElse";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Boolean.class).when(path).getJavaType();

		QueryInfoException actualException = null;

		try {
			parser.parseLiteralForExpression(path,
					ConditionOperator.EQUAL_TO,
					givenFieldValue);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Could not parse [\"somethingElse\"] as [class java.lang.Boolean]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void parseLiteralForExpression_boolean_true() throws QueryInfoException {
		Boolean givenTypedFieldValue = true;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parseLiteralForExpression(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		assertTrue(actual);
	}

	@Test
	public void parseLiteralForExpression_date() throws QueryInfoException {
		String givenTypedFieldValue = "1982-05-09T00:00:00";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Date.class).when(path).getJavaType();

		Date actual = (Date) parser.parseLiteralForExpression(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		long actualMillis = actual.getTime();
		long expectedMillis = 389750400000L;

		assertEquals(expectedMillis, actualMillis);
	}

	@Test
	public void parseLiteralForExpression_in_integer() throws QueryInfoException {
		Integer[] givenTypedFieldValue = { 1, 2, 3 };

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Integer.class).when(path).getJavaType();

		@SuppressWarnings("unchecked")
		List<Integer> actual = (List<Integer>) parser.parseLiteralForExpression(path,
				ConditionOperator.IN,
				givenFieldValue);

		List<Integer> expected = Arrays.asList(givenTypedFieldValue);

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteralForExpression_integer() throws QueryInfoException {
		Integer givenTypedFieldValue = 1;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Integer.class).when(path).getJavaType();

		Integer actual = (Integer) parser.parseLiteralForExpression(path,
				ConditionOperator.EQUAL_TO, 
				givenFieldValue);

		Integer expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteralForExpression_isNotNull_typeMismatch() throws QueryInfoException {
		String givenTypedFieldValue = "abc";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Long.class).when(path).getJavaType();

		Long actual = (Long) parser.parseLiteralForExpression(path,
				ConditionOperator.IS_NOT_NULL,
				givenFieldValue);

		assertNull(actual);
	}

	@Test
	public void parseLiteralForExpression_isNull_typeMismatch() throws QueryInfoException {
		String givenTypedFieldValue = "abc";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Long.class).when(path).getJavaType();

		Long actual = (Long) parser.parseLiteralForExpression(path,
				ConditionOperator.IS_NULL,
				givenFieldValue);

		assertNull(actual);
	}

	@Test
	public void parseLiteralForExpression_notIn_string() throws QueryInfoException {
		String[] givenTypedFieldValue = { "a", "b", "c" };

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(String.class).when(path).getJavaType();

		@SuppressWarnings("unchecked")
		List<String> actual = (List<String>) parser.parseLiteralForExpression(path,
				ConditionOperator.NOT_IN,
				givenFieldValue);

		List<String> expected = Arrays.asList(givenTypedFieldValue);

		assertEquals(expected, actual);
	}

	@Test
	public void parseLiteralForExpression_string() throws QueryInfoException {
		String givenTypedFieldValue = "abc";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(String.class).when(path).getJavaType();

		String actual = (String) parser.parseLiteralForExpression(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		String expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}
}
