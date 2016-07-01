package com.evanzeimet.queryinfo.jpa.result;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.test.TupleElementImpl;
import com.evanzeimet.queryinfo.jpa.test.TupleImpl;

public class AbstractTupleToPojoQueryInfoResultConverterTest {

	@Test
	public void convert_mixed() throws QueryInfoException {
		List<TupleElement<?>> tupleElements = new ArrayList<>();

		TupleElement<?> tupleElement = new TupleElementImpl<>("firstName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("hairColor", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("lastName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("spouse.firstName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("spouse.lastName", String.class);
		tupleElements.add(tupleElement);

		List<Tuple> tuples = new ArrayList<>();

		Object[] tupleValues = new Object[] {
				"Evan",
				"Brown",
				"Zeimet",
				"Skye",
				"Spurgat"
		};

		Tuple tuple = new TupleImpl(tupleElements, tupleValues);
		tuples.add(tuple);

		TestMixedConverter converter = new TestMixedConverter();
		List<TestMixedResult> actual = converter.convert(tuples);

		assertThat(actual, hasSize(1));

		TestMixedResult actualPojo = actual.iterator().next();

		assertNotNull(actualPojo);

		String actualFirstName = actualPojo.firstName;
		assertEquals("Evan", actualFirstName);

		String actualHairColor = actualPojo.hairColor;
		assertEquals("Brown", actualHairColor);

		String actualLastName = actualPojo.lastName;
		assertEquals("Zeimet", actualLastName);

		String actualSpouseFirstName = actualPojo.spouseFirstName;
		assertEquals("Skye", actualSpouseFirstName);

		String actualSpouseLastName = actualPojo.spouseLastName;
		assertEquals("Spurgat", actualSpouseLastName);
	}

	@Test
	public void mapElementMethodHandles_fields() throws QueryInfoException {
		List<TupleElement<?>> tupleElements = new ArrayList<>();

		TupleElement<?> tupleElement = new TupleElementImpl<>("firstName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("hairColor", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("lastName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("spouse.firstName", String.class);
		tupleElements.add(tupleElement);

		TestFieldsConverter converter = new TestFieldsConverter();
		Map<String, MethodHandle> actual = converter.mapElementMethodHandles(tupleElements);

		/*
		 * Java 7 has no good way to get MethodHandle info, just verify size
		 * here.
		 */
		assertThat(actual.entrySet(), hasSize(4));
	}

	@Test
	public void mapElementMethodHandles_fields_declaredInSuperClass() throws QueryInfoException {
		List<TupleElement<?>> tupleElements = new ArrayList<>();

		TupleElement<?> tupleElement = new TupleElementImpl<>("base", String.class);
		tupleElements.add(tupleElement);

		TestFieldsConverter converter = new TestFieldsConverter();
		Map<String, MethodHandle> actual = converter.mapElementMethodHandles(tupleElements);

		assertThat(actual.entrySet(), hasSize(1));
	}

	@Test
	public void mapElementMethodHandles_properties() throws QueryInfoException {
		List<TupleElement<?>> tupleElements = new ArrayList<>();

		TupleElement<?> tupleElement = new TupleElementImpl<>("firstName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("hairColor", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("lastName", String.class);
		tupleElements.add(tupleElement);

		tupleElement = new TupleElementImpl<>("spouse.firstName", String.class);
		tupleElements.add(tupleElement);

		TestPropertiesConverter converter = new TestPropertiesConverter();
		Map<String, MethodHandle> actual = converter.mapElementMethodHandles(tupleElements);

		assertThat(actual.entrySet(), hasSize(4));
	}

	@Test
	public void mapElementMethodHandles_properties_declaredInSuperClass() throws QueryInfoException {
		List<TupleElement<?>> tupleElements = new ArrayList<>();

		TupleElement<?> tupleElement = new TupleElementImpl<>("base", String.class);
		tupleElements.add(tupleElement);

		TestPropertiesConverter converter = new TestPropertiesConverter();
		Map<String, MethodHandle> actual = converter.mapElementMethodHandles(tupleElements);

		assertThat(actual.entrySet(), hasSize(1));
	}

	@Test
	public void mapElementMethodHandles_undefined() {
		List<TupleElement<?>> tupleElements = new ArrayList<>();

		TupleElement<?> tupleElement = new TupleElementImpl<>("thisDoesNotExist", String.class);
		tupleElements.add(tupleElement);

		TestFieldsConverter converter = new TestFieldsConverter();
		QueryInfoException actualException = null;

		try {
			converter.mapElementMethodHandles(tupleElements);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualMessage = actualException.getMessage();
		String expectedMessage = "Could not find accessible setter or field for attribute [thisDoesNotExist]";

		assertEquals(expectedMessage, actualMessage);
	}

	@SuppressWarnings("unused")
	private static class TestBaseFieldsResult {

		private String base;

	}

	@SuppressWarnings("unused")
	private static class TestFieldsResult extends TestBaseFieldsResult {

		private String firstName;
		private String hairColor;
		private String lastName;
		private String spouseFirstName;

	}

	@SuppressWarnings("unused")
	private static class TestMixedResult {
		private String firstName;
		private String hairColor;
		private String lastName;
		private String spouseFirstName;
		private String spouseLastName;

		public String getSpouseFirstName() {
			return spouseFirstName;
		}

		public void setSpouseFirstName(String spouseFirstName) {
			this.spouseFirstName = spouseFirstName;
		}

		public String getSpouseLastName() {
			return spouseLastName;
		}

		public void setSpouseLastName(String spouseLastName) {
			this.spouseLastName = spouseLastName;
		}
	}

	@SuppressWarnings("unused")
	private static class TestBasePropertiesResult {

		private String base;

		public String getBase() {
			return base;
		}

		public void setBase(String base) {
			this.base = base;
		}
	}

	@SuppressWarnings("unused")
	private static class TestPropertiesResult extends TestBasePropertiesResult {

		private String firstName;
		private String hairColor;
		private String lastName;
		private String spouseFirstName;

		public TestPropertiesResult() {

		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getHairColor() {
			return hairColor;
		}

		public void setHairColor(String hairColor) {
			this.hairColor = hairColor;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getSpouseFirstName() {
			return spouseFirstName;
		}

		public void setSpouseFirstName(String spouseFirstName) {
			this.spouseFirstName = spouseFirstName;
		}
	}

	private static class TestFieldsConverter extends AbstractTupleToPojoQueryInfoResultConverter<TestFieldsResult> {

		private QueryInfoBaseInstanceFactory<TestFieldsResult> baseInstanceFactory;

		public TestFieldsConverter() {
			baseInstanceFactory = new QueryInfoBaseInstanceFactory<TestFieldsResult>() {

				@Override
				public TestFieldsResult create() {
					return new TestFieldsResult();
				}

			};
		}

		@Override
		public QueryInfoBaseInstanceFactory<TestFieldsResult> getBaseInstanceFactory() {
			return baseInstanceFactory;
		}

		@Override
		public Class<TestFieldsResult> getResultClass() {
			return TestFieldsResult.class;
		}

	}

	private static class TestMixedConverter
			extends AbstractTupleToPojoQueryInfoResultConverter<TestMixedResult> {

		private QueryInfoBaseInstanceFactory<TestMixedResult> baseInstanceFactory;

		public TestMixedConverter() {
			baseInstanceFactory = new QueryInfoBaseInstanceFactory<TestMixedResult>() {

				@Override
				public TestMixedResult create() {
					return new TestMixedResult();
				}

			};
		}

		@Override
		public QueryInfoBaseInstanceFactory<TestMixedResult> getBaseInstanceFactory() {
			return baseInstanceFactory;
		}

		@Override
		public Class<TestMixedResult> getResultClass() {
			return TestMixedResult.class;
		}

	}

	private static class TestPropertiesConverter
			extends AbstractTupleToPojoQueryInfoResultConverter<TestPropertiesResult> {

		private QueryInfoBaseInstanceFactory<TestPropertiesResult> baseInstanceFactory;

		public TestPropertiesConverter() {
			baseInstanceFactory = new QueryInfoBaseInstanceFactory<TestPropertiesResult>() {

				@Override
				public TestPropertiesResult create() {
					return new TestPropertiesResult();
				}

			};
		}

		@Override
		public QueryInfoBaseInstanceFactory<TestPropertiesResult> getBaseInstanceFactory() {
			return baseInstanceFactory;
		}

		@Override
		public Class<TestPropertiesResult> getResultClass() {
			return TestPropertiesResult.class;
		}

	}

}
