package com.evanzeimet.queryinfo.jpa.attribute;

import java.lang.annotation.Annotation;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfoBuilder;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoin;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfoBuilder;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class DefaultEntityAnnotationsAttributeInfoResolver<T>
		implements QueryInfoAtrributeInfoResolver<T> {

	private static final String NON_UNIQUE_FIELD_NAME_MESSAGE_JOIN_TEXT = String.format(".%n");

	private static final Pattern accessorFieldNamePattern = Pattern.compile("(?:get|set)?(.+)");

	private Class<T> entityClass;

	public DefaultEntityAnnotationsAttributeInfoResolver(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected QueryInfoAttributeContext createAttributeInfo(List<QueryInfoFieldInfo> fields,
			List<QueryInfoJoinInfo> joins) {
		return QueryInfoAttributeContextBuilder.create()
				.fields(fields)
				.joins(joins)
				.build();
	}

	protected String createJpaAttributeName(Method annotatedMethod) throws QueryInfoException {
		String result;
		String methodName = annotatedMethod.getName();
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

		return result;
	}

	protected QueryInfoFieldInfo createFieldInfo(Method annotatedMethod) throws QueryInfoException {
		QueryInfoField annotation = annotatedMethod.getAnnotation(QueryInfoField.class);

		String jpaAttributeName = createJpaAttributeName(annotatedMethod);
		String fieldName = annotation.name();

		if (StringUtils.isBlank(fieldName)) {
			fieldName = jpaAttributeName;
		}

		return QueryInfoFieldInfoBuilder.create()
				.annotation(annotation)
				.name(fieldName)
				.jpaAttributeName(jpaAttributeName)
				.build();
	}

	protected List<QueryInfoFieldInfo> createFields() throws QueryInfoException {
		List<Method> annotatedMethods = findAnnotatedMethods(entityClass, QueryInfoField.class);

		int annotatedMethodCount = annotatedMethods.size();
		List<QueryInfoFieldInfo> result = new ArrayList<>(annotatedMethodCount);

		for (Method annotatedMethod : annotatedMethods) {
			QueryInfoFieldInfo fieldInfo = createFieldInfo(annotatedMethod);
			result.add(fieldInfo);
		}

		return result;
	}

	protected QueryInfoJoinInfo createJoinInfo(Method annotatedMethod) throws QueryInfoException {
		QueryInfoJoin annotation = annotatedMethod.getAnnotation(QueryInfoJoin.class);

		String jpaAttributeName = createJpaAttributeName(annotatedMethod);
		String name = annotation.name();

		return QueryInfoJoinInfoBuilder.create()
				.jpaAttributeName(jpaAttributeName)
				.name(name)
				.build();
	}

	protected List<QueryInfoJoinInfo> createJoins() throws QueryInfoException {
		List<Method> annotatedMethods = findAnnotatedMethods(entityClass, QueryInfoJoin.class);

		int annotatedMethodCount = annotatedMethods.size();
		List<QueryInfoJoinInfo> result = new ArrayList<>(annotatedMethodCount);

		for (Method annotatedMethod : annotatedMethods) {
			QueryInfoJoinInfo joinInfo = createJoinInfo(annotatedMethod);
			result.add(joinInfo);
		}

		return result;
	}

	protected List<Method> findAnnotatedMethods(Class<T> entityClass,
			Class<? extends Annotation> annotationClass) {
		Method[] methods = entityClass.getMethods();

		int methodCount = methods.length;
		List<Method> annotatedMethods = new ArrayList<Method>(methodCount);

		for (Method method : methods) {
			if (method.isAnnotationPresent(annotationClass)) {
				annotatedMethods.add(method);
			}
		}

		return annotatedMethods;
	}

	protected List<String> generateNonUniqueAttributeNameMessages(ListMultimap<String, QueryInfoAttributeInfo> attributesMap) {
		List<String> nonUniqueFieldNameTexts = new ArrayList<>();
		Iterator<String> attributeNames = attributesMap.keySet().iterator();

		while (attributeNames.hasNext()) {
			String attributeName = attributeNames.next();
			List<QueryInfoAttributeInfo> attributes = attributesMap.get(attributeName);
			int attributeCount = attributes.size();

			if (attributeCount > 1) {
				List<String> jpaAttributeNames = new ArrayList<>(attributes.size());

				for (QueryInfoAttributeInfo attribute : attributes) {
					String jpaAttributeName = attribute.getJpaAttributeName();
					jpaAttributeNames.add(jpaAttributeName);
				}

				String jpaAttributeNamesTest = StringUtils.join(jpaAttributeNames, ", ");

				String nonUniqueFieldNameText = String.format("Found [%s] field infos for name [%s]: %s",
						attributeCount,
						attributeName,
						jpaAttributeNamesTest);
				nonUniqueFieldNameTexts.add(nonUniqueFieldNameText);
			}
		}
		return nonUniqueFieldNameTexts;
	}

	protected ListMultimap<String, QueryInfoAttributeInfo> mapAttributesToNames(List<QueryInfoAttributeInfo> attributes) {
		ListMultimap<String, QueryInfoAttributeInfo> result = ArrayListMultimap.create();

		for (QueryInfoAttributeInfo attribute : attributes) {
			String attributeName = attribute.getName();
			result.put(attributeName, attribute);
		}

		return result;
	}

	@Override
	public QueryInfoAttributeContext resolve(QueryInfoPathFactory<T> pathFactory)
			throws QueryInfoException {
		List<QueryInfoFieldInfo> fields = createFields();
		List<QueryInfoJoinInfo> joins = createJoins();

		validateAttributeNameUniqueness(fields, joins);

		return createAttributeInfo(fields, joins);
	}

	protected void validateAttributeNameUniqueness(List<QueryInfoFieldInfo> fields, List<QueryInfoJoinInfo> joins)
			throws QueryInfoException {
		int fieldCount = fields.size();
		int joinCount = joins.size();

		int attributeCount = (fieldCount + joinCount);

		List<QueryInfoAttributeInfo> attributes = new ArrayList<>(attributeCount);

		attributes.addAll(fields);
		attributes.addAll(joins);

		ListMultimap<String, QueryInfoAttributeInfo> fieldNameMap = mapAttributesToNames(attributes);
		List<String> nonUniqueAttributeNameMessages = generateNonUniqueAttributeNameMessages(fieldNameMap);

		int nonUniqueFieldNameCount = nonUniqueAttributeNameMessages.size();

		if (nonUniqueFieldNameCount > 0) {
			Collections.sort(nonUniqueAttributeNameMessages);

			String entityName = entityClass.getCanonicalName();
			String nonUniqueFieldsNamesText = StringUtils.join(nonUniqueAttributeNameMessages,
					NON_UNIQUE_FIELD_NAME_MESSAGE_JOIN_TEXT);

			String message = String.format("Found [%s] non-unique attribute names for entity [%s]:%n%s.",
					nonUniqueFieldNameCount,
					entityName,
					nonUniqueFieldsNamesText);
			throw new QueryInfoException(message);
		}
	}
}
