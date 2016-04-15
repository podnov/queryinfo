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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Date;

import javax.persistence.criteria.Expression;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.fasterxml.jackson.databind.JsonNode;

public class FieldValueParserTest {

	private FieldValueParser parser;
	private Expression<?> path;
	private QueryInfoUtils utils;

	@Before
	public void setUp() {
		parser = new FieldValueParser();

		path = mock(Expression.class);

		utils = new QueryInfoUtils();
	}

	@Test
	public void parse_boolean_false() throws QueryInfoException {
		Boolean givenTypedFieldValue = false;
		
		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		assertFalse(actual);
	}

	@Test
	public void parse_boolean_somethingElse() {
		String givenTypedFieldValue = "somethingElse";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Boolean.class).when(path).getJavaType();

		QueryInfoException actualException = null;

		try {
			parser.parse(path,
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
	public void parse_boolean_true() throws QueryInfoException {
		Boolean givenTypedFieldValue = true;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		assertTrue(actual);
	}

	@Test
	public void parse_date() throws QueryInfoException {
		String givenTypedFieldValue = "1982-05-09T00:00:00";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Date.class).when(path).getJavaType();

		Date actual = (Date) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		long actualMillis = actual.getTime();
		long expectedMillis = 389750400000L;

		assertEquals(expectedMillis, actualMillis);
	}

	@Test
	public void parse_in_integer() throws QueryInfoException {
		Integer[] givenTypedFieldValue = { 1, 2, 3 };

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Integer.class).when(path).getJavaType();

		Integer[] actual = (Integer[]) parser.parse(path,
				ConditionOperator.IN,
				givenFieldValue);

		Integer[] expected = givenTypedFieldValue;

		assertArrayEquals(expected, actual);
	}

	@Test
	public void parse_integer() throws QueryInfoException {
		Integer givenTypedFieldValue = 1;

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(Integer.class).when(path).getJavaType();

		Integer actual = (Integer) parser.parse(path,
				ConditionOperator.EQUAL_TO, 
				givenFieldValue);

		Integer expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}

	@Test
	public void parse_notIn_String() throws QueryInfoException {
		String[] givenTypedFieldValue = { "a", "b", "c" };

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(String.class).when(path).getJavaType();

		String[] actual = (String[]) parser.parse(path,
				ConditionOperator.NOT_IN,
				givenFieldValue);

		String[] expected = givenTypedFieldValue;

		assertArrayEquals(expected, actual);
	}

	@Test
	public void parse_string() throws QueryInfoException {
		String givenTypedFieldValue = "abc";

		JsonNode givenFieldValue = utils.treeify(givenTypedFieldValue);

		doReturn(String.class).when(path).getJavaType();

		String actual = (String) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				givenFieldValue);

		String expected = givenTypedFieldValue;

		assertEquals(expected, actual);
	}
}
