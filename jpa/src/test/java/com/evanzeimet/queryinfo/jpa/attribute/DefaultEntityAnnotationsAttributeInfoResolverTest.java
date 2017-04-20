package com.evanzeimet.queryinfo.jpa.attribute;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.jpa.field.DefaultQueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.DefaultQueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoin;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;

public class DefaultEntityAnnotationsAttributeInfoResolverTest {

	private DefaultEntityAnnotationsAttributeInfoResolver<FTEmployeeEntity> resolver;

	@Before
	public void setUp() {
		resolver = new DefaultEntityAnnotationsAttributeInfoResolver<FTEmployeeEntity>(FTEmployeeEntity.class);
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

		String actualName = actualFieldInfo.getName();
		String expectedName = "stuffAndThings";

		assertEquals(expectedName, actualName);

		Boolean actualIsPredicateable = actualFieldInfo.getIsPredicateable();
		assertTrue(actualIsPredicateable);

		Boolean actualIsSelectable = actualFieldInfo.getIsSelectable();
		assertFalse(actualIsSelectable);

		Boolean actualIsOrderable = actualFieldInfo.getIsOrderable();
		assertTrue(actualIsOrderable);
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
	public void createJoinInfo() throws NoSuchMethodException,
			QueryInfoException {
		Method annotatedMethod = getEmployerEntityGetter();

		QueryInfoJoinInfo actualJoinInfo = resolver.createJoinInfo(annotatedMethod);

		assertNotNull(actualJoinInfo);

		String actualJpaAttributeName = actualJoinInfo.getJpaAttributeName();
		String expectedJpaAttributeName = "employerEntity";

		assertEquals(expectedJpaAttributeName, actualJpaAttributeName);

		String actualName = actualJoinInfo.getName();
		String expectedName = "employer";

		assertEquals(expectedName, actualName);

		QueryInfoJoinType actualJoinType = actualJoinInfo.getJoinType();
		QueryInfoJoinType expectedJoinType = QueryInfoJoinType.LEFT;

		assertEquals(expectedJoinType, actualJoinType);
	}

	@Test
	public void createJoinInfo_nameNotSet() throws NoSuchMethodException,
			QueryInfoException {
		Method annotatedMethod = getSpouseEntityGetter();

		QueryInfoJoinInfo actualJoinInfo = resolver.createJoinInfo(annotatedMethod);

		assertNotNull(actualJoinInfo);

		String actualJpaAttributeName = actualJoinInfo.getJpaAttributeName();
		String expectedJpaAttributeName = "spouseEntity";

		assertEquals(expectedJpaAttributeName, actualJpaAttributeName);

		String actualName = actualJoinInfo.getName();
		String expectedName = "spouseEntity";

		assertEquals(expectedName, actualName);

		QueryInfoJoinType actualJoinType = actualJoinInfo.getJoinType();
		QueryInfoJoinType expectedJoinType = QueryInfoJoinType.UNSPECIFIED;

		assertEquals(expectedJoinType, actualJoinType);
	}

	@Test
	public void findAnnotatedMethods() {
		List<Method> actualMethods = resolver.findAnnotatedMethods(FTEmployeeEntity.class,
				QueryInfoField.class);

		List<String> actualMethodIds = convertMethodsToIds(actualMethods);
		Collections.sort(actualMethodIds);

		List<String> expected = Arrays.asList(
				"com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest$FTEmployeeEntity::getEmployerEntity",
				"com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest$FTEmployeeEntity::getStuffAndThings",
				"com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest$FTEmployeeEntity::imOverridden",
				"com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest$FTEmployeeEntity::isSomethingElse",
				"com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest$FTEmployeeEntity::setThingsAndStuff",
				"com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest$PersonEntity::getInherited"
				);

		assertEquals(expected, actualMethodIds);
	}

	@Test
	public void validateFieldNameUniqueness_allowFieldAndJoinNameCollision() throws QueryInfoException {
		List<QueryInfoFieldInfo> fieldInfos = new ArrayList<>();

		QueryInfoFieldInfo fieldInfo;

		{
			fieldInfo = new DefaultQueryInfoFieldInfo();

			fieldInfo.setName("field1");
			fieldInfo.setJpaAttributeName("field1");

			fieldInfos.add(fieldInfo);
		}

		List<QueryInfoJoinInfo> joinInfos = new ArrayList<>();

		QueryInfoJoinInfo joinInfo;

		{
			joinInfo = new DefaultQueryInfoJoinInfo();

			joinInfo.setName("field1");
			joinInfo.setJpaAttributeName("field1");

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
		String expectedExceptionMessage = String.format(
				"Found [3] non-unique attribute names for entity [com.evanzeimet.queryinfo.jpa.attribute.DefaultEntityAnnotationsAttributeInfoResolverTest.FTEmployeeEntity]:%n"
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

	protected List<String> convertMethodsToIds(List<Method> methods) {
		int methodCount = methods.size();
		List<String> result = new ArrayList<String>(methodCount);

		for (Method method : methods) {
			String methodName = method.getName();
			String className = method.getDeclaringClass().getName();

			String methodId = String.format("%s::%s", className, methodName);

			result.add(methodId);
		}

		return result;
	}

	private Method getEmployerEntityGetter() throws NoSuchMethodException {
		return FTEmployeeEntity.class.getMethod("getEmployerEntity");
	}

	private Method getIsSomethingElseMethod() throws NoSuchMethodException {
		return FTEmployeeEntity.class.getMethod("isSomethingElse");
	}

	private Method getSpouseEntityGetter() throws NoSuchMethodException {
		return FTEmployeeEntity.class.getMethod("getSpouseEntity");
	}

	private Method getStuffAndThingsGetter() throws NoSuchMethodException {
		return FTEmployeeEntity.class.getMethod("getStuffAndThings");
	}

	private Method getThingAndStuffSetter() throws NoSuchMethodException {
		return FTEmployeeEntity.class.getMethod("setThingsAndStuff");
	}

	private static class PersonEntity {

		@QueryInfoField
		public Boolean getInherited() {
			return true;
		}

	}

	private static class FTEmployeeEntity extends PersonEntity {

		private String stuffAndThings;

		@QueryInfoField(isPredicateable = true,
				isSelectable = false,
				isOrderable = true)
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

		@QueryInfoField(name = "employer")
		@QueryInfoJoin(name = "employer",
				joinType = QueryInfoJoinType.LEFT)
		public EmployerEntity getEmployerEntity() {
			return null;
		}

		@QueryInfoJoin
		public PersonEntity getSpouseEntity() {
			return null;
		}
	}

	private static class EmployerEntity {

	}
}
