package com.evanzeimet.queryinfo.jpa.predicate.directive;

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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoTestUtils;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.DefaultConditionGroup;

public class ExistsDirectivePredicateFactoryTest {

	private ExistsDirectiveConditionGroupPredicateFactory<Object> factory;

	@Before
	public void setUp() {
		factory = new ExistsDirectiveConditionGroupPredicateFactory<>();
	}

	@Test
	public void createConditionGroupWithoutDirective() throws IOException, QueryInfoException {
		String givenJsonResourcePath = "ExistsDirectivePredicateFactoryTest_createConditionGroupWithoutDirective_given.json";

		String givenJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				givenJsonResourcePath);
		DefaultConditionGroup givenConditionGroup = QueryInfoTestUtils.objectify(givenJson,
				DefaultConditionGroup.class);

		ConditionGroup actual = factory.createConditionGroupWithoutDirective(givenConditionGroup);

		String actualJson = QueryInfoTestUtils.stringify(actual);
		actualJson = QueryInfoTestUtils.dosToUnix(actualJson);

		String expactedJsonResourcePath = "ExistsDirectivePredicateFactoryTest_createConditionGroupWithoutDirective_expected.json";
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				expactedJsonResourcePath);

		assertEquals(expectedJson, actualJson);
	}

}
