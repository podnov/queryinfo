package com.evanzeimet.queryinfo;

public enum ConditionGroupOperator {
    AND,
    OR;

    public String getText() {
        return name().toLowerCase();
    }
}
