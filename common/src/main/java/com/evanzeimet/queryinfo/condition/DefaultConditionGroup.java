package com.evanzeimet.queryinfo.condition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DefaultConditionGroup implements ConditionGroup {

	private static final long serialVersionUID = -4685739021151165664L;

	@JsonDeserialize(contentAs = DefaultConditionGroup.class)
	private List<ConditionGroup> conditionGroups = new ArrayList<ConditionGroup>();
	@JsonDeserialize(contentAs = DefaultCondition.class)
	private List<Condition> conditions = new ArrayList<Condition>();
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
