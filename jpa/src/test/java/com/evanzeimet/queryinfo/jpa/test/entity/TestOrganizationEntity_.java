package com.evanzeimet.queryinfo.jpa.test.entity;

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

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.evanzeimet.queryinfo.jpa.test.utils.QueryInfoJPATestUtils;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TestOrganizationEntity.class)
public abstract class TestOrganizationEntity_ {

	public static volatile SingularAttribute<TestOrganizationEntity, String> address1;
	public static volatile SingularAttribute<TestOrganizationEntity, String> address2;
	public static volatile SingularAttribute<TestOrganizationEntity, String> city;
	public static volatile ListAttribute<TestOrganizationEntity, TestEmployeeEntity> employeeEntities;
	public static volatile SingularAttribute<TestOrganizationEntity, Long> id;
	public static volatile SingularAttribute<TestOrganizationEntity, String> name;
	public static volatile SingularAttribute<TestOrganizationEntity, String> phone;
	public static volatile SingularAttribute<TestOrganizationEntity, String> state;
	public static volatile SingularAttribute<TestOrganizationEntity, String> zip;

	private static final QueryInfoJPATestUtils jpaTestUtils = new QueryInfoJPATestUtils();

	static {
		address1 = mockSingularAttribute("address1");
		address2 = mockSingularAttribute("address2");
		city = mockSingularAttribute("city");
		employeeEntities = mockListAttribute("employeeEntities");
		id = mockSingularAttribute("id");
		name = mockSingularAttribute("name");
		phone = mockSingularAttribute("phone");
		state = mockSingularAttribute("state");
		zip = mockSingularAttribute("zip");
	}

	private static <E> ListAttribute<TestOrganizationEntity, E> mockListAttribute(String name) {
		return jpaTestUtils.mockListAttribute(name, TestOrganizationEntity.class);
	}

	private static <T> SingularAttribute<TestOrganizationEntity, T> mockSingularAttribute(String name) {
		return jpaTestUtils.mockSingularAttribute(name, TestOrganizationEntity.class);
	}
}
