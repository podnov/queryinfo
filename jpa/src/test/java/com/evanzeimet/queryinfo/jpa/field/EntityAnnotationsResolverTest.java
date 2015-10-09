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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;

public class EntityAnnotationsResolverTest {

	private DefaultEntityAnnotationsQueryInfoFieldResolver<TestEntity> resolver;

	@Before
	public void setUp() {
		resolver = new DefaultEntityAnnotationsQueryInfoFieldResolver<TestEntity>(TestEntity.class);
	}

	@Test
	public void createFieldInfo() throws NoSuchMethodException,
			QueryInfoException {
		Method annotatedMethod = getStuffAndThingsGetter();

		QueryInfoFieldInfo actualFieldInfo = resolver.createFieldInfo(annotatedMethod);

		assertNotNull(actualFieldInfo);

		String actualEntityAttributeNameName = actualFieldInfo.getEntityAttributeName();
		String expectedEntityAttributeName = "stuffAndThings";

		assertEquals(expectedEntityAttributeName, actualEntityAttributeNameName);

		String actualFieldName = actualFieldInfo.getFieldName();
		String expectedFieldName = "stuffAndThings";

		assertEquals(expectedFieldName, actualFieldName);

		Boolean actualIsQueryable = actualFieldInfo.getIsQueryable();
		assertTrue(actualIsQueryable);

		Boolean actualIsSelectable = actualFieldInfo.getIsSelectable();
		assertFalse(actualIsSelectable);

		Boolean actualIsSortable = actualFieldInfo.getIsSortable();
		assertTrue(actualIsSortable);
	}

	@Test
	public void createFieldName_getter() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getStuffAndThingsGetter();

		String actual = resolver.createFieldName(annotatedMethod);

		String expected = "stuffAndThings";

		assertEquals(expected, actual);
	}

	@Test
	public void createFieldName_nonAccessor() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getIsSomethingElseMethod();

		String actual = resolver.createFieldName(annotatedMethod);

		String expected = "isSomethingElse";

		assertEquals(expected, actual);
	}

	@Test
	public void createFieldName_override() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getImOverriddenMethod();

		String actual = resolver.createFieldName(annotatedMethod);

		String expected = "iLikeToOverrideThings";

		assertEquals(expected, actual);
	}

	@Test
	public void createFieldName_setter() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getThingAndStuffSetter();

		String actual = resolver.createFieldName(annotatedMethod);

		String expected = "thingsAndStuff";

		assertEquals(expected, actual);
	}

	@Test
	public void validateFieldNameUniqueness_notUnique() throws QueryInfoException {
		List<QueryInfoFieldInfo> fieldInfos = new ArrayList<>();

		QueryInfoFieldInfo fieldInfo;

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setFieldName("field1");
			fieldInfo.setEntityAttributeName("field1");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setFieldName("field1");
			fieldInfo.setEntityAttributeName("fieldOne");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setFieldName("field2");
			fieldInfo.setEntityAttributeName("field2");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setFieldName("field2");
			fieldInfo.setEntityAttributeName("fieldTwo");

			fieldInfos.add(fieldInfo);
		}

		QueryInfoException actualException = null;

		try {
			resolver.validateFieldNameUniqueness(fieldInfos);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = String.format("Found [2] non-unique field names for entity [com.evanzeimet.queryinfo.jpa.field.EntityAnnotationsResolverTest.TestEntity]:%n"
				+ "Found [2] field infos for name [field1]: field1, fieldOne.%n"
				+ "Found [2] field infos for name [field2]: field2, fieldTwo.");

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldNameUniqueness_unique() {
		List<QueryInfoFieldInfo> fieldInfos = new ArrayList<>();

		QueryInfoFieldInfo fieldInfo;

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setFieldName("field1");
			fieldInfo.setEntityAttributeName("fieldTwo");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setFieldName("field2");
			fieldInfo.setEntityAttributeName("fieldTwo");

			fieldInfos.add(fieldInfo);
		}

		QueryInfoException actualException = null;

		try {
			resolver.validateFieldNameUniqueness(fieldInfos);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	private Method getImOverriddenMethod() throws NoSuchMethodException {
		return TestEntity.class.getMethod("imOverridden");
	}

	private Method getIsSomethingElseMethod() throws NoSuchMethodException {
		return TestEntity.class.getMethod("isSomethingElse");
	}

	private Method getStuffAndThingsGetter() throws NoSuchMethodException {
		return TestEntity.class.getMethod("getStuffAndThings");
	}

	private Method getThingAndStuffSetter() throws NoSuchMethodException {
		return TestEntity.class.getMethod("setThingsAndStuff");
	}

	private static class TestEntity {

		private String stuffAndThings;

		@QueryInfoField(isQueryable = true,
				isSelectable = false,
				isSortable = true)
		public String getStuffAndThings() {
			return stuffAndThings;
		}

		@QueryInfoField
		public void setThingsAndStuff() {

		}

		@QueryInfoField
		public boolean isSomethingElse() {
			return true;
		}

		@QueryInfoField(fieldName = "iLikeToOverrideThings")
		public boolean imOverridden() {
			return true;
		}
	}

}
