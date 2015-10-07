package com.evanzeimet.queryinfo.jpa.beancontext;

import com.evanzeimet.queryinfo.jpa.field.EntityAnnotationsResolver;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfoResolver;

public class DefaultQueryInfoBeanContext {

	private QueryInfoFieldInfoResolver<?> fieldInfoResolver;

	public DefaultQueryInfoBeanContext() {
		// TODO
		setFieldInfoResolver(new EntityAnnotationsResolver<Object>(Object.class));
		// TODO setFieldInfoResolver(new FieldEnumResolver(FieldEnum.class));
	}


	public QueryInfoFieldInfoResolver<?> getFieldInfoResolver() {
		return fieldInfoResolver;
	}

	protected void setFieldInfoResolver(QueryInfoFieldInfoResolver<?> fieldInfoResolver) {
		this.fieldInfoResolver = fieldInfoResolver;
	}
}
