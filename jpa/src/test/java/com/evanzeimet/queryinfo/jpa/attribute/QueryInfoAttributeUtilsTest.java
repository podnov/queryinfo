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
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.DefaultQueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.test.entity.TestEmployeeEntity;
import com.evanzeimet.queryinfo.jpa.test.entity.TestOrganizationEntity;
import com.evanzeimet.queryinfo.jpa.test.entity.TestPersonEntity;
import com.evanzeimet.queryinfo.jpa.test.entity.TestQueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.test.utils.QueryInfoJPATestUtils;

public class QueryInfoAttributeUtilsTest {

	private QueryInfoAttributeUtils utils;
	private QueryInfoJPATestUtils jpaTestUtils;

	@Before
	public void setUp() {
		utils = new QueryInfoAttributeUtils();

		jpaTestUtils = new QueryInfoJPATestUtils();
	}

	@Test
	public void convertAttributeNameToMemberName_joins() {
		String givenElementAlias = "employees.spouse.lastName";

		String actual = utils.convertAttributeNameToMemberName(givenElementAlias);
		String expected = "employeesSpouseLastName";

		assertEquals(expected, actual);
	}

	@Test
	public void convertAttributeNameToMemberName_noJoin() {
		String givenElementAlias = "lastName";

		String actual = utils.convertAttributeNameToMemberName(givenElementAlias);
		String expected = "lastName";

		assertEquals(expected, actual);
	}

	@Test
	public void getFieldInfo() {
		Map<String, QueryInfoFieldInfo> givenFields = new HashMap<>();

		QueryInfoFieldInfo givenCityFieldInfo;

		{
			QueryInfoFieldInfo fieldInfo = createFieldInfo("firstName");
			givenFields.put(fieldInfo.getJpaAttributeName(), fieldInfo);
		}
		{
			QueryInfoFieldInfo fieldInfo = createFieldInfo("lastName");
			givenFields.put(fieldInfo.getJpaAttributeName(), fieldInfo);
		}
		{
			QueryInfoFieldInfo fieldInfo = createFieldInfo("address");
			givenFields.put(fieldInfo.getJpaAttributeName(), fieldInfo);
		}
		{
			QueryInfoFieldInfo fieldInfo = createFieldInfo("city");
			givenCityFieldInfo = fieldInfo;
			givenFields.put(fieldInfo.getJpaAttributeName(), fieldInfo);
		}
		{
			QueryInfoFieldInfo fieldInfo = createFieldInfo("state");
			givenFields.put(fieldInfo.getJpaAttributeName(), fieldInfo);
		}

		DefaultQueryInfoAttributeContext givenAttributeContext = new DefaultQueryInfoAttributeContext();
		givenAttributeContext.setFields(givenFields);

		QueryInfoFieldInfo actual = utils.getFieldInfo(givenAttributeContext, "city");
		QueryInfoFieldInfo expected = givenCityFieldInfo;

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getJoinForAttributePath() {
		Root<TestOrganizationEntity> organizationRoot = jpaTestUtils.mockFrom(Root.class,
				TestOrganizationEntity.class);
		Join<?, ?> employeesJoin = jpaTestUtils.mockFrom(Join.class,
				TestEmployeeEntity.class);
		Join<?, ?> employeesSpouseJoin = jpaTestUtils.mockFrom(Join.class,
				TestPersonEntity.class);
		Join<?, ?> employeesSpousesSpouseJoin = jpaTestUtils.mockFrom(Join.class,
				TestPersonEntity.class);

		QueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		QueryInfoJPAContext<TestOrganizationEntity> jpaContext = new QueryInfoJPAContext<>();
		jpaContext.setRoot(organizationRoot);

		{
			// bootstrap jpa joins
			doReturn(employeesJoin).when(organizationRoot).join("employeeEntities");
			doReturn(employeesSpouseJoin).when(employeesJoin).join("spouse");
			doReturn(employeesSpousesSpouseJoin).when(employeesSpouseJoin).join("spouse");
		}

		{
			// bootstrap queryinfo joins
			QueryInfoEntityContext<TestOrganizationEntity> organizationEntityContext = entityContextRegistry.getContext(TestOrganizationEntity.class);
			QueryInfoEntityContext<TestEmployeeEntity> employeeEntityContext = entityContextRegistry.getContext(TestEmployeeEntity.class);
			QueryInfoEntityContext<TestPersonEntity> personEntityContext = entityContextRegistry.getContext(TestPersonEntity.class);

			QueryInfoJoinInfo employeesJoinInfo = organizationEntityContext.getAttributeContext().getJoin("employees");
			jpaContext.getJoin(organizationRoot, employeesJoinInfo);

			QueryInfoJoinInfo employeesSpouseJoinInfo = employeeEntityContext.getAttributeContext().getJoin("spouse");
			jpaContext.getJoin(employeesJoin, employeesSpouseJoinInfo);

			QueryInfoJoinInfo employeesSpousesSpouseJoinInfo = personEntityContext.getAttributeContext().getJoin("spouse");
			jpaContext.getJoin(employeesSpouseJoin, employeesSpousesSpouseJoinInfo);
		}

		List<String> jpaAttributeNames = Arrays.asList("employeeEntities", "spouse", "spouse");

		Join<?, ?> actual = utils.getJoinForAttributePath(entityContextRegistry,
				jpaContext,
				jpaAttributeNames);

		Join<?, ?> expected = employeesSpousesSpouseJoin;

		assertEquals(expected, actual);
	}

	protected QueryInfoFieldInfo createFieldInfo(String jpaAttributeName) {
		DefaultQueryInfoFieldInfo result = new DefaultQueryInfoFieldInfo();
		result.setJpaAttributeName(jpaAttributeName);
		return result;
	}
}
