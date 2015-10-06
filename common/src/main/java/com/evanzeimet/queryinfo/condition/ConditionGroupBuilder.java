package com.evanzeimet.queryinfo.condition;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class ConditionGroupBuilder {

	private ConditionGroup builderReferenceInstance = new DefaultConditionGroup();

	public ConditionGroupBuilder() {

	}

	public ConditionGroup build() {
		return SerializationUtils.clone(builderReferenceInstance);
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
