package com.evanzeimet.queryinfo.jpa.selection;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldUtils;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public class DefaultTupleQueryInfoSelectionSetter<RootEntity>
		implements QueryInfoSelectionSetter<RootEntity> {

	protected QueryInfoFieldUtils fieldUtils = new QueryInfoFieldUtils();

	public DefaultTupleQueryInfoSelectionSetter() {

	}

	protected void setRequestAllSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext)
				throws QueryInfoException {
		QueryInfoEntityContext<RootEntity> entityContext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoAttributeContext queryInfoAttributeContext = entityContext.getQueryInfoAttributeContext();

		Map<String, QueryInfoFieldInfo> fields = queryInfoAttributeContext.getFields();
		Iterator<QueryInfoFieldInfo> iterator = fields.values().iterator();

		int selectionCount = fields.size();
		List<String> fieldNames = new ArrayList<>(selectionCount);

		while (iterator.hasNext()) {
			QueryInfoFieldInfo fieldInfo = iterator.next();
			String fieldName = fieldInfo.getName();
			fieldNames.add(fieldName);
		}

		setFieldNamesSelection(entityContextRegistry, jpaContext, fieldNames);
	}

	protected void setFieldNamesSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			List<String> requestedFields) throws QueryInfoException {
		QueryInfoEntityContext<RootEntity> entityContext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory<RootEntity> pathFactory = entityContext.getPathFactory();

		int selectionCount = requestedFields.size();

		Root<RootEntity> root = jpaContext.getRoot();
		List<Selection<?>> selections = new ArrayList<>(selectionCount);

		for (String requestedField : requestedFields) {
			Expression<?> path = pathFactory.getPathForField(entityContextRegistry,
					jpaContext,
					root,
					requestedField,
					QueryInfoAttributePurpose.SELECT);
			path.alias(requestedField);
			selections.add(path);
		}

		CriteriaQuery<Object> criteriaQuery = jpaContext.getCriteriaQuery();
		criteriaQuery.multiselect(selections);
	}

	protected void setRequestedFieldsSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<String> fieldNames = fieldUtils.coalesceRequestedFields(queryInfo);
		setFieldNamesSelection(entityContextRegistry, jpaContext, fieldNames);
	}

	@Override
	public void setSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		boolean hasRequestedAllFields = fieldUtils.hasRequestedAllFields(queryInfo);

		if (hasRequestedAllFields) {
			setRequestAllSelection(entityContextRegistry, jpaContext);
		} else {
			setRequestedFieldsSelection(entityContextRegistry, jpaContext, queryInfo);
		}
	}
}
