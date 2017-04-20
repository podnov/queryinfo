package com.evanzeimet.queryinfo.jpa.path;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPathParts;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultQueryInfoPathFactory<RootEntity>
		implements QueryInfoPathFactory<RootEntity> {

	protected Class<RootEntity> entityClass;

	public DefaultQueryInfoPathFactory(Class<RootEntity> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public Class<RootEntity> getEntityClass() {
		return entityClass;
	}

	protected QueryInfoAttributeContext getAttributeContext(QueryInfoEntityContextRegistry entityContextRegistry,
			From<?, RootEntity> from) {
		QueryInfoEntityContext<?> entityContext = entityContextRegistry.getContext(from);
		return entityContext.getAttributeContext();
	}

	protected <T> Path<T> getAttributePath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath,
			QueryInfoAttributePurpose purpose)
			throws QueryInfoException {
		Path<T> result = null;

		switch (purpose) {
			case GROUP_BY:
			case ORDER:
			case PREDICATE:
			case SELECT:
				result = getFieldPath(entityContextRegistry, jpaContext, from, attributePath, purpose);
				break;

			case SUBQUERY_ROOT:
				result = getSubqueryRootPath(entityContextRegistry, jpaContext, from, attributePath, purpose);
				break;
		}

		return result;
	}

	protected QueryInfoFieldInfo getFieldInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			From<?, RootEntity> from,
			String attributePath) {
		QueryInfoAttributeContext queryInfoAttributeContext = getAttributeContext(entityContextRegistry,
				from);

		return queryInfoAttributeContext.getField(attributePath);
	}

	protected <T> Path<T> getFieldPath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		QueryInfoFieldInfo fieldInfo = validateFieldInfo(entityContextRegistry,
				jpaContext,
				from,
				attributePath,
				purpose);

		Path<T> result = null;
		String jpaFieldAttributeName = fieldInfo.getJpaAttributeName();

		try {
			result = from.get(jpaFieldAttributeName);
		} catch (IllegalArgumentException e) {
			String fromName = from.getModel().getBindableJavaType().getName();
			String message = String.format("Could not find jpa attribute [%s] attribute path [%s] in [%s]",
					jpaFieldAttributeName,
					attributePath,
					fromName);
			throw new QueryInfoException(message, e);
		}

		return result;
	}

	protected <JoinedEntity> Join<RootEntity, JoinedEntity> getJoin(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath) throws QueryInfoException {
		QueryInfoJoinInfo queryInfoJoinInfo = validateJoinInfo(entityContextRegistry,
				from,
				attributePath);

		return jpaContext.getJoin(from, queryInfoJoinInfo);
	}

	protected QueryInfoJoinInfo getJoinInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			From<?, RootEntity> from,
			String attributePath) {
		QueryInfoAttributeContext queryInfoAttributeContext = getAttributeContext(entityContextRegistry,
				from);
		return queryInfoAttributeContext.getJoin(attributePath);
	}

	protected <T> Path<T> getJoinPath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		QueryInfoJoinInfo joinInfo = validateJoinInfo(entityContextRegistry,
				from,
				attributePath);

		Path<T> result = null;
		String jpaJoinAttributeName = joinInfo.getJpaAttributeName();

		try {
			result = from.get(jpaJoinAttributeName);
		} catch (IllegalArgumentException e) {
			String fromName = from.getModel().getBindableJavaType().getName();
			String message = String.format("Could not find jpa attribute [%s] for join attribute path [%s] in [%s]",
					jpaJoinAttributeName,
					attributePath,
					fromName);
			throw new QueryInfoException(message, e);
		}

		return result;
	}

	protected <JoinedEntity> QueryInfoPathFactory<JoinedEntity> getJoinPathFactory(QueryInfoEntityContextRegistry entityContextRegistry,
			Join<RootEntity, JoinedEntity> join) {
		Class<JoinedEntity> joinedClass = join.getModel().getBindableJavaType();
		QueryInfoEntityContext<JoinedEntity> joinBeanContext = entityContextRegistry.getContext(joinedClass);

		return joinBeanContext.getPathFactory();
	}

	@Override
	public <T> Path<T> getPathForAttribute(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		Path<T> result = null;

		QueryInfoFieldPathParts pathParts = QueryInfoFieldPathParts.fromFullPath(attributePath);

		if (pathParts.hasJoins()) {
			result = walkJoinPaths(entityContextRegistry, jpaContext, from, pathParts, purpose);
		} else {
			result = getAttributePath(entityContextRegistry, jpaContext, from, attributePath, purpose);
		}

		return result;
	}

	protected <T> Path<T> getSubqueryRootPath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		Path<T> result = null;
		QueryInfoFieldInfo fieldInfo = getFieldInfo(entityContextRegistry, from, attributePath);

		if (fieldInfo == null) {
			QueryInfoJoinInfo joinInfo = getJoinInfo(entityContextRegistry, from, attributePath);

			if (joinInfo != null) {
				result = getJoin(entityContextRegistry, jpaContext, from, attributePath);
			}
		} else {
			result = getFieldPath(entityContextRegistry, jpaContext, from, attributePath, purpose);
		}

		return result;
	}

	protected boolean isFieldInfoValidForPurpose(QueryInfoFieldInfo fieldInfo, QueryInfoAttributePurpose purpose) {
		boolean result = false;

		switch (purpose) {
			case GROUP_BY:
			case SELECT:
				result = fieldInfo.getIsSelectable();
				break;

			case ORDER:
				result = fieldInfo.getIsOrderable();
				break;

			case PREDICATE:
			case SUBQUERY_ROOT:
				result = fieldInfo.getIsPredicateable();
				break;
		}

		return result;
	}

	protected QueryInfoFieldInfo validateFieldInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			String attributePath,
			QueryInfoAttributePurpose purpose)
			throws QueryInfoException {

		if (purpose == null) {
			String message = String.format("No purpose for requested attribute path [%s] specified", attributePath);
			throw new QueryInfoException(message);
		}

		QueryInfoFieldInfo fieldInfo = getFieldInfo(entityContextRegistry, from, attributePath);

		if (fieldInfo == null) {
			String message = String.format("Field not defined for attribute path [%s]", attributePath);
			throw new QueryInfoException(message);
		}

		validateFieldInfoForPurpose(fieldInfo, purpose);

		QueryInfoJoinType queryInfoJoinType = fieldInfo.getJoinType();
		boolean joinTypeIsSpecified = !QueryInfoJoinType.isUnspecified(queryInfoJoinType);

		if (joinTypeIsSpecified) {
			/*
			 * Bootstrap the correct join type if it is not already. Without this, JPA will
			 * add an inner join by default. This probably only applies to the SELECT purpose.
			 */
			jpaContext.getJoin(from, fieldInfo);
		}

		return fieldInfo;
	}

	protected void validateFieldInfoForPurpose(QueryInfoFieldInfo fieldInfo, QueryInfoAttributePurpose purpose)
			throws QueryInfoException {
		boolean validForPurpose = isFieldInfoValidForPurpose(fieldInfo, purpose);

		if (!validForPurpose) {
			String fieldName = fieldInfo.getName();
			String message = String.format("Field name [%s] is not valid for [%s]", fieldName, purpose);
			throw new QueryInfoException(message);
		}
	}

	protected QueryInfoJoinInfo validateJoinInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			From<?, RootEntity> from,
			String attributePath)
			throws QueryInfoException {
		QueryInfoJoinInfo joinInfo = getJoinInfo(entityContextRegistry, from, attributePath);

		if (joinInfo == null) {
			String message = String.format("Join not defined for attribute path [%s]", attributePath);
			throw new QueryInfoException(message);
		}

		return joinInfo;
	}

	protected <T, JoinedEntity> Path<T> walkJoinPaths(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
			From<?, RootEntity> from,
			QueryInfoFieldPathParts pathParts,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		String queryInfoJoinAttributeName = pathParts.consumeJoin();

		Join<RootEntity, JoinedEntity> join = getJoin(entityContextRegistry,
				jpaContext,
				from,
				queryInfoJoinAttributeName);
		QueryInfoPathFactory<JoinedEntity> joinPathFactory = getJoinPathFactory(entityContextRegistry,
				join);

		String attributePath = pathParts.toString();

		return joinPathFactory.getPathForAttribute(entityContextRegistry,
				jpaContext,
				join,
				attributePath,
				purpose);
	}

}
