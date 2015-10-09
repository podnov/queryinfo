package com.evanzeimet.queryinfo.jpa.path;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public interface QueryInfoPathFactory<RootEntity>  {

	Class<?> getEntityClass();

	<T> Expression<T> getPathForField(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String fieldName,
			QueryInfoFieldPurpose purpose) throws QueryInfoException;

}
