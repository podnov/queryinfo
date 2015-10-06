package com.evanzeimet.queryinfo.sort;

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
