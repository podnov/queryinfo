package com.evanzeimet.queryinfo;

public enum ConditionOperator {

    EQUAL_TO("=", "equals"),
    NOT_EQUAL_TO("<>", "not equals"),
    GREATER_THAN(">", "is greater than"),
    GREATER_THAN_OR_EQUAL_TO(">=", "is greater than or equal to"),
    LESS_THAN("<", "is less than"),
    LESS_THAN_OR_EQUAL_TO("<=", "is less than or equal to"),
    NULL("is null", "is null"),
    NOT_NULL("is not null", "is not null"),
    IN("is in", "is in"),
    NOT_IN("is not in", "is not in");

    private final String description;
    private final String text;

    ConditionOperator(String text, String description) {
        this.text = text;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }
}
