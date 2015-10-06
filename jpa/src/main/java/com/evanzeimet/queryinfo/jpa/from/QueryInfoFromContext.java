package com.evanzeimet.queryinfo.jpa.from;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

public class QueryInfoFromContext {

	protected Map<QueryInfoJoinKey, Join<?, ?>> joins = new HashMap<>();
	protected From<?, ?> root;

	public QueryInfoFromContext() {

	}

	@SuppressWarnings("unchecked")
	public <L, R> From<L, R> getRoot() {
		return (From<L, R>) root;
	}

	public void setRoot(From<?, ?> root) {
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
	public <L, R> Join<L, R> getJoin(From<L, ?> joinParent,
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
	public <L, R> Join<L, R> resolveJoin(From<L, ?> joinParent,
			String joinAttributeName) {
		Join<L, Object> result = getJoin(joinParent, joinAttributeName);

		if (result == null) {
			result = joinParent.join(joinAttributeName);
			putJoin(joinParent, joinAttributeName, result);
		}

		return (Join<L, R>) result;
	}
}
