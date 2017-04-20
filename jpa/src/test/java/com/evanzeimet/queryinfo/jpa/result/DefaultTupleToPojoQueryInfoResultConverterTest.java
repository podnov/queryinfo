package com.evanzeimet.queryinfo.jpa.result;

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

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.evanzeimet.queryinfo.jpa.result.DefaultTupleToPojoQueryInfoResultConverter.DefaultBaseInstanceFactory;

public class DefaultTupleToPojoQueryInfoResultConverterTest {

	@Test
	public void DefaultBaseInstanceFactory_create() {
		DefaultBaseInstanceFactory<Person> factory = new DefaultTupleToPojoQueryInfoResultConverter.DefaultBaseInstanceFactory<>(Person.class);

		Person actual = factory.create();

		assertNotNull(actual);
		assertThat(actual, instanceOf(Person.class));
	}

	protected static class Person {

	}
}
