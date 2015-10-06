package com.evanzeimet.queryinfo.jpa.path;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoFromContext;

public interface QueryInfoPathFactory<RootEntity>  {
	
	Class<?> getEntityClass();

	<T> Expression<T> getPathForField(From<?, RootEntity> from,
			QueryInfoFromContext fromContext,
			String fieldName) throws QueryInfoException;

}
