package com.evanzeimet.queryinfo.builder;

import com.evanzeimet.queryinfo.Condition;
import com.evanzeimet.queryinfo.DefaultSort;
import com.evanzeimet.queryinfo.Sort;
import com.evanzeimet.queryinfo.SortDirection;
import org.apache.commons.lang3.SerializationUtils;

public class SortBuilder {

    private Sort builderReferenceInstance = new DefaultSort();

    public SortBuilder() {

    }

    public Sort build() {
        return SerializationUtils.clone(builderReferenceInstance);
    }

    public SortBuilder builderReferenceInstance(Sort builderReferenceInstance) {
        this.builderReferenceInstance = builderReferenceInstance;
        return this;
    }

    public static SortBuilder create() {
        return new SortBuilder();
    }

    public SortBuilder direction(SortDirection direction) {
        String rawDirection;

        if (direction == null) {
            rawDirection = null;
        } else {
            rawDirection = direction.getText();
        }

        return direction(rawDirection);
    }

    public SortBuilder direction(String direction) {
        builderReferenceInstance.setDirection(direction);
        return this;
    }

    public SortBuilder fieldName(String fieldName) {
        builderReferenceInstance.setFieldName(fieldName);
        return this;
    }
}
