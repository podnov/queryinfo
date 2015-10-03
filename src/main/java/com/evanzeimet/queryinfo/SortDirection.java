package com.evanzeimet.queryinfo;

public enum SortDirection {
    ASC,
    DESC;

    public String getText() {
        return name().toLowerCase();
    }
}
