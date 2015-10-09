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
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldUtils;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public class DefaultEntityQueryInfoSelectionSetter<RootEntity>
		implements QueryInfoSelectionSetter<RootEntity> {

	protected QueryInfoBeanContextRegistry beanContextRegistry;
	protected QueryInfoFieldUtils fieldUtils = new QueryInfoFieldUtils();

	public DefaultEntityQueryInfoSelectionSetter(QueryInfoBeanContextRegistry beanContextRegistry) {
		this.beanContextRegistry = beanContextRegistry;
	}

	protected void setRequestAllSelection(QueryInfoJPAContext<RootEntity> jpaContext) {
		CriteriaQuery<RootEntity> criteriaQuery = jpaContext.getCriteriaQuery();
		Root<RootEntity> root = jpaContext.getRoot();
		criteriaQuery.select(root);
	}

	protected void setRequestedFieldsSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<String> requestedFields = fieldUtils.coalesceRequestedFields(queryInfo);

		QueryInfoBeanContext<RootEntity, ?, ?> queryInfoBeanContext = beanContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory<RootEntity> pathFactory = queryInfoBeanContext.getPathFactory();

		int selectionCount = requestedFields.size();

		Root<RootEntity> root = jpaContext.getRoot();
		List<Selection<?>> selections = new ArrayList<>(selectionCount);

		for (String requestedField : requestedFields) {
			Expression<?> path = pathFactory.getPathForField(jpaContext,
					root,
					requestedField,
					QueryInfoFieldPurpose.SELECT);
			selections.add(path);
		}

		CriteriaQuery<Object> criteriaQuery = jpaContext.getCriteriaQuery();
		criteriaQuery.multiselect(selections);
	}

	@Override
	public void setSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		boolean hasRequestedAllFields = fieldUtils.hasRequestedAllFields(queryInfo);

		if (hasRequestedAllFields) {
			setRequestAllSelection(jpaContext);
		} else {
			setRequestedFieldsSelection(jpaContext, queryInfo);
		}
	}
}
