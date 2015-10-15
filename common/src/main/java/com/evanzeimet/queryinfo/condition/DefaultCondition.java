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

import com.evanzeimet.queryinfo.util.JSONToStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultCondition implements Condition {

	private static final long serialVersionUID = 3264149014277724710L;

	private String leftHandSide;
	private String operator;
	@JsonDeserialize(using = JSONToStringDeserializer.class)
	private String rightHandSide;

	public DefaultCondition() {

	}

	@Override
	public String getLeftHandSide() {
		return leftHandSide;
	}

	@Override
	public void setLeftHandSide(String leftHandSide) {
		this.leftHandSide = leftHandSide;
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
	public String getRightHandSide() {
		return rightHandSide;
	}

	@Override
	public void setRightHandSide(String rightHandSide) {
		this.rightHandSide = rightHandSide;
	}
}
