package com.evanzeimet.queryinfo.jpa.path;

import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public interface QueryInfoPathFactory<RootEntity>  {

	Class<?> getEntityClass();

	Map<String, QueryInfoFieldInfo> getFieldInfos();

	void setFieldInfos(Map<String, QueryInfoFieldInfo> fieldInfos);

	<T> Expression<T> getPathForField(From<?, RootEntity> from,
			QueryInfoJPAContext<?> jpaContext,
			String fieldName,
			QueryInfoFieldPurpose purpose) throws QueryInfoException;

}
