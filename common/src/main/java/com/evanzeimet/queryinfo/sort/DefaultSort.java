package com.evanzeimet.queryinfo.sort;

public class DefaultSort implements Sort {

	private static final long serialVersionUID = -3004002349970913148L;

	private String direction;
	private String fieldName;

	public DefaultSort() {

	}

	@Override
	public String getDirection() {
		return direction;
	}

	@Override
	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
