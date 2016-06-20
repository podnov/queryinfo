package com.evanzeimet.queryinfo.jpa.test.utils;

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

import java.util.List;

import javax.persistence.criteria.From;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

public class QueryInfoJPATestUtils {

	public <T extends From<?, ?>> T mockFrom(Class<T> fromClass,
			Class<?> entityClass) {
		T result = mock(fromClass);

		Bindable<?> model = mock(Bindable.class);
		doReturn(model).when(result).getModel();

		doReturn(entityClass).when(model).getBindableJavaType();

		return result;
	}

	@SuppressWarnings("unchecked")
	public <X, E> ListAttribute<X, E> mockListAttribute(String name,
			Class<X> hostType) {
		ListAttribute<X, E> result = mock(ListAttribute.class);
		doReturn(name).when(result).getName();

		ManagedType<X> managedType = mockManagedType(hostType);
		doReturn(managedType).when(result).getDeclaringType();

		return result;
	}

	public <X, E> ListAttribute<X, E> mockListAttribute(String name,
			Class<X> hostType,
			Class<E> joinedType) {
		ListAttribute<X, E> result = mockListAttribute(name, hostType);

		doReturn(List.class).when(result).getJavaType();
		doReturn(joinedType).when(result).getBindableJavaType();

		return result;
	}

	@SuppressWarnings("unchecked")
	public <X> ManagedType<X> mockManagedType(Class<X> hostType) {
		ManagedType<X> result = mock(ManagedType.class);
		doReturn(hostType).when(result).getJavaType();
		return result;
	}

	@SuppressWarnings("unchecked")
	public <X, T> SingularAttribute<X, T> mockSingularAttribute(String name, Class<X> hostType) {
		SingularAttribute<X, T> result = mock(SingularAttribute.class);
		doReturn(name).when(result).getName();

		ManagedType<X> managedType = mockManagedType(hostType);
		doReturn(managedType).when(result).getDeclaringType();

		return result;
	}

	public <X, T> SingularAttribute<X, T> mockSingularAttribute(String name, Class<X> hostType,
			Class<T> joinedType) {
		SingularAttribute<X, T> result = mockSingularAttribute(name, hostType);

		doReturn(joinedType).when(result).getJavaType();

		return result;
	}

}
