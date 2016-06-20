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
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.evanzeimet.queryinfo.jpa.test.utils.QueryInfoJPATestUtils;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TestAbstractMappedSuperclass.class)
public abstract class TestAbstractMappedSuperclass_ {

	public static volatile SingularAttribute<TestAbstractMappedSuperclass, String> mappedSuperclassField;

	private static final QueryInfoJPATestUtils jpaTestUtils = new QueryInfoJPATestUtils();

	static {
		mappedSuperclassField = mockSingularAttribute("mappedSuperclassField");
	}

	private static <X, T> SingularAttribute<TestAbstractMappedSuperclass, T> mockSingularAttribute(String name) {
		return jpaTestUtils.mockSingularAttribute(name, TestAbstractMappedSuperclass.class);
	}
}
