package com.evanzeimet.queryinfo.jpa.jpacontext;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

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
			String joinAttributeName) {
		QueryInfoJoinKey result = new QueryInfoJoinKey();

		result.setJoinParent(joinParent);
		result.setParentAttributeName(joinAttributeName);

		return result;
	}

	@SuppressWarnings("unchecked")
	public <L, R> Join<L, R> getJoin(From<?, ?> joinParent,
			String joinAttributeName) {

		QueryInfoJoinKey joinKey = createJoinKey(joinParent,
				joinAttributeName);

		Join<?, ?> result = joins.get(joinKey);

		if (result == null) {
			result = joinParent.join(joinAttributeName);
			putJoin(joinKey, result);
		}

		return (Join<L, R>) result;
	}

	public QueryInfoJoinKey putJoin(From<?, ?> joinParent,
			String attributeName,
			Join<?, ?> join) {
		QueryInfoJoinKey joinKey = createJoinKey(joinParent, attributeName);
		putJoin(joinKey, join);
		return joinKey;
	}

	public void putJoin(QueryInfoJoinKey joinKey, Join<?, ?> join) {
		joins.put(joinKey, join);
	}

	@SuppressWarnings("unchecked")
	public <L, R> Join<L, R> resolveJoin(From<?, ?> joinParent,
			String joinAttributeName) {
		Join<?, ?> result = getJoin(joinParent, joinAttributeName);

		if (result == null) {
			result = joinParent.join(joinAttributeName);
			putJoin(joinParent, joinAttributeName, result);
		}

		return (Join<L, R>) result;
	}
}
