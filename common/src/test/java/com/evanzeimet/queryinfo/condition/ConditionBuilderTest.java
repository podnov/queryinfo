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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoTestUtils;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.fasterxml.jackson.databind.JsonNode;

import uk.co.jemos.podam.api.PodamFactoryImpl;

public class ConditionBuilderTest {

	private ConditionBuilder builder;
	private PodamFactoryImpl podam;
	private QueryInfoUtils utils;

	@Before
	public void setUp() {
		builder = new ConditionBuilder();
		podam = new PodamFactoryImpl();
		utils = new QueryInfoUtils();
	}

	@Test
	public void build_allFields() throws IOException, QueryInfoException {
		DefaultCondition givenCondition = podam.manufacturePojo(DefaultCondition.class);

		Condition actualCondition = builder.builderReferenceInstance(givenCondition)
				.build();

		String actualJson = QueryInfoTestUtils.createActualJson(actualCondition);
		String expectedJson = QueryInfoTestUtils.createActualJson(givenCondition);

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void build_fieldValues() throws IOException, QueryInfoException {
		String givenLhs = "lhs";
		OperandType givenLhsType = OperandType.LITERAL;
		String givenLhsTypeConfig = "lhsTypeConfig";
		ConditionOperator givenOperator = ConditionOperator.EQUAL_TO;
		String givenRhs = "rhs";
		OperandType givenRhsType = OperandType.ATTRIBUTE_PATH;
		String rightHandSideTypeConfig = "rhsTypeConfig";

		Condition actualCondition = builder.leftHandSide(givenLhs)
				.leftHandSideType(givenLhsType)
				.leftHandSideTypeConfig(givenLhsTypeConfig)
				.operator(givenOperator)
				.rightHandSide(givenRhs)
				.rightHandSideType(givenRhsType)
				.rightHandSideTypeConfig(rightHandSideTypeConfig)
				.build();

		String actualJson = QueryInfoTestUtils.createActualJson(actualCondition);
		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"ConditionBuilderTest_build_expected.json");

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void leftHandSide_object_notNull() {
		Object givenLhs = "meh";

		Condition actualCondition = builder.leftHandSide(givenLhs).build();

		JsonNode actualLhs = actualCondition.getLeftHandSide();

		String actualRawLhs = actualLhs.asText();
		String expectedRawLhs = givenLhs.toString();
		assertEquals(expectedRawLhs, actualRawLhs);
	}

	@Test
	public void leftHandSide_object_null() {
		Object givenLhs = null;

		Condition actualCondition = builder.leftHandSide(givenLhs).build();

		JsonNode actualLhs = actualCondition.getLeftHandSide();

		JsonNode actualRawLhs = actualLhs;
		JsonNode expectedRawLhs = null;
		assertEquals(expectedRawLhs, actualRawLhs);
	}

	@Test
	public void leftHandSide_jsonNode_notNull() {
		String givenRawLhs = "abc";
		JsonNode givenLhs = utils.treeify(givenRawLhs);

		Condition actualCondition = builder.leftHandSide(givenLhs).build();

		JsonNode actualLhs = actualCondition.getLeftHandSide();

		String actualRawLhs = actualLhs.asText();
		String expectedRawLhs = givenRawLhs;
		assertEquals(expectedRawLhs, actualRawLhs);
	}

	@Test
	public void leftHandSide_jsonNode_null() {
		JsonNode givenLhs = null;

		Condition actualCondition = builder.leftHandSide(givenLhs).build();

		JsonNode actualLhs = actualCondition.getLeftHandSide();

		JsonNode actualRawLhs = actualLhs;
		JsonNode expectedRawLhs = null;
		assertEquals(expectedRawLhs, actualRawLhs);
	}

	@Test
	public void rightHandSide_object_notNull() {
		Object givenRhs = "meh";

		Condition actualCondition = builder.rightHandSide(givenRhs).build();

		JsonNode actualRhs = actualCondition.getRightHandSide();

		String actualRawRhs = actualRhs.asText();
		String expectedRawRhs = givenRhs.toString();
		assertEquals(expectedRawRhs, actualRawRhs);
	}

	@Test
	public void rightHandSide_object_null() {
		Object givenRhs = null;

		Condition actualCondition = builder.rightHandSide(givenRhs).build();

		JsonNode actualRhs = actualCondition.getRightHandSide();

		JsonNode actualRawRhs = actualRhs;
		JsonNode expectedRawRhs = null;
		assertEquals(expectedRawRhs, actualRawRhs);
	}

	@Test
	public void rightHandSide_jsonNode_notNull() {
		String givenRawRhs = "abc";
		JsonNode givenRhs = utils.treeify(givenRawRhs);

		Condition actualCondition = builder.rightHandSide(givenRhs).build();

		JsonNode actualRhs = actualCondition.getRightHandSide();

		String actualRawRhs = actualRhs.asText();
		String expectedRawRhs = givenRawRhs;
		assertEquals(expectedRawRhs, actualRawRhs);
	}

	@Test
	public void rightHandSide_jsonNode_null() {
		JsonNode givenRhs = null;

		Condition actualCondition = builder.rightHandSide(givenRhs).build();

		JsonNode actualRhs = actualCondition.getRightHandSide();

		JsonNode actualRawRhs = actualRhs;
		JsonNode expectedRawRhs = null;
		assertEquals(expectedRawRhs, actualRawRhs);
	}
}
