package com.evanzeimet.queryinfo.selection;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AggregateFunctionDeserializerTest {

	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		objectMapper = new QueryInfoUtils().createObjectMapper();
	}

	@Test
	public void lowerCase() throws JsonParseException, JsonMappingException, IOException {
		AggregateFunction actual = objectMapper.readValue("\"count\"", AggregateFunction.class);
		AggregateFunction expected = AggregateFunction.COUNT;

		assertEquals(expected, actual);
	}

	@Test
	public void mixedCase() throws JsonParseException, JsonMappingException, IOException {
		AggregateFunction actual = objectMapper.readValue("\"CoUnT\"", AggregateFunction.class);
		AggregateFunction expected = AggregateFunction.COUNT;

		assertEquals(expected, actual);
	}

	@Test
	public void nullValue() throws JsonParseException, JsonMappingException, IOException {
		AggregateFunction actual = objectMapper.readValue("null", AggregateFunction.class);

		assertNull(actual);
	}

	@Test
	public void upperCase() throws JsonParseException, JsonMappingException, IOException {
		AggregateFunction actual = objectMapper.readValue("\"COUNT\"", AggregateFunction.class);
		AggregateFunction expected = AggregateFunction.COUNT;

		assertEquals(expected, actual);
	}

}
