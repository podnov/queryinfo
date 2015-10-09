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


import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;

public class QueryInfoFieldPathPartsTest {

	private QueryInfoFieldPathParts parts;

	@Before
	public void setUp() {
		parts = new QueryInfoFieldPathParts();
	}

	@Test
	public void consumeJoin() throws QueryInfoException {
		List<String> fromAttributeNames = Arrays.asList("join1", "join2", "join3");

		parts.setJoinAttributeNames(fromAttributeNames);

		String actualFrom = parts.consumeJoin();

		assertEquals("join1", actualFrom);

		actualFrom = parts.consumeJoin();

		assertEquals("join2", actualFrom);

		actualFrom = parts.consumeJoin();

		assertEquals("join3", actualFrom);

		QueryInfoException actualException = null;

		try {
			parts.consumeJoin();
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualMessage = actualException.getMessage();
		String expectedMessage = "No joins to consume";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void fromFullPath_emptyString() {
		QueryInfoException actualException = null;

		try {
			QueryInfoFieldPathParts.fromFullPath("");
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualMessage = actualException.getMessage();
		String expectedMessage = "Found 0 path parts in []";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void fromFullPath_multipleParts() throws QueryInfoException {
		String givenFullPath = "join1.join2.actualField";

		QueryInfoFieldPathParts actual = QueryInfoFieldPathParts.fromFullPath(givenFullPath);

		List<String> actualFromAttributeNames = actual.getJoinAttributeNames();
		List<String> expectedFromAttributeNames = Arrays.asList("join1", "join2");

		assertThat(actualFromAttributeNames, is(expectedFromAttributeNames));

		String actualFieldAttributeName = actual.getFieldAttributeName();
		String expectedFieldAttributeName = "actualField";

		assertEquals(expectedFieldAttributeName, actualFieldAttributeName);
	}

	@Test
	public void fromFullPath_null() {
		QueryInfoException actualException = null;

		try {
			QueryInfoFieldPathParts.fromFullPath(null);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualMessage = actualException.getMessage();
		String expectedMessage = "Found 0 path parts in [null]";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void fromFullPath_singlePart() throws QueryInfoException {
		String givenFullPath = "actualField";

		QueryInfoFieldPathParts actual = QueryInfoFieldPathParts.fromFullPath(givenFullPath);

		List<String> actualFromAttributeNames = actual.getJoinAttributeNames();

		assertThat(actualFromAttributeNames, empty());

		String actualFieldAttributeName = actual.getFieldAttributeName();
		String expectedFieldAttributeName = "actualField";

		assertEquals(expectedFieldAttributeName, actualFieldAttributeName);
	}

	@Test
	public void hasJoins_someJoins() {
		List<String> joinAttributeNames = Arrays.asList("join1", "join2");

		parts.setJoinAttributeNames(joinAttributeNames);

		boolean actual = parts.hasJoins();

		assertTrue(actual);
	}

	@Test
	public void hasJoins_zeroJoins() {
		List<String> joinAttributeNames = Collections.emptyList();

		parts.setJoinAttributeNames(joinAttributeNames);

		boolean actual = parts.hasJoins();

		assertFalse(actual);
	}

	@Test
	public void toString_hasJoins() {
		List<String> joinAttributeNames = Arrays.asList("join1", "join2");

		parts.setFieldAttributeName("field1");
		parts.setJoinAttributeNames(joinAttributeNames);

		String actual = parts.toString();
		String expected = "join1.join2.field1";

		assertEquals(expected, actual);
	}

	@Test
	public void toString_noJoins() {
		List<String> joinAttributeNames = Collections.emptyList();

		parts.setFieldAttributeName("field1");
		parts.setJoinAttributeNames(joinAttributeNames);

		String actual = parts.toString();
		String expected = "field1";

		assertEquals(expected, actual);
	}
}
