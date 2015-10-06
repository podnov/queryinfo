package com.evanzeimet.queryinfo.jpa.field;

public class DefaultQueryInfoFieldInfo implements QueryInfoFieldInfo {

	private String entityAttributeName;
	private String fieldName;
	private Boolean isQueryable;
	private Boolean isResult;

	public DefaultQueryInfoFieldInfo() {
	}

	@Override
	public String getEntityAttributeName() {
		return entityAttributeName;
	}

	@Override
	public void setEntityAttributeName(String entityAttributeName) {
		this.entityAttributeName = entityAttributeName;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Boolean getIsQueryable() {
		return isQueryable;
	}

	@Override
	public void setIsQueryable(Boolean isQueryable) {
		this.isQueryable = isQueryable;
	}

	@Override
	public Boolean getIsResult() {
		return isResult;
	}

	@Override
	public void setIsResult(Boolean isResult) {
		this.isResult = isResult;
	}
}
