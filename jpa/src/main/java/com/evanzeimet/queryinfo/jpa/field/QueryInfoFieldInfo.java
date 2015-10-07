package com.evanzeimet.queryinfo.jpa.field;

public interface QueryInfoFieldInfo {

	String getEntityAttributeName();

	void setEntityAttributeName(String entityAttributeName);

	String getFieldName();

	void setFieldName(String fieldName);

	Boolean getIsQueryable();

	void setIsQueryable(Boolean isQueryable);

	Boolean getIsSelectable();

	void setIsSelectable(Boolean isSelectable);

	Boolean getIsSortable();

	void setIsSortable(Boolean isSortable);

}
