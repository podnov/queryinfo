package com.evanzeimet.queryinfo.jpa.selection;

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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.jpa.entity.AbstractQueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;

public class QueryInfoRequestedFieldsBuilderTest {

	private QueryInfoRequestedFieldsBuilder builder;

	@Before
	public void setUp() {
		builder = new QueryInfoRequestedFieldsBuilder();
	}

	@Test
	public void addSelectableFields() {
		TestEntityContext entityContext = new TestEntityContext();

		List<String> actual = builder.addSelectableFields(entityContext).build();

		assertThat(actual, containsInAnyOrder("firstName", "lastName", "meh"));
	}

	private static class TestEntity {

		@QueryInfoField
		public String getFirstName() {
			return null;
		}

		@QueryInfoField
		public String getLastName() {
			return null;
		}

		@QueryInfoField(name = "meh")
		public String getMehComplicatedName() {
			return null;
		}

		@QueryInfoField(isSelectable = false)
		public String getSocialSecurityNumber() {
			return null;
		}
	}

	private static class TestEntityContext
			extends AbstractQueryInfoEntityContext<TestEntity> {

		@Override
		public Class<TestEntity> getEntityClass() {
			return TestEntity.class;
		}

	}
}
