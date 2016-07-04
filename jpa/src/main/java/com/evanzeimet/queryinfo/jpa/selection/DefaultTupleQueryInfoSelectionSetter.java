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

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultTupleQueryInfoSelectionSetter<RootEntity>
		implements QueryInfoSelectionSetter<RootEntity> {

	protected QueryInfoUtils utils = new QueryInfoUtils();
	protected QueryInfoSelectionUtils selectionUtils = new QueryInfoSelectionUtils();

	public DefaultTupleQueryInfoSelectionSetter() {

	}

	protected void setAllFieldSelections(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext) throws QueryInfoException {
		List<com.evanzeimet.queryinfo.selection.Selection> selections = selectionUtils.createAllFieldQueryInfoSelections(entityContextRegistry, jpaContext);
		setExplicitSelections(entityContextRegistry, jpaContext, selections);
	}

	protected void setExplicitSelections(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			List<com.evanzeimet.queryinfo.selection.Selection> selections) throws QueryInfoException {
		List<Selection<?>> jpaSelections = selectionUtils.createJpaSelections(entityContextRegistry, jpaContext, selections);

		CriteriaQuery<?> criteriaQuery = jpaContext.getCriteriaQuery();
		criteriaQuery.multiselect(jpaSelections);
	}

	protected void setExplicitSelections(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<com.evanzeimet.queryinfo.selection.Selection> selections = utils.coalesceSelections(queryInfo);
		setExplicitSelections(entityContextRegistry, jpaContext, selections);
	}

	@Override
	public void setSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		boolean hasRequestedAllFields = utils.hasRequestedAllFields(queryInfo);

		if (hasRequestedAllFields) {
			setAllFieldSelections(entityContextRegistry, jpaContext);
		} else {
			setExplicitSelections(entityContextRegistry, jpaContext, queryInfo);
		}
	}
}
