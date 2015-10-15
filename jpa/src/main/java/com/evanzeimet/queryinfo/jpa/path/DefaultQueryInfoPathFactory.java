package com.evanzeimet.queryinfo.jpa.path;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPathParts;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
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
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		validateFieldInfo(from, fieldName, purpose);

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

	protected <JoinedEntity> Join<RootEntity, JoinedEntity> getJoin(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String queryInfoJoinAttributeName) {
		QueryInfoBeanContext<?, ?, ?> fromBeanContext = beanContextRegistry.getContext(from);

		QueryInfoAttributeContext queryInfoAttributeContext = fromBeanContext.getQueryInfoAttributeContext();
		QueryInfoJoinInfo querInfoJoinInfo = queryInfoAttributeContext.getJoin(queryInfoJoinAttributeName);

		return jpaContext.getJoin(from, querInfoJoinInfo);
	}

	protected <T, JoinedEntity> Expression<T> getJoinPath(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			QueryInfoFieldPathParts pathParts,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		String queryInfoJoinAttributeName = pathParts.consumeJoin();

		Join<RootEntity, JoinedEntity> join = getJoin(jpaContext, from, queryInfoJoinAttributeName);
		QueryInfoPathFactory<JoinedEntity> joinPathFactory = getJoinPathFactory(join);

		String joinFieldName = pathParts.toString();

		return joinPathFactory.getPathForField(jpaContext,
				join,
				joinFieldName,
				purpose);
	}

	protected <JoinedEntity> QueryInfoPathFactory<JoinedEntity> getJoinPathFactory(Join<RootEntity, JoinedEntity> join) {
		Class<JoinedEntity> joinedClass = join.getModel().getBindableJavaType();
		QueryInfoBeanContext<JoinedEntity, ?, ?> joinBeanContext = beanContextRegistry.getContext(joinedClass);

		return joinBeanContext.getPathFactory();
	}

	@Override
	public <T> Expression<T> getPathForField(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String fieldName,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		QueryInfoFieldPathParts pathParts = QueryInfoFieldPathParts.fromFullPath(fieldName);

		if (pathParts.hasJoins()) {
			result = getJoinPath(jpaContext, from, pathParts, purpose);
		} else {
			result = getEntityPath(jpaContext, from, fieldName, purpose);
		}

		return result;
	}

	protected void validateFieldInfo(From<?, RootEntity> from,
			String fieldName,
			QueryInfoAttributePurpose purpose)
			throws QueryInfoException {
		QueryInfoBeanContext<?, ?, ?> beanContext = beanContextRegistry.getContext(from);
		QueryInfoAttributeContext queryInfoAttributeContext = beanContext.getQueryInfoAttributeContext();
		Map<String, QueryInfoFieldInfo> fields = queryInfoAttributeContext.getFields();

		QueryInfoFieldInfo fieldInfo = fields.get(fieldName);

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

	protected void validateJoinInfo(QueryInfoJPAContext<?> jpaContext,
			String joinName)
			throws QueryInfoException {
		QueryInfoBeanContext<?, ?, ?> beanContext = beanContextRegistry.getContextForRoot(jpaContext);
		QueryInfoAttributeContext queryInfoAttributeContext = beanContext.getQueryInfoAttributeContext();
		Map<String, QueryInfoJoinInfo> joins = queryInfoAttributeContext.getJoins();

		QueryInfoJoinInfo joinInfo = joins.get(joinName);

		if (joinInfo == null) {
			String message = String.format("Field not defined for name [%s]", joinName);
			throw new QueryInfoException(message);
		}
	}
}
