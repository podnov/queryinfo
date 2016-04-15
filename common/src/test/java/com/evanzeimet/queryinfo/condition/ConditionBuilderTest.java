package com.evanzeimet.queryinfo.condition;

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

import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.fasterxml.jackson.databind.JsonNode;

public class ConditionBuilderTest {

	private ConditionBuilder builder;
	private QueryInfoUtils utils;

	@Before
	public void setUp() {
		builder = new ConditionBuilder();

		utils = new QueryInfoUtils();
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

		JsonNode actualRhs = actualCondition.getRightHandSide();

		String actualRawRhs = actualRhs.toString();
		String expectedRawRhs = "\"rhs\"";

		assertEquals(expectedRawRhs, actualRawRhs);
	}

	@Test
	public void rightHandSide_object_notNull() {
		Object rightHandSide = "meh";

		Condition actualCondition = builder.rightHandSide(rightHandSide).build();

		JsonNode actualRightHandSide = actualCondition.getRightHandSide();

		String actualRawRightHandSide = actualRightHandSide.toString();
		String expectedRawRightHandSide = "\"meh\"";

		assertEquals(expectedRawRightHandSide, actualRawRightHandSide);
	}

	@Test
	public void rightHandSide_object_null() {
		Object rightHandSide = null;

		Condition actualCondition = builder.rightHandSide(rightHandSide).build();

		JsonNode actualRightHandSide = actualCondition.getRightHandSide();

		JsonNode actualRawRightHandSide = actualRightHandSide;
		JsonNode expectedRawRightHandSide = null;

		assertEquals(expectedRawRightHandSide, actualRawRightHandSide);
	}


	@Test
	public void rightHandSide_jsonNode_notNull() {
		JsonNode rightHandSide = utils.treeify("abc");

		Condition actualCondition = builder.rightHandSide(rightHandSide).build();

		JsonNode actualRightHandSide = actualCondition.getRightHandSide();

		String actualRawRightHandSide = actualRightHandSide.toString();
		String expectedRawRightHandSide = "\"abc\"";

		assertEquals(expectedRawRightHandSide, actualRawRightHandSide);
	}

	@Test
	public void rightHandSide_jsonNode_null() {
		JsonNode rightHandSide = null;

		Condition actualCondition = builder.rightHandSide(rightHandSide).build();

		JsonNode actualRightHandSide = actualCondition.getRightHandSide();

		JsonNode actualRawRightHandSide = actualRightHandSide;
		JsonNode expectedRawRightHandSide = null;

		assertEquals(expectedRawRightHandSide, actualRawRightHandSide);
	}
}
