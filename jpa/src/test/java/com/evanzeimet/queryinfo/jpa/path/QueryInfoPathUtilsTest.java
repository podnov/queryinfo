package com.evanzeimet.queryinfo.jpa.path;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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
import static org.mockito.Mockito.mock;

import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Bindable;

import org.junit.Before;
import org.junit.Test;

public class QueryInfoPathUtilsTest {

	private QueryInfoPathUtils utils;

	@Before
	public void setUp() {
		utils = new QueryInfoPathUtils();
	}

	@Test
	public void getBindableJavaType_model_notNull() {
		Class<QueryInfoPathUtilsTest> givenBindableJavaType = QueryInfoPathUtilsTest.class;

		Bindable<?> givenModel = mock(Bindable.class);
		doReturn(givenBindableJavaType).when(givenModel).getBindableJavaType();

		Path<?> givenPath = mock(Path.class);

		doReturn(null).when(givenPath).getJavaType();
		doReturn(givenModel).when(givenPath).getModel();

		Class<?> actual = utils.getBindableJavaType(givenPath);
		Class<?> expected = givenBindableJavaType;

		assertEquals(expected, actual);
	}

	@Test
	public void getBindableJavaType_model_null() {
		Class<QueryInfoPathUtilsTest> givenJavaType = QueryInfoPathUtilsTest.class;

		Path<?> givenPath = mock(Path.class);

		doReturn(givenJavaType).when(givenPath).getJavaType();
		doReturn(null).when(givenPath).getModel();

		Class<?> actual = utils.getBindableJavaType(givenPath);
		Class<?> expected = givenJavaType;

		assertEquals(expected, actual);
	}

}
