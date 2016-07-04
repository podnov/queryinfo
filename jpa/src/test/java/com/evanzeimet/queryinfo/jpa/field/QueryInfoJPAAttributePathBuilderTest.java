package com.evanzeimet.queryinfo.jpa.field;

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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeType;
import com.evanzeimet.queryinfo.jpa.test.entity.TestEmployeeEntity_;
import com.evanzeimet.queryinfo.jpa.test.entity.TestOrganizationEntity;
import com.evanzeimet.queryinfo.jpa.test.entity.TestOrganizationEntity_;
import com.evanzeimet.queryinfo.jpa.test.entity.TestPersonEntity_;
import com.evanzeimet.queryinfo.jpa.test.entity.TestQueryInfoEntityContextRegistry;

public class QueryInfoJPAAttributePathBuilderTest {

	@Test
	public void buildList_entityContextRegistry() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		List<String> actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.firstName)
				.buildList();

		List<String> expected = Arrays.asList("employees", "spouse", "firstName");

		assertEquals(expected, actual);
	}

	@Test
	public void buildList_noEntityContextRegistry() {
		List<String> actual = QueryInfoJPAAttributePathBuilder.create()
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.firstName)
				.buildList();

		List<String> expected = Arrays.asList("employeeEntities", "spouse", "firstName");

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_clear() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();
	
		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.mappedSuperclassField)
				.clear()
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.firstName)
				.buildString();
		String expected = "employees.firstName";
	
		assertEquals(expected, actual);
	}

	@Test
	public void buildString_entityContextRegistry() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.firstName)
				.buildString();
		String expected = "employees.spouse.firstName";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_leafAttributeType_field() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.buildString(QueryInfoAttributeType.FIELD);
		String expected = "employees.spouseEntity";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_leafAttributeType_join() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.buildString(QueryInfoAttributeType.JOIN);
		String expected = "employees.spouse";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_leafAttributeType_unspecified_preferField() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.buildString();
		String expected = "employees.spouseEntity";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_leafAttributeType_unspecified_fallbackToJoin() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.buildString();
		String expected = "employees";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_noEntityContextRegistry() {
		String actual = QueryInfoJPAAttributePathBuilder.create()
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.firstName)
				.buildString();
		String expected = "employeeEntities.spouse.firstName";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_entityContextRegistry_mappedSuperclass() {
		TestQueryInfoEntityContextRegistry entityContextRegistry = TestQueryInfoEntityContextRegistry.create();

		String actual = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.mappedSuperclassField)
				.buildString();
		String expected = "employees.spouse.mappedSuperclassFieldRenamed";

		assertEquals(expected, actual);
	}

	@Test
	public void buildString_noEntityContextRegistry_mappedSuperclass() {
		String actual = QueryInfoJPAAttributePathBuilder.create()
				.root(TestOrganizationEntity.class)
				.add(TestOrganizationEntity_.employeeEntities)
				.add(TestEmployeeEntity_.spouse)
				.add(TestPersonEntity_.mappedSuperclassField)
				.buildString();
		String expected = "employeeEntities.spouse.mappedSuperclassField";

		assertEquals(expected, actual);
	}

}
