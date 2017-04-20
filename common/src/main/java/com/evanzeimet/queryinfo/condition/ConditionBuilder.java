package com.evanzeimet.queryinfo.condition;

/*
 * #%L
 * queryinfo-common
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

import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.fasterxml.jackson.databind.JsonNode;

public class ConditionBuilder {

	private static final QueryInfoUtils utils = new QueryInfoUtils();

	private Condition builderReferenceInstance = createDefaultInstance();

	public ConditionBuilder() {

	}

	public Condition build() {
		Condition result = createDefaultInstance();

		result.setLeftHandSide(builderReferenceInstance.getLeftHandSide());
		result.setLeftHandSideType(builderReferenceInstance.getLeftHandSideType());
		result.setLeftHandSideTypeConfig(builderReferenceInstance.getLeftHandSideTypeConfig());
		result.setOperator(builderReferenceInstance.getOperator());
		result.setRightHandSide(builderReferenceInstance.getRightHandSide());
		result.setRightHandSideType(builderReferenceInstance.getRightHandSideType());
		result.setRightHandSideTypeConfig(builderReferenceInstance.getRightHandSideTypeConfig());

		return result;
	}

	public ConditionBuilder builderReferenceInstance(Condition builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	protected JsonNode convertObjectToJsonNode(Object rightHandSide) {
		boolean needsConversion = (rightHandSide != null);
		needsConversion = (needsConversion && !(rightHandSide instanceof JsonNode));

		JsonNode jsonNode;

		if (needsConversion) {
			jsonNode = utils.treeify(rightHandSide);
		} else {
			jsonNode = ((JsonNode) rightHandSide);
		}

		return jsonNode;
	}

	public static ConditionBuilder create() {
		return new ConditionBuilder();
	}

	protected DefaultCondition createDefaultInstance() {
		return new DefaultCondition();
	}

	public ConditionBuilder leftHandSide(Object leftHandSide) {
		JsonNode jsonNode = convertObjectToJsonNode(leftHandSide);
		return leftHandSide(jsonNode);
	}

	public ConditionBuilder leftHandSide(JsonNode leftHandSide) {
		builderReferenceInstance.setLeftHandSide(leftHandSide);
		return this;
	}

	public ConditionBuilder leftHandSideType(OperandType leftHandSideType) {
		builderReferenceInstance.setLeftHandSideType(leftHandSideType);
		return this;
	}

	public ConditionBuilder leftHandSideTypeConfig(String leftHandSideTypeConfig) {
		builderReferenceInstance.setLeftHandSideTypeConfig(leftHandSideTypeConfig);
		return this;
	}

	public ConditionBuilder operator(ConditionOperator operator) {
		String rawOperator = operator.getText();
		return operator(rawOperator);
	}

	public ConditionBuilder operator(String operator) {
		builderReferenceInstance.setOperator(operator);
		return this;
	}

	public ConditionBuilder rightHandSide(Object rightHandSide) {
		JsonNode jsonNode = convertObjectToJsonNode(rightHandSide);
		return rightHandSide(jsonNode);
	}

	public ConditionBuilder rightHandSide(JsonNode rightHandSide) {
		builderReferenceInstance.setRightHandSide(rightHandSide);
		return this;
	}

	public ConditionBuilder rightHandSideType(OperandType rightHandSideType) {
		builderReferenceInstance.setRightHandSideType(rightHandSideType);
		return this;
	}

	public ConditionBuilder rightHandSideTypeConfig(String rightHandSideTypeConfig) {
		builderReferenceInstance.setRightHandSideTypeConfig(rightHandSideTypeConfig);
		return this;
	}

}
