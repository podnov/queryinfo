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

public interface Condition {

	JsonNode getLeftHandSide();

	void setLeftHandSide(JsonNode leftHandSide);

	OperandType getLeftHandSideType();

	void setLeftHandSideType(OperandType leftHandSideType);

	String getLeftHandSideTypeConfig();

	void setLeftHandSideTypeConfig(String leftHandSideTypeConfig);

	String getOperator();

	void setOperator(String operator);

	JsonNode getRightHandSide();

	void setRightHandSide(JsonNode rightHandSide);

	OperandType getRightHandSideType();

	void setRightHandSideType(OperandType rightHandSideType);

	String getRightHandSideTypeConfig();

	void setRightHandSideTypeConfig(String rightHandSideTypeConfig);

}
