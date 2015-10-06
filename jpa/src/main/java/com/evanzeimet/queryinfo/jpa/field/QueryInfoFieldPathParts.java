package com.evanzeimet.queryinfo.jpa.field;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.QueryInfoException;

public class QueryInfoFieldPathParts {

	private String fieldAttributeName;
	private List<String> joinAttributeNames;

	public QueryInfoFieldPathParts() {

	}

	public String getFieldAttributeName() {
		return fieldAttributeName;
	}

	public void setFieldAttributeName(String fieldAttributeName) {
		this.fieldAttributeName = fieldAttributeName;
	}

	public List<String> getJoinAttributeNames() {
		return joinAttributeNames;
	}

	public void setJoinAttributeNames(List<String> joinAttributeNames) {
		this.joinAttributeNames = joinAttributeNames;
	}

	public String consumeJoin() throws QueryInfoException {
		boolean hasNoJoins = (joinAttributeNames == null || joinAttributeNames.isEmpty());

		if (hasNoJoins) {
			String message = "No joins to consume";
			throw new QueryInfoException(message);
		}

		String result = joinAttributeNames.get(0);

		int currentJoinCount = joinAttributeNames.size();
		List<String> remainingJoinAttributeNames = joinAttributeNames.subList(1, currentJoinCount);

		setJoinAttributeNames(remainingJoinAttributeNames);

		return result;
	}

	public static QueryInfoFieldPathParts fromFullPath(String fullPath) throws QueryInfoException {
		int partCount;
		String[] partArray = null;

		if (StringUtils.isBlank(fullPath)) {
			partCount = 0;
		} else {
			partArray = fullPath.split("\\.");
			partCount = partArray.length;
		}

		if (partCount == 0) {
			String message = String.format("Found 0 path parts in [%s]", fullPath);
			throw new QueryInfoException(message);
		}

		int joinCount = (partCount - 1);

		List<String> joinAttributeNames = new ArrayList<>(joinCount);

		for (int partIndex = 0; partIndex < joinCount; partIndex++) {
			String joinAttributeName = partArray[partIndex];
			joinAttributeNames.add(joinAttributeName);
		}

		int fieldAttributeNameIndex = (partCount - 1);
		String fieldAttributeName = partArray[fieldAttributeNameIndex];

		QueryInfoFieldPathParts result = new QueryInfoFieldPathParts();

		result.setFieldAttributeName(fieldAttributeName);
		result.setJoinAttributeNames(joinAttributeNames);

		return result;
	}

	public boolean hasJoins() {
		return !joinAttributeNames.isEmpty();
	}

	public String toString() {
		StringBuilder result = new StringBuilder();

		if (hasJoins()) {
			String joins = StringUtils.join(joinAttributeNames, ".");

			result.append(joins)
				.append(".");
		}

		result.append(fieldAttributeName);

		return result.toString();
	}
}
