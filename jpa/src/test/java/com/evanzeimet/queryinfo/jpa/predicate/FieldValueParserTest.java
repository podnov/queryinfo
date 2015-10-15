package com.evanzeimet.queryinfo.jpa.predicate;

import static org.junit.Assert.assertEquals;

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
import static org.junit.Assert.assertNotNull;
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
import com.evanzeimet.queryinfo.condition.ConditionOperator;

public class FieldValueParserTest {

	private FieldValueParser parser;
	private Expression<?> path;

	@Before
	public void setUp() {
		parser = new FieldValueParser();

		path = mock(Expression.class);
	}

	@Test
	public void parse_boolean_false() throws QueryInfoException {
		String fieldValue = "false";

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				fieldValue);

		assertFalse(actual);
	}

	@Test
	public void parse_boolean_somethingElse() {
		String fieldValue = "somethingElse";

		doReturn(Boolean.class).when(path).getJavaType();

		QueryInfoException actualException = null;

		try {
			parser.parse(path,
					ConditionOperator.EQUAL_TO,
					fieldValue);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Could not parse [somethingElse] as [class java.lang.Boolean]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void parse_boolean_true() throws QueryInfoException {
		String fieldValue = "true";

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				fieldValue);

		assertTrue(actual);
	}

	@Test
	public void parse_date() throws QueryInfoException {
		String fieldValue = "\"1982-05-09T00:00:00\"";

		doReturn(Date.class).when(path).getJavaType();

		Date actual = (Date) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				fieldValue);

		long actualMillis = actual.getTime();
		long expectedMillis = 389750400000L;

		assertEquals(expectedMillis, actualMillis);
	}

	@Test
	public void parse_in_integer() throws QueryInfoException {
		String fieldValue = "[1,2,3]";

		doReturn(Integer.class).when(path).getJavaType();

		@SuppressWarnings("unchecked")
		List<Integer> actual = (List<Integer>) parser.parse(path,
				ConditionOperator.IN,
				fieldValue);

		List<Integer> expected = Arrays.asList(1, 2, 3);

		assertEquals(expected, actual);
	}

	@Test
	public void parse_integer() throws QueryInfoException {
		String fieldValue = "1";

		doReturn(Integer.class).when(path).getJavaType();

		Integer actual = (Integer) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				fieldValue);

		Integer expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	public void parse_notIn_String() throws QueryInfoException {
		String fieldValue = "[\"a\",\"b\",\"c\"]";

		doReturn(String.class).when(path).getJavaType();

		@SuppressWarnings("unchecked")
		List<String> actual = (List<String>) parser.parse(path,
				ConditionOperator.NOT_IN,
				fieldValue);

		List<String> expected = Arrays.asList("a","b","c");

		assertEquals(expected, actual);
	}

	@Test
	public void parse_string() throws QueryInfoException {
		String fieldValue = "\"abc\"";

		doReturn(String.class).when(path).getJavaType();

		String actual = (String) parser.parse(path,
				ConditionOperator.EQUAL_TO,
				fieldValue);

		String expected = "abc";

		assertEquals(expected, actual);
	}
}
