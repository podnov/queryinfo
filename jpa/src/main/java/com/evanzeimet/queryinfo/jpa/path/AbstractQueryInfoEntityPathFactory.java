package com.evanzeimet.queryinfo.jpa.path;

import javax.inject.Inject;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPathParts;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoFromContext;

public abstract class AbstractQueryInfoEntityPathFactory<RootEntity> implements QueryInfoPathFactory<RootEntity> {

	@Inject
	protected QueryInfoPathFactoryRegistry pathFactoryRegistry;

	protected <T> Expression<T> getEntityPath(From<?, RootEntity> from,
			String fieldName) throws QueryInfoException {
		Expression<T> result = null;

		try {
			result = from.get(fieldName);
		} catch (IllegalArgumentException e) {
			String fromName = from.getModel().getBindableJavaType().getName();
			String message = String.format("Could not find field [%s] in [%s]",
					fieldName,
					fromName);
			throw new QueryInfoException(message);
		}

		return result;
	}

	protected <T> Expression<T> getJoinPath(From<?, RootEntity> from,
			QueryInfoFromContext fromContext,
			QueryInfoFieldPathParts pathParts) throws QueryInfoException {
		String joinAttributeName = pathParts.consumeJoin();
		Join<?, Object> join = fromContext.getJoin(from, joinAttributeName);

		Class<Object> joinClass = join.getModel().getBindableJavaType();
		QueryInfoPathFactory<Object> joinPathFactory = pathFactoryRegistry.getFactory(joinClass);

		String joinFieldName = pathParts.toString();

		return joinPathFactory.getPathForField(join, fromContext, joinFieldName);
	}

	@Override
	public <T> Expression<T> getPathForField(From<?, RootEntity> from,
			QueryInfoFromContext froms,
			String fieldName) throws QueryInfoException {
		Expression<T> result = null;

		QueryInfoFieldPathParts pathParts = QueryInfoFieldPathParts.fromFullPath(fieldName);

		if (pathParts.hasJoins()) {
			result = getJoinPath(from, froms, pathParts);
		} else {
			result = getEntityPath(from, fieldName);
		}

		return result;
	}
}
