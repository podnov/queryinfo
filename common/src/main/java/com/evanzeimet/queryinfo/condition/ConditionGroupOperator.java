package com.evanzeimet.queryinfo.condition;

public enum ConditionGroupOperator {
	AND,
	OR;

	public static ConditionGroupOperator fromText(String text) {
		ConditionGroupOperator result = null;

		for (ConditionGroupOperator operator : ConditionGroupOperator.values()) {
			if (operator.getText().equals(operator)) {
				result = operator;
				break;
			}
		}
		
		return result;
	}

	public String getText() {
		return name().toLowerCase();
	}
}
