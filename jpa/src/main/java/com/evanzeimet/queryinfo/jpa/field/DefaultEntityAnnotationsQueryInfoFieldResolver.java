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


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class DefaultEntityAnnotationsQueryInfoFieldResolver<T>
		implements QueryInfoFieldInfoResolver<T> {

	private static final String NON_UNIQUE_FIELD_NAME_MESSAGE_JOIN_TEXT = String.format(".%n");

	private static final Pattern accessorFieldNamePattern = Pattern.compile("(?:get|set)?(.+)");

	private Class<T> entityClass;

	public DefaultEntityAnnotationsQueryInfoFieldResolver(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected QueryInfoFieldInfo createFieldInfo(Method annotatedMethod) throws QueryInfoException {
		QueryInfoField annotation = annotatedMethod.getAnnotation(QueryInfoField.class);

		String entityAttributeName = createFieldName(annotatedMethod);
		String fieldName = entityAttributeName;
		boolean isQueryable = annotation.isQueryable();
		boolean isSelectable = annotation.isSelectable();
		boolean isSortable = annotation.isSortable();

		return QueryInfoFieldInfoBuilder.create().entityAttributeName(
				entityAttributeName).fieldName(fieldName).isQueryable(isQueryable).isSelectable(
						isSelectable).isSortable(isSortable).build();
	}

	protected String createFieldName(Method annotatedMethod) throws QueryInfoException {
		String methodName = annotatedMethod.getName();
		QueryInfoField annotation = annotatedMethod.getAnnotation(QueryInfoField.class);

		String result = annotation.fieldName();

		if (StringUtils.isBlank(result)) {
			Matcher matcher = accessorFieldNamePattern.matcher(methodName);

			if (matcher.matches()) {
				String group = matcher.group(1);

				String firstLetter = group.substring(0, 1).toLowerCase();
				String theRest = group.substring(1);

				result = firstLetter + theRest;
			} else {
				String message = String.format("Could not match [%s] with pattern [%s]",
						methodName,
						accessorFieldNamePattern);
				throw new QueryInfoException(message);
			}
		}

		return result;
	}

	protected List<QueryInfoFieldInfo> createQueryInfoFieldInfos() throws QueryInfoException {
		List<Method> annotatedMethods = findAnnotatedMethods(entityClass);

		int annotatedMethodCount = annotatedMethods.size();
		List<QueryInfoFieldInfo> result = new ArrayList<>(annotatedMethodCount);

		for (Method annotatedMethod : annotatedMethods) {
			QueryInfoFieldInfo fieldInfo = createFieldInfo(annotatedMethod);
			result.add(fieldInfo);
		}

		return result;
	}

	protected List<Method> findAnnotatedMethods(Class<T> entityClass) {
		Method[] methods = entityClass.getMethods();

		int methodCount = methods.length;
		List<Method> annotatedMethods = new ArrayList<Method>(methodCount);

		for (Method method : methods) {
			if (method.isAnnotationPresent(QueryInfoField.class)) {
				annotatedMethods.add(method);
			}
		}

		return annotatedMethods;
	}

	protected List<String> generateNonUniqueFieldNameMessages(
			ListMultimap<String, QueryInfoFieldInfo> fieldNameMap) {
		List<String> nonUniqueFieldNameTexts = new ArrayList<>();
		Iterator<String> fieldNames = fieldNameMap.keySet().iterator();

		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			List<QueryInfoFieldInfo> fields = fieldNameMap.get(fieldName);
			int fieldInfoCount = fields.size();

			if (fieldInfoCount > 1) {
				List<String> entityAttributeNames = new ArrayList<>(fields.size());

				for (QueryInfoFieldInfo field : fields) {
					String entityAttributeName = field.getEntityAttributeName();
					entityAttributeNames.add(entityAttributeName);
				}

				String pathsText = StringUtils.join(entityAttributeNames, ", ");

				String nonUniqueFieldNameText = String.format(
						"Found [%s] field infos for name [%s]: %s",
						fieldInfoCount,
						fieldName,
						pathsText);
				nonUniqueFieldNameTexts.add(nonUniqueFieldNameText);
			}
		}
		return nonUniqueFieldNameTexts;
	}

	protected ListMultimap<String /* fieldName */, QueryInfoFieldInfo> mapFieldsToNonUniqueNames(List<QueryInfoFieldInfo> fieldInfos) {
		ListMultimap<String, QueryInfoFieldInfo> fieldNameMap = ArrayListMultimap.create();

		for (QueryInfoFieldInfo fieldInfo : fieldInfos) {
			String fieldName = fieldInfo.getFieldName();
			fieldNameMap.put(fieldName, fieldInfo);
		}

		return fieldNameMap;
	}

	protected Map<String /*fieldName */, QueryInfoFieldInfo> mapFieldsToUniqueFieldNames(List<QueryInfoFieldInfo> fieldInfos) {
		int fieldInfoCount = fieldInfos.size();
		Map<String, QueryInfoFieldInfo> result = new HashMap<>(fieldInfoCount);

		for (QueryInfoFieldInfo fieldInfo : fieldInfos) {
			String fieldName = fieldInfo.getFieldName();
			result.put(fieldName, fieldInfo);
		}

		return result;
	}

	// TODO make the map a getter/setter class so we can avoid these comments everywhere?
	@Override
	public Map<String /* fieldName */, QueryInfoFieldInfo> resolve(
			QueryInfoPathFactory<T> pathFactory)
					throws QueryInfoException {
		List<QueryInfoFieldInfo> fieldInfos = createQueryInfoFieldInfos();

		validateFieldNameUniqueness(fieldInfos);

		return mapFieldsToUniqueFieldNames(fieldInfos);
	}

	protected void validateFieldNameUniqueness(List<QueryInfoFieldInfo> fieldInfos)
			throws QueryInfoException {
		ListMultimap<String, QueryInfoFieldInfo> fieldNameMap = mapFieldsToNonUniqueNames(
				fieldInfos);
		List<String> nonUniqueFieldNameMessages = generateNonUniqueFieldNameMessages(fieldNameMap);

		int nonUniqueFieldNameCount = nonUniqueFieldNameMessages.size();

		if (nonUniqueFieldNameCount > 0) {
			String entityName = entityClass.getCanonicalName();
			String nonUniqueFieldsNamesText = StringUtils.join(nonUniqueFieldNameMessages,
					NON_UNIQUE_FIELD_NAME_MESSAGE_JOIN_TEXT);

			String message = String.format(
					"Found [%s] non-unique field names for entity [%s]:%n%s.",
					nonUniqueFieldNameCount,
					entityName,
					nonUniqueFieldsNamesText);
			throw new QueryInfoException(message);
		}
	}
}
