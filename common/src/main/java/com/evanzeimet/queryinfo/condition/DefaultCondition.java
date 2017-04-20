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

import com.fasterxml.jackson.databind.JsonNode;

public class DefaultCondition implements Condition {

	private JsonNode leftHandSide;
	private OperandType leftHandSideType;
	private String leftHandSideTypeConfig;
	private String operator;
	private JsonNode rightHandSide;
	private OperandType rightHandSideType;
	private String rightHandSideTypeConfig;

	public DefaultCondition() {

	}

	@Override
	public JsonNode getLeftHandSide() {
		return leftHandSide;
	}

	@Override
	public void setLeftHandSide(JsonNode leftHandSide) {
		this.leftHandSide = leftHandSide;
	}

	@Override
	public OperandType getLeftHandSideType() {
		return leftHandSideType;
	}

	@Override
	public void setLeftHandSideType(OperandType leftHandSideType) {
		this.leftHandSideType = leftHandSideType;
	}

	@Override
	public String getLeftHandSideTypeConfig() {
		return leftHandSideTypeConfig;
	}

	@Override
	public void setLeftHandSideTypeConfig(String leftHandSideTypeConfig) {
		this.leftHandSideTypeConfig = leftHandSideTypeConfig;
	}

	@Override
	public String getOperator() {
		return operator;
	}

	@Override
	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public JsonNode getRightHandSide() {
		return rightHandSide;
	}

	@Override
	public void setRightHandSide(JsonNode rightHandSide) {
		this.rightHandSide = rightHandSide;
	}

	@Override
	public OperandType getRightHandSideType() {
		return rightHandSideType;
	}

	@Override
	public void setRightHandSideType(OperandType rightHandSideType) {
		this.rightHandSideType = rightHandSideType;
	}

	@Override
	public String getRightHandSideTypeConfig() {
		return rightHandSideTypeConfig;
	}

	@Override
	public void setRightHandSideTypeConfig(String rightHandSideTypeConfig) {
		this.rightHandSideTypeConfig = rightHandSideTypeConfig;
	}

}
