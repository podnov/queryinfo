package com.evanzeimet.queryinfo.builder;

/*
 * #%L
 * queryinfo-common
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
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

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionBuilder;
import com.evanzeimet.queryinfo.condition.ConditionOperator;

public class ConditionBuilderTest {

	private ConditionBuilder builder;

	@Before
	public void setUp() {
		builder = new ConditionBuilder();
	}

	@Test
	public void build() {
		String givenLhs = "lhs";
		ConditionOperator givenOperator = ConditionOperator.EQUAL_TO;
		String givenRhs = "rhs";

		Condition actualCondition = builder.leftHandSide(givenLhs)
				.operator(givenOperator)
				.rightHandSide(givenRhs)
				.build();

		String actualLhs = actualCondition.getLeftHandSide();
		assertEquals(givenLhs, actualLhs);

		String expectedOperator = givenOperator.getText();
		String actualOperator = actualCondition.getOperator();
		assertEquals(expectedOperator, actualOperator);

		String actualRhs = actualCondition.getRightHandSide();
		assertEquals(givenRhs, actualRhs);
	}
}
