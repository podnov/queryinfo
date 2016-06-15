package com.evanzeimet.queryinfo.jpa.jpacontext;


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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;

public class QueryInfoJPAContext<RootEntity> {

	protected CriteriaBuilder criteriaBuilder;
	protected CriteriaQuery<?> criteriaQuery;
	protected Map<QueryInfoJoinKey, Join<?, ?>> joins = new HashMap<>();
	protected Root<RootEntity> root;

	public QueryInfoJPAContext() {

	}

	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	@SuppressWarnings("unchecked")
	public <T> CriteriaQuery<T> getCriteriaQuery() {
		return (CriteriaQuery<T>) criteriaQuery;
	}

	public void setCriteriaQuery(CriteriaQuery<?> criteriaQuery) {
		this.criteriaQuery = criteriaQuery;
	}

	public Root<RootEntity> getRoot() {
		return root;
	}

	public void setRoot(Root<RootEntity> root) {
		this.root = root;
	}

	protected QueryInfoJoinKey createJoinKey(From<?, ?> joinParent,
			QueryInfoAttributeInfo attributeInfo) {
		QueryInfoJoinKey result = new QueryInfoJoinKey();

		result.setJoinParent(joinParent);

		String jpaAttributeName = attributeInfo.getJpaAttributeName();
		result.setParentAttributeName(jpaAttributeName);

		return result;
	}

	@SuppressWarnings("unchecked")
	public <Z, X> Join<Z, X> getJoin(From<?, ?> joinParent,
			QueryInfoAttributeInfo attributeInfo) {
		QueryInfoJoinKey joinKey = createJoinKey(joinParent,
				attributeInfo);

		Join<?, ?> result = joins.get(joinKey);

		if (result == null) {
			String jpaAttributeName = attributeInfo.getJpaAttributeName();
			QueryInfoJoinType queryInfoJoinType = attributeInfo.getJoinType();

			boolean queryInfoJoinTypeIsUnspecified = QueryInfoJoinType.isUnspecified(queryInfoJoinType);

			if (queryInfoJoinTypeIsUnspecified) {
				result = joinParent.join(jpaAttributeName);
			} else {
				JoinType jpaJoinType = queryInfoJoinType.toJpaType();
				result = joinParent.join(jpaAttributeName, jpaJoinType);
			}

			putJoin(joinKey, result);
		}

		return (Join<Z, X>) result;
	}

	public QueryInfoJoinKey putJoin(From<?, ?> joinParent,
			QueryInfoAttributeInfo attributeInfo,
			Join<?, ?> join) {
		QueryInfoJoinKey joinKey = createJoinKey(joinParent, attributeInfo);
		putJoin(joinKey, join);
		return joinKey;
	}

	public void putJoin(QueryInfoJoinKey joinKey, Join<?, ?> join) {
		joins.put(joinKey, join);
	}
}
