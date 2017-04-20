package com.evanzeimet.queryinfo.jpa.result;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoTestUtils;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TupleToObjectNodeQueryInfoResultConverterTest {

	private TupleToObjectNodeQueryInfoResultConverter converter;
	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		converter = new TupleToObjectNodeQueryInfoResultConverter();

		objectMapper = new QueryInfoUtils().createObjectMapper();
	}

	@Test
	public void convertTuple_nestedPojo() throws IOException,
			QueryInfoException {
		Tuple tuple = mock(Tuple.class);

		List<TupleElement<?>> tupleElements = new ArrayList<>();
		TupleElement<?> tupleElement;
		String fieldName;
		Object fieldValue;

		{
			fieldName = "firstName";
			fieldValue = "Evan";

			tupleElement = mock(TupleElement.class);

			doReturn(fieldName).when(tupleElement).getAlias();
			doReturn(fieldValue).when(tuple).get(tupleElement);

			tupleElements.add(tupleElement);
		}

		{
			fieldName = "lastName";
			fieldValue = "Zeimet";

			tupleElement = mock(TupleElement.class);

			doReturn(fieldName).when(tupleElement).getAlias();
			doReturn(fieldValue).when(tuple).get(tupleElement);

			tupleElements.add(tupleElement);
		}

		{
			Employer employer = new Employer();
			employer.setName("Some Company");

			fieldName = "employer";
			fieldValue = employer;

			tupleElement = mock(TupleElement.class);

			doReturn(fieldName).when(tupleElement).getAlias();
			doReturn(fieldValue).when(tuple).get(tupleElement);

			tupleElements.add(tupleElement);
		}

		doReturn(tupleElements).when(tuple).getElements();

		ObjectNode actualObjectNode = converter.convertTuple(tuple, objectMapper);

		String actualJson = QueryInfoTestUtils.createActualJson(actualObjectNode);
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"TupleToObjectNodeQueryInfoResultConverterTest_convertTuple_nestedPojo_expected.json");

		assertEquals(expectedJson, actualJson);
	}

	private static class Employer {
		private String name;

		public Employer() {

		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
