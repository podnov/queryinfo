package com.evanzeimet.queryinfo.jpa.selection;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
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

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathUtils;
import com.evanzeimet.queryinfo.selection.AggregateFunction;
import com.evanzeimet.queryinfo.selection.DefaultSelection;

public class QueryInfoSelectionUtils {

	protected QueryInfoPathUtils pathUtils = new QueryInfoPathUtils();

	public <E> Expression<?> createJpaSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<E, ?> jpaContext,
			QueryInfoPathFactory<E> pathFactory,
			com.evanzeimet.queryinfo.selection.Selection selection) throws QueryInfoException {
		Root<E> root = jpaContext.getRoot();
		String givenAttributePath = selection.getAttributePath();

		Expression<?> result = pathFactory.getPathForAttribute(entityContextRegistry,
				jpaContext,
				root,
				givenAttributePath,
				QueryInfoAttributePurpose.SELECT);

		AggregateFunction aggregateFunction = selection.getAggregateFunction();

		if (aggregateFunction != null) {
			result = pathUtils.applyAggregate(jpaContext, result, aggregateFunction);
		}

		result.alias(givenAttributePath);

		return result;
	}

	public <E> List<Selection<?>> createJpaSelections(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<E, ?> jpaContext,
			List<com.evanzeimet.queryinfo.selection.Selection> selections) throws QueryInfoException {
		QueryInfoEntityContext<E> entityContext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory<E> pathFactory = entityContext.getPathFactory();

		int selectionCount = selections.size();
		List<Selection<?>> result = new ArrayList<>(selectionCount);

		for (com.evanzeimet.queryinfo.selection.Selection givenSelection : selections) {
			Expression<?> path = createJpaSelection(entityContextRegistry, jpaContext, pathFactory, givenSelection);
			result.add(path);
		}

		return result;
	}

	public List<com.evanzeimet.queryinfo.selection.Selection> createAllFieldQueryInfoSelections(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext) {
		QueryInfoEntityContext<?> entityContext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoAttributeContext queryInfoAttributeContext = entityContext.getAttributeContext();

		Map<String, QueryInfoFieldInfo> fields = queryInfoAttributeContext.getFields();
		Iterator<QueryInfoFieldInfo> iterator = fields.values().iterator();

		int selectionCount = fields.size();
		List<com.evanzeimet.queryinfo.selection.Selection> result = new ArrayList<>(selectionCount);

		while (iterator.hasNext()) {
			QueryInfoFieldInfo fieldInfo = iterator.next();
			String fieldName = fieldInfo.getName();

			com.evanzeimet.queryinfo.selection.Selection selection = new DefaultSelection();
			selection.setAttributePath(fieldName);

			result.add(selection);
		}

		return result;
	}
}
