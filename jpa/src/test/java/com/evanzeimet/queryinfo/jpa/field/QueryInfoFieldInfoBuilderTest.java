package com.evanzeimet.queryinfo.jpa.field;

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
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;

public class QueryInfoFieldInfoBuilderTest {

	private QueryInfoFieldInfoBuilder builder;

	@Before
	public void setUp() {
		builder = QueryInfoFieldInfoBuilder.create();
	}

	@Test
	public void annotation_defaultEntityClass() {
		QueryInfoField givenAnnotation = new QueryInfoField() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return null;
			}

			@Override
			public QueryInfoJoinType joinType() {
				return null;
			}

			@Override
			public boolean isSelectable() {
				return false;
			}

			@Override
			public boolean isPredicateable() {
				return false;
			}

			@Override
			public boolean isOrderable() {
				return false;
			}

			@Override
			public Class<?> entityClass() {
				return QueryInfoField.class;
			}
		};

		QueryInfoFieldInfo actual = builder.annotation(givenAnnotation)
				.build();

		Class<?> actualEntityClass = actual.getEntityClass();

		assertNull(actualEntityClass);
	}

	@Test
	public void annotation_explicitEntityClass() {
		QueryInfoField givenAnnotation = new QueryInfoField() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return null;
			}

			@Override
			public QueryInfoJoinType joinType() {
				return null;
			}

			@Override
			public boolean isSelectable() {
				return false;
			}

			@Override
			public boolean isPredicateable() {
				return false;
			}

			@Override
			public boolean isOrderable() {
				return false;
			}

			@Override
			public Class<?> entityClass() {
				return TestEntity.class;
			}
		};

		QueryInfoFieldInfo actual = builder.annotation(givenAnnotation)
				.build();

		Class<?> actualEntityClass = actual.getEntityClass();
		Class<?> expectedEntityClass = TestEntity.class;

		assertEquals(expectedEntityClass, actualEntityClass);
	}

	private static class TestEntity {

	}

}
