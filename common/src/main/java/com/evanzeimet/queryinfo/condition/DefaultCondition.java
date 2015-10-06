package com.evanzeimet.queryinfo.condition;

public class DefaultCondition implements Condition {

	private static final long serialVersionUID = 3264149014277724710L;

	private String leftHandSide;
	private String operator;
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
