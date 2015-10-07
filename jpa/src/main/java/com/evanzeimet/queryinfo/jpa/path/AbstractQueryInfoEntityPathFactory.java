package com.evanzeimet.queryinfo.jpa.path;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPathParts;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoJPAContext;

public abstract class AbstractQueryInfoEntityPathFactory<RootEntity> implements QueryInfoPathFactory<RootEntity> {

	private Class<RootEntity> entityClass;
	private Map<String /* fieldName */, QueryInfoFieldInfo> fieldInfos = new HashMap<>();

	@Inject
	private QueryInfoPathFactoryRegistry pathFactoryRegistry;

	public AbstractQueryInfoEntityPathFactory(Class<RootEntity> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public Class<RootEntity> getEntityClass() {
		return entityClass;
	}

	@Override
	public Map<String, QueryInfoFieldInfo> getFieldInfos() {
		return fieldInfos;
	}

	@Override
	public void setFieldInfos(Map<String, QueryInfoFieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}

	protected <T> Expression<T> getEntityPath(From<?, RootEntity> from,
			String fieldName,
			QueryInfoFieldPurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		validateFieldInfo(fieldName, purpose);

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
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfoFieldPathParts pathParts,
			QueryInfoFieldPurpose purpose) throws QueryInfoException {
		String joinAttributeName = pathParts.consumeJoin();
		Join<?, RootEntity> join = jpaContext.getJoin(from, joinAttributeName);

		Class<RootEntity> joinClass = join.getModel().getBindableJavaType();
		QueryInfoPathFactory<RootEntity> joinPathFactory = pathFactoryRegistry.getFactory(joinClass);

		String joinFieldName = pathParts.toString();

		return joinPathFactory.getPathForField(join,
				jpaContext,
				joinFieldName,
				purpose);
	}

	@Override
	public <T> Expression<T> getPathForField(From<?, RootEntity> from,
			QueryInfoJPAContext<RootEntity> jpaContext,
			String fieldName,
			QueryInfoFieldPurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		QueryInfoFieldPathParts pathParts = QueryInfoFieldPathParts.fromFullPath(fieldName);

		if (pathParts.hasJoins()) {
			result = getJoinPath(from, jpaContext, pathParts, purpose);
		} else {
			result = getEntityPath(from, fieldName, purpose);
		}

		return result;
	}

	protected void validateFieldInfo(String fieldName, QueryInfoFieldPurpose purpose)
			throws QueryInfoException {
		QueryInfoFieldInfo fieldInfo = fieldInfos.get(fieldName);

		if (fieldInfo == null) {
			String message = String.format("Field not defined for name [%s]", fieldName);
			throw new QueryInfoException(message);
		}

		boolean valid = false;

		switch (purpose) {
			case ORDER:
				valid = fieldInfo.getIsSortable();
				break;

			case PREDICATE:
				valid = fieldInfo.getIsQueryable();
				break;

			case SELECT:
				valid = fieldInfo.getIsSelectable();
				break;
		}

		if (!valid) {
			String message = String.format("Field [%s] not valid for [%s]", fieldName, purpose);
			throw new QueryInfoException(message);
		}
	}
}
