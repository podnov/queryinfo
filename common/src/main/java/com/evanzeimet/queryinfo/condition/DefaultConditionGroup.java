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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultConditionGroup implements ConditionGroup {

	@JsonDeserialize(contentAs = DefaultConditionGroup.class)
	private List<ConditionGroup> conditionGroups;
	@JsonDeserialize(contentAs = DefaultCondition.class)
	private List<Condition> conditions;
	private String operator = ConditionGroupOperator.AND.getText();

	public DefaultConditionGroup() {

	}

	@Override
	public List<ConditionGroup> getConditionGroups() {
		return conditionGroups;
	}

	@Override
	public void setConditionGroups(List<ConditionGroup> conditionGroups) {
		this.conditionGroups = conditionGroups;
	}

	@Override
	public List<Condition> getConditions() {
		return conditions;
	}

	@Override
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String getOperator() {
		return operator;
	}

	@Override
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
