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

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

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

	protected QueryInfoEntityContextRegistry entityContextRegistry;
	protected Class<RootEntity> entityClass;

	public DefaultQueryInfoPathFactory(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<RootEntity> entityClass) {
		this.entityContextRegistry = entityContextRegistry;
		this.entityClass = entityClass;
	}

	@Override
	public Class<RootEntity> getEntityClass() {
		return entityClass;
	}

	@Override
	public void setEntityContextRegistry(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;
	}

	protected QueryInfoAttributeContext getAttributeContext(From<?, RootEntity> from) {
		QueryInfoEntityContext<?> entityContext = getEntityContext(from);
		return entityContext.getQueryInfoAttributeContext();
	}

	protected QueryInfoEntityContext<RootEntity> getEntityContext(From<?, RootEntity> from) {
		return entityContextRegistry.getContext(from);
	}

	protected <T> Expression<T> getEntityPath(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String queryInfoFieldAttributeName,
			QueryInfoAttributePurpose purpose) throws QueryInfoException {
		Expression<T> result = null;

		QueryInfoFieldInfo fieldInfo = validateFieldInfo(jpaContext,
				from,
				queryInfoFieldAttributeName,
				purpose);

		String jpaFieldAttributeName = fieldInfo.getJpaAttributeName();

		try {
			result = from.get(jpaFieldAttributeName);
		} catch (IllegalArgumentException e) {
			String fromName = from.getModel().getBindableJavaType().getName();
			String message = String.format("Could not find jpa attribute [%s] for field [%s] in [%s]",
					jpaFieldAttributeName,
					queryInfoFieldAttributeName,
					fromName);
			throw new QueryInfoException(message, e);
		}

		return result;
	}

	protected <JoinedEntity> Join<RootEntity, JoinedEntity> getJoin(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String queryInfoJoinAttributeName) throws QueryInfoException {
		QueryInfoJoinInfo querInfoJoinInfo = validateJoinInfo(from, queryInfoJoinAttributeName);

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

	protected <JoinedEntity> QueryInfoPathFactory<JoinedEntity> getJoinPathFactory(
			Join<RootEntity, JoinedEntity> join) {
		Class<JoinedEntity> joinedClass = join.getModel().getBindableJavaType();
		QueryInfoEntityContext<JoinedEntity> joinBeanContext = entityContextRegistry.getContext(joinedClass);

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

	protected QueryInfoFieldInfo validateFieldInfo(QueryInfoJPAContext<?> jpaContext,
			From<?, RootEntity> from,
			String queryInfoFieldAttributeName,
			QueryInfoAttributePurpose purpose)
					throws QueryInfoException {

		if (purpose == null) {
			String message = String.format("No purpose for field [%s] specified", queryInfoFieldAttributeName);
			throw new QueryInfoException(message);
		}

		QueryInfoAttributeContext queryInfoAttributeContext = getAttributeContext(from);

		QueryInfoFieldInfo fieldInfo = queryInfoAttributeContext.getField(queryInfoFieldAttributeName);

		if (fieldInfo == null) {
			String message = String.format("Field not defined for name [%s]", queryInfoFieldAttributeName);
			throw new QueryInfoException(message);
		}

		boolean validForPurpose = false;

		switch (purpose) {
			case GROUP_BY:
			case SELECT:
				validForPurpose = fieldInfo.getIsSelectable();
				break;

			case ORDER:
				validForPurpose = fieldInfo.getIsOrderable();
				break;

			case PREDICATE:
				validForPurpose = fieldInfo.getIsPredicateable();
				break;
		}

		if (!validForPurpose) {
			String message = String.format("Field [%s] not valid for [%s]", queryInfoFieldAttributeName, purpose);
			throw new QueryInfoException(message);
		}

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

	protected QueryInfoJoinInfo validateJoinInfo(From<?, RootEntity> from, String queryInfoJoinAttributeName)
			throws QueryInfoException {
		QueryInfoAttributeContext queryInfoAttributeContext = getAttributeContext(from);
		QueryInfoJoinInfo querInfoJoinInfo = queryInfoAttributeContext.getJoin(queryInfoJoinAttributeName);

		if (querInfoJoinInfo == null) {
			String message = String.format("Join not defined for name [%s]", queryInfoJoinAttributeName);
			throw new QueryInfoException(message);
		}

		return querInfoJoinInfo;
	}
}
