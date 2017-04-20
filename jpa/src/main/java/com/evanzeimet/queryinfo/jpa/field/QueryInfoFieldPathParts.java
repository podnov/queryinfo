package com.evanzeimet.queryinfo.jpa.field;

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
