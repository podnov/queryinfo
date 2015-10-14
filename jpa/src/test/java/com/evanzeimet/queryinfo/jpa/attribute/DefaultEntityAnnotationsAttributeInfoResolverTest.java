package com.evanzeimet.queryinfo.jpa.attribute;

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
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolver;
import com.evanzeimet.queryinfo.jpa.field.DefaultQueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.DefaultQueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoin;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;

public class DefaultEntityAnnotationsAttributeInfoResolverTest {

	private DefaultEntityAnnotationsAttributeInfoResolver<EmployeeEntity> resolver;

	@Before
	public void setUp() {
		resolver = new DefaultEntityAnnotationsAttributeInfoResolver<EmployeeEntity>(EmployeeEntity.class);
	}

	@Test
	public void createFieldInfo() throws NoSuchMethodException,
			QueryInfoException {
		Method annotatedMethod = getStuffAndThingsGetter();

		QueryInfoFieldInfo actualFieldInfo = resolver.createFieldInfo(annotatedMethod);

		assertNotNull(actualFieldInfo);

		String actualJpaAttributeName = actualFieldInfo.getJpaAttributeName();
		String expectedJpaAttributeName = "stuffAndThings";

		assertEquals(expectedJpaAttributeName, actualJpaAttributeName);

		String actualFieldName = actualFieldInfo.getName();
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
	public void createJpaAttributeName_getter() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getStuffAndThingsGetter();

		String actual = resolver.createJpaAttributeName(annotatedMethod);

		String expected = "stuffAndThings";

		assertEquals(expected, actual);
	}

	@Test
	public void createJpaAttributeName_nonAccessor() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getIsSomethingElseMethod();

		String actual = resolver.createJpaAttributeName(annotatedMethod);

		String expected = "isSomethingElse";

		assertEquals(expected, actual);
	}

	@Test
	public void createJpaAttributeName_setter() throws NoSuchMethodException,
			SecurityException,
			QueryInfoException {
		Method annotatedMethod = getThingAndStuffSetter();

		String actual = resolver.createJpaAttributeName(annotatedMethod);

		String expected = "thingsAndStuff";

		assertEquals(expected, actual);
	}

	@Test
	public void validateFieldNameUniqueness_notUnique() throws QueryInfoException {
		List<QueryInfoFieldInfo> fieldInfos = new ArrayList<>();

		QueryInfoFieldInfo fieldInfo;

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field1");
			fieldInfo.setJpaAttributeName("field1");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field1");
			fieldInfo.setJpaAttributeName("fieldOne");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field2");
			fieldInfo.setJpaAttributeName("field2");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field2");
			fieldInfo.setJpaAttributeName("fieldTwo");

			fieldInfos.add(fieldInfo);
		}

		List<QueryInfoJoinInfo> joinInfos = new ArrayList<>();

		QueryInfoJoinInfo joinInfo;

		{
			joinInfo = new DefaultQueryInfoJoinInfo();

			joinInfo.setName("join1");
			joinInfo.setJpaAttributeName("join1");

			joinInfos.add(joinInfo);
		}

		{
			joinInfo = new DefaultQueryInfoJoinInfo();

			joinInfo.setName("join1");
			joinInfo.setJpaAttributeName("joinOne");

			joinInfos.add(joinInfo);
		}

		QueryInfoRuntimeException actualException = null;

		try {
			resolver.validateAttributeNameUniqueness(fieldInfos, joinInfos);
		} catch (QueryInfoRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = String.format("Found [3] non-unique attribute names for entity [com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest.EmployeeEntity]:%n"
				+ "Found [2] field infos for name [field1]: field1, fieldOne.%n"
				+ "Found [2] field infos for name [field2]: field2, fieldTwo.%n"
				+ "Found [2] field infos for name [join1]: join1, joinOne.");

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateAttributeNameUniqueness_unique() {
		List<QueryInfoFieldInfo> fieldInfos = new ArrayList<>();

		QueryInfoFieldInfo fieldInfo;

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field1");
			fieldInfo.setJpaAttributeName("fieldTwo");

			fieldInfos.add(fieldInfo);
		}

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field2");
			fieldInfo.setJpaAttributeName("fieldTwo");

			fieldInfos.add(fieldInfo);
		}

		List<QueryInfoJoinInfo> joinInfos = new ArrayList<>();

		QueryInfoJoinInfo joinInfo;

		{
			joinInfo = new DefaultQueryInfoJoinInfo();

			joinInfo.setName("join1");
			joinInfo.setJpaAttributeName("joinOne");

			joinInfos.add(joinInfo);
		}

		{
			joinInfo = new DefaultQueryInfoJoinInfo();

			joinInfo.setName("join2");
			joinInfo.setJpaAttributeName("joinTwo");

			joinInfos.add(joinInfo);
		}

		QueryInfoRuntimeException actualException = null;

		try {
			resolver.validateAttributeNameUniqueness(fieldInfos, joinInfos);
		} catch (QueryInfoRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	private Method getIsSomethingElseMethod() throws NoSuchMethodException {
		return EmployeeEntity.class.getMethod("isSomethingElse");
	}

	private Method getStuffAndThingsGetter() throws NoSuchMethodException {
		return EmployeeEntity.class.getMethod("getStuffAndThings");
	}

	private Method getThingAndStuffSetter() throws NoSuchMethodException {
		return EmployeeEntity.class.getMethod("setThingsAndStuff");
	}

	private static class EmployeeEntity {

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

		@QueryInfoField(name = "iLikeToOverrideThings")
		public boolean imOverridden() {
			return true;
		}

		@QueryInfoJoin(name = "employer")
		public EmployerEntity getEmployerEntity() {
			return null;
		}
	}

	private static class EmployerEntity {

	}
}
