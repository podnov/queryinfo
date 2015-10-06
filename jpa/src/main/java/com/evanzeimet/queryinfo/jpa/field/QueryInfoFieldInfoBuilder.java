package com.evanzeimet.queryinfo.jpa.field;

public class QueryInfoFieldInfoBuilder {

	private QueryInfoFieldInfo builderRefrenceInstance = new DefaultQueryInfoFieldInfo();

	public QueryInfoFieldInfoBuilder() {

	}

	public QueryInfoFieldInfo build() {
		QueryInfoFieldInfo result = new DefaultQueryInfoFieldInfo();

		result.setEntityAttributeName(builderRefrenceInstance.getEntityAttributeName());
		result.setFieldName(builderRefrenceInstance.getFieldName());
		result.setIsQueryable(builderRefrenceInstance.getIsQueryable());
		result.setIsResult(builderRefrenceInstance.getIsResult());

		return result;
	}

	public static QueryInfoFieldInfoBuilder create() {
		return new QueryInfoFieldInfoBuilder();
	}

	public QueryInfoFieldInfoBuilder entityAttributeName(String entityAttributeName) {
		builderRefrenceInstance.setEntityAttributeName(entityAttributeName);
		return this;
	}

	public QueryInfoFieldInfoBuilder fieldName(String fieldName) {
		builderRefrenceInstance.setFieldName(fieldName);
		return this;
	}

	public QueryInfoFieldInfoBuilder isQueryable(Boolean isQueryable) {
		builderRefrenceInstance.setIsQueryable(isQueryable);
		return this;
	}

	public QueryInfoFieldInfoBuilder isResult(Boolean isResult) {
		builderRefrenceInstance.setIsResult(isResult);
		return this;
	}

}
