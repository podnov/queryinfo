package com.evanzeimet.queryinfo.jpa.predicate;

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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import javax.persistence.criteria.Expression;

import org.junit.Before;
import org.junit.Test;

public class FieldValueParserTest {

	private FieldValueParser parser;
	private Expression<?> path;

	@Before
	public void setUp() {
		parser = new FieldValueParser();

		path = mock(Expression.class);
	}

	@Test
	public void parse_boolean_false() {
		String fieldValue = "false";

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path, fieldValue);

		assertFalse(actual);
	}

	@Test
	public void parse_boolean_somethingElse() {
		String fieldValue = "somethingElse";

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path, fieldValue);

		assertFalse(actual);
	}

	@Test
	public void parse_boolean_true() {
		String fieldValue = "true";

		doReturn(Boolean.class).when(path).getJavaType();

		Boolean actual = (Boolean) parser.parse(path, fieldValue);

		assertTrue(actual);
	}
}
