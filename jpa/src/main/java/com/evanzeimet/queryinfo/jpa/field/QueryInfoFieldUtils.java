package com.evanzeimet.queryinfo.jpa.field;

/*
 * #%L queryinfo-jpa $Id:$ $HeadURL:$ %% Copyright (C) 2015 Evan Zeimet %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.sort.Sort;

public class QueryInfoFieldUtils {

	public QueryInfoFieldUtils() {

	}

	public List<String> coalesceRequestedFields(QueryInfo queryInfo) {
		List<String> result = queryInfo.getRequestedFieldNames();

		if (result == null) {
			result = Collections.emptyList();
		}

		return result;
	}

	public List<String> getFieldNames(List<QueryInfoFieldInfo> fields) {
		List<String> result = new ArrayList<>(fields.size());

		for (QueryInfoFieldInfo field : fields) {
			result.add(field.getName());
		}

		return result;
	}

	public boolean hasAnyConditionFieldName(QueryInfo queryInfo, List<String> fieldNames) {
		boolean result;
		ConditionGroup conditionGroup = queryInfo.getConditionGroup();

		if (conditionGroup == null) {
			result = false;
		} else {
			result = hasAnyConditionFieldName(conditionGroup, fieldNames);
		}

		return result;
	}

	public boolean hasAnyConditionFieldName(ConditionGroup conditionGroup,
			List<String> fieldNames) {
		boolean result = false;

		List<ConditionGroup> nestedConditionGroups = conditionGroup.getConditionGroups();

		if (nestedConditionGroups == null) {
			result = false;
		} else {
			for (ConditionGroup nestedConditionGroup : nestedConditionGroups) {
				result = hasAnyConditionFieldName(nestedConditionGroup, fieldNames);

				if (result) {
					break;
				}
			}
		}

		if (!result) {
			List<Condition> conditions = conditionGroup.getConditions();

			if (conditions == null) {
				result = false;
			} else {
				for (Condition condition : conditions) {
					result = hasAnyConditionFieldName(condition, fieldNames);

					if (result) {
						break;
					}
				}
			}
		}

		return result;
	}

	public boolean hasAnyConditionFieldName(Condition condition, List<String> fieldNames) {
		String fieldName = condition.getLeftHandSide();
		return fieldNames.contains(fieldName);
	}

	public boolean hasAnyField(QueryInfo queryInfo, List<QueryInfoFieldInfo> fields) {
		List<String> fieldNames = getFieldNames(fields);
		return hasAnyFieldName(queryInfo, fieldNames);
	}

	public boolean hasAnyFieldName(QueryInfo queryInfo, List<String> fieldNames) {
		boolean result = hasAnySortFieldName(queryInfo, fieldNames);
		result = (result || hasAnyConditionFieldName(queryInfo, fieldNames));
		result = (result || hasAnyRequestedFieldName(queryInfo, fieldNames));
		return result;
	}

	public boolean hasAnyRequestedFieldName(QueryInfo queryInfo, List<String> fieldNames) {
		List<String> requestedFieldNames = queryInfo.getRequestedFieldNames();
		boolean result;

		if (requestedFieldNames == null) {
			result = false;
		} else {
			boolean disjoint = Collections.disjoint(requestedFieldNames, fieldNames);
			result = !disjoint;
		}

		return result;
	}

	public boolean hasAnySortFieldName(QueryInfo queryInfo, List<String> fieldNames) {
		boolean result = false;
		List<Sort> sorts = queryInfo.getSorts();

		if (sorts == null) {
			result = false;
		} else {
			for (Sort sort : sorts) {
				result = hasAnySortFieldName(sort, fieldNames);

				if (result) {
					break;
				}
			}
		}

		return result;
	}

	public boolean hasAnySortFieldName(Sort sort, List<String> fieldNames) {
		String fieldName = sort.getFieldName();
		return fieldNames.contains(fieldName);
	}

	public boolean hasField(QueryInfo queryInfo, QueryInfoFieldInfo field) {
		List<QueryInfoFieldInfo> fields = Arrays.asList(field);
		return hasAnyField(queryInfo, fields);
	}

	public boolean hasRequestedAllFields(QueryInfo queryInfo) {
		List<String> requestedFieldNames = queryInfo.getRequestedFieldNames();
		return (requestedFieldNames == null);
	}

	public boolean requiresAllFields(QueryInfo queryInfo, List<String> fieldNames) {
		boolean result = hasRequestedAllFields(queryInfo);
		result = (result || hasAnyFieldName(queryInfo, fieldNames));
		return result;
	}
}
