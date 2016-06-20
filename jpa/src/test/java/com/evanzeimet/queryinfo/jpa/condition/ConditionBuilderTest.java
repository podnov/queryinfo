package com.evanzeimet.queryinfo.jpa.condition;

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
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.jpa.test.entity.TestEmployeeEntity;
import com.evanzeimet.queryinfo.jpa.test.entity.TestEmployeeEntity_;
import com.evanzeimet.queryinfo.jpa.test.entity.TestQueryInfoEntityContextRegistry;

public class ConditionBuilderTest {

	private TestQueryInfoEntityContextRegistry entityContextRegistry;

	@Before
	public void setUp() {
		entityContextRegistry = TestQueryInfoEntityContextRegistry.create();
	}

	@Test
	public void leftHandSide_superClassAttribute_hostNotSpecified() {
		QueryInfoRuntimeException actualRuntimeException = null;

		try {
			ConditionBuilder.create(entityContextRegistry)
					.leftHandSide(TestEmployeeEntity_.mappedSuperclassField)
					.build();
		} catch (QueryInfoRuntimeException e) {
			actualRuntimeException = e;
		}

		assertNotNull(actualRuntimeException);

		String actualExceptionMessage = actualRuntimeException.getMessage();
		String expectedExceptionMessage = "Could not find entity context for [com.evanzeimet.queryinfo.jpa.test.entity.TestAbstractMappedSuperclass]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void leftHandSideHost() {
		Condition actualCondition = ConditionBuilder.create(entityContextRegistry)
				.leftHandSideHost(TestEmployeeEntity.class)
				.leftHandSide(TestEmployeeEntity_.mappedSuperclassField)
				.build();

		assertNotNull(actualCondition);

		String actualLeftHandSide = actualCondition.getLeftHandSide();
		String expectedLeftHandSide = "mappedSuperclassFieldRenamed";

		assertEquals(expectedLeftHandSide, actualLeftHandSide);
	}
}
