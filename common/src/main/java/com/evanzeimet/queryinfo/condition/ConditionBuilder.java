package com.evanzeimet.queryinfo.condition;

import org.apache.commons.lang3.SerializationUtils;

public class ConditionBuilder {

	private Condition builderReferenceInstance = new DefaultCondition();

	public ConditionBuilder() {

	}

	public Condition build() {
		return SerializationUtils.clone(builderReferenceInstance);
	}

	public ConditionBuilder builderReferenceInstance(Condition builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public static ConditionBuilder create() {
		return new ConditionBuilder();
	}

	public ConditionBuilder leftHandSide(String leftHandSide) {
		builderReferenceInstance.setLeftHandSide(leftHandSide);
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

	public ConditionBuilder rightHandSide(String rightHandSide) {
		builderReferenceInstance.setRightHandSide(rightHandSide);
		return this;
	}
}
