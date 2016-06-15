package com.evanzeimet.queryinfo.jpa.attribute;

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

import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.metamodel.Attribute;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class QueryInfoAttributeUtils {

	public <Z, X> Join<Z, X> getJoinForAttributePath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?> jpaContext,
			List<String> jpaAttributeNames) {
		Join<Z, X> result = null;
		From<?, ?> currentJoinParent = jpaContext.getRoot();
		QueryInfoEntityContext<?> currentEntityContext = entityContextRegistry.getContext(currentJoinParent);

		for (String jpaAttributeName : jpaAttributeNames) {
			QueryInfoAttributeContext currentAttributeContext = currentEntityContext.getAttributeContext();

			QueryInfoJoinInfo currentJoinInfo = getJoinInfo(currentAttributeContext, jpaAttributeName);
			result = jpaContext.getJoin(currentJoinParent, currentJoinInfo);

			currentJoinParent = result;
			Class<?> currentJoinParentEntity = currentJoinParent.getModel().getBindableJavaType();
			currentEntityContext = entityContextRegistry.getContext(currentJoinParentEntity);
		}

		return result;
	}

	public QueryInfoJoinInfo getJoinInfo(QueryInfoAttributeContext attributeContext,
			Attribute<?, ?> jpaAttribute) {
		String jpaAttributeName = jpaAttribute.getName();
		return getJoinInfo(attributeContext, jpaAttributeName);
	}

	public QueryInfoJoinInfo getJoinInfo(QueryInfoAttributeContext attributeContext,
			String jpaAttributeName) {
		QueryInfoJoinInfo result = null;
		Iterator<QueryInfoJoinInfo> joinInfos = attributeContext.getJoins().values().iterator();

		while (joinInfos.hasNext()) {
			QueryInfoJoinInfo joinInfo = joinInfos.next();

			String joinInfoJpaAttributeName = joinInfo.getJpaAttributeName();

			if (joinInfoJpaAttributeName.equals(jpaAttributeName)) {
				result = joinInfo;
				break;
			}
		}

		return result;
	}

}
