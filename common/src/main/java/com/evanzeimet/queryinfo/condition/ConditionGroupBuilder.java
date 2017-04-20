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

import java.util.ArrayList;
import java.util.List;

public class ConditionGroupBuilder {

	private ConditionGroup builderReferenceInstance = createDefaultInstance();

	public ConditionGroupBuilder() {

	}

	public ConditionGroup build() {
		DefaultConditionGroup result = createDefaultInstance();

		result.setConditionGroups(builderReferenceInstance.getConditionGroups());
		result.setConditions(builderReferenceInstance.getConditions());
		result.setDirective(builderReferenceInstance.getDirective());
		result.setDirectiveConfig(builderReferenceInstance.getDirectiveConfig());
		result.setOperator(builderReferenceInstance.getOperator());

		return result;
	}

	public ConditionGroupBuilder builderReferenceInstance(ConditionGroup builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public ConditionGroupBuilder conditionGroups(List<ConditionGroup> conditionGroups) {
		builderReferenceInstance.setConditionGroups(conditionGroups);
		return this;
	}

	public ConditionGroupBuilder conditions(List<Condition> conditions) {
		builderReferenceInstance.setConditions(conditions);
		return this;
	}

	public static ConditionGroupBuilder create() {
		return new ConditionGroupBuilder();
	}

	protected DefaultConditionGroup createDefaultInstance() {
		return new DefaultConditionGroup();
	}

	public static ConditionGroupBuilder createForCondition(Condition condition) {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(condition);

		return create()
				.conditions(conditions);
	}

	public static ConditionGroupBuilder createForConditionGroup(ConditionGroup conditionGroup) {
		List<ConditionGroup> conditionGroups = new ArrayList<ConditionGroup>();
		conditionGroups.add(conditionGroup);

		return create()
				.conditionGroups(conditionGroups);
	}

	public ConditionGroupBuilder directive(String directive) {
		builderReferenceInstance.setDirective(directive);
		return this;
	}

	public ConditionGroupBuilder directiveConfig(String directiveConfig) {
		builderReferenceInstance.setDirectiveConfig(directiveConfig);
		return this;
	}

	public ConditionGroupBuilder operator(ConditionGroupOperator operator) {
		String rawOperator;

		if (operator == null) {
			rawOperator = null;
		} else {
			rawOperator = operator.getText();
		}

		return operator(rawOperator);
	}

	public ConditionGroupBuilder operator(String operator) {
		builderReferenceInstance.setOperator(operator);
		return this;
	}
}
