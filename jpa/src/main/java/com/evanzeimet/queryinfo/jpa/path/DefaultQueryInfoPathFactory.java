package com.evanzeimet.queryinfo.jpa.path;

import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPathParts;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultQueryInfoPathFactory<RootEntity>
		implements QueryInfoPathFactory<RootEntity> {

	protected QueryInfoBeanContextRegistry beanContextRegistry;
	protected Class<RootEntity> entityClass;

	public DefaultQueryInfoPathFactory(QueryInfoBeanContextRegistry beanContextRegistry,
			Class<RootEntity> entityClass) {
		this.beanContextRegistry = beanContextRegistry;
		// TODO producer inspect annotation for constuctor argument?
		this.entityClass = entityClass;
	}

	@Override
	public Class<RootEntity> getEntityClass() {
		return entityClass;
	}

	protected <T> Expression<T> getEntityPath(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String fieldName,
			QueryInfoFieldPurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		validateFieldInfo(jpaContext, fieldName, purpose);

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

	protected <T, JoinedEntity> Expression<T> getJoinPath(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			QueryInfoFieldPathParts pathParts,
			QueryInfoFieldPurpose purpose) throws QueryInfoException {
		String joinAttributeName = pathParts.consumeJoin();
		Join<RootEntity, JoinedEntity> join = jpaContext.getJoin(from, joinAttributeName);

		Class<JoinedEntity> joinedClass = join.getModel().getBindableJavaType();

		QueryInfoBeanContext<JoinedEntity, ?, ?> joinBeanContext = beanContextRegistry.getContext(joinedClass);
		QueryInfoPathFactory<JoinedEntity> pathFactory = joinBeanContext.getPathFactory();

		String joinFieldName = pathParts.toString();

		return pathFactory.getPathForField(jpaContext,
				join,
				joinFieldName,
				purpose);
	}

	@Override
	public <T> Expression<T> getPathForField(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String fieldName,
			QueryInfoFieldPurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		QueryInfoFieldPathParts pathParts = QueryInfoFieldPathParts.fromFullPath(fieldName);

		if (pathParts.hasJoins()) {
			// TODO need to validate that joins are supposed to be walked w/ their own annotation
			result = getJoinPath(jpaContext, from, pathParts, purpose);
		} else {
			result = getEntityPath(jpaContext, from, fieldName, purpose);
		}

		return result;
	}

	protected void validateFieldInfo(QueryInfoJPAContext<?> jpaContext,
			String fieldName,
			QueryInfoFieldPurpose purpose)
			throws QueryInfoException {
		QueryInfoBeanContext<?, ?, ?> beanContext = beanContextRegistry.getContextForRoot(jpaContext);
		Map<String /* fieldName */, QueryInfoFieldInfo> fieldInfos = beanContext.getFieldInfos();

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
