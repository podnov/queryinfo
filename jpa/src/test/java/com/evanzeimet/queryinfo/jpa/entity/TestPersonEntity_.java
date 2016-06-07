package com.evanzeimet.queryinfo.jpa.entity;

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


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@SuppressWarnings("unchecked")
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TestPersonEntity.class)
public abstract class TestPersonEntity_ {

	public static volatile SingularAttribute<TestPersonEntity, String> firstName;
	public static volatile SingularAttribute<TestPersonEntity, String> lastName;
	public static volatile SingularAttribute<TestPersonEntity, Long> id;
	public static volatile SingularAttribute<TestPersonEntity, TestPersonEntity> spouse;

	static {
		firstName = mock(SingularAttribute.class);
		doReturn("firstName").when(firstName).getName();

		lastName = mock(SingularAttribute.class);
		doReturn("lastName").when(lastName).getName();

		id = mock(SingularAttribute.class);
		doReturn("id").when(id).getName();

		spouse = mock(SingularAttribute.class);
		doReturn("spouse").when(spouse).getName();
	}
}
