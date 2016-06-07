package com.evanzeimet.queryinfo.jpa.group;

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
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public class DefaultQueryInfoGroupByFactory<RootEntity>
		implements QueryInfoGroupByFactory<RootEntity> {

	@Override
	public List<Expression<?>> createGroupByExpressions(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<Expression<?>> result;
		List<String> groupByFields = queryInfo.getGroupByFields();
		boolean hasGroupByFields = groupByFields != null && !groupByFields.isEmpty();

		if (hasGroupByFields) {
			QueryInfoEntityContext<RootEntity> entityContext = entityContextRegistry.getContextForRoot(jpaContext);

			Root<RootEntity> root = jpaContext.getRoot();
			QueryInfoPathFactory<RootEntity> pathFactory = entityContext.getPathFactory();

			int groupByFieldCount = groupByFields.size();
			result = new ArrayList<Expression<?>>(groupByFieldCount);

			for (String groupByField : groupByFields) {
				Expression<?> path = pathFactory.getPathForField(jpaContext,
						root,
						groupByField,
						QueryInfoAttributePurpose.GROUP_BY);

				result.add(path);
			}

		} else {
			result = Collections.emptyList();
		}

		return result;
	}

}
