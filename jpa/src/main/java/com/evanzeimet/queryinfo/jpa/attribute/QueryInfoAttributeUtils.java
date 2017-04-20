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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.metamodel.Attribute;

import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class QueryInfoAttributeUtils {

	public static final QueryInfoAttributeType ATTRIBUTE_TYPE_NOT_SPECIFIED = null;
	public static final QueryInfoEntityContextRegistry ENTITY_CONTEXT_REGISTRY_NOT_USED = null;

	private static final Pattern convertAttributeNameToMemberNamePattern = Pattern.compile(""
			+ "(\\w+)" // capture all word characters
			+ "(\\.|$)" // match either join notation "." or end of line
	);

	public String convertAttributeNameToMemberName(String attributeName) {
		Matcher matcher = convertAttributeNameToMemberNamePattern.matcher(attributeName);
		boolean foundFirstMatch = false;

		int characterCount = attributeName.length();
		StringBuilder result = new StringBuilder(characterCount);

		while (matcher.find()) {
			String attributePart = matcher.group(1);

			if (foundFirstMatch) {
				String firstLetter = attributePart.substring(0, 1).toUpperCase();
				String otherLetters = attributePart.substring(1);

				result.append(firstLetter)
						.append(otherLetters);
			} else {
				result.append(attributePart);
			}

			foundFirstMatch = true;
		}

		return result.toString();
	}

	protected <T> QueryInfoAttributeContext getAttributeContext(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> clazz) {
		QueryInfoEntityContext<T> entityContext = entityContextRegistry.getContext(clazz);

		if (entityContext == null) {
			String message = String.format("Could not find entity context for [%s]",
					clazz.getCanonicalName());
			throw new QueryInfoRuntimeException(message);
		}

		return entityContext.getAttributeContext();
	}

	protected <T> Class<T> getAttributeHostType(Attribute<T, ?> attribute) {
		return attribute.getDeclaringType().getJavaType();
	}

	public <T extends QueryInfoAttributeInfo> T getAttributeInfo(Map<String, T> attributes,
			Attribute<?, ?> jpaAttribute) {
		String jpaAttributeName = jpaAttribute.getName();
		return getAttributeInfo(attributes, jpaAttributeName);
	}

	public <T extends QueryInfoAttributeInfo> T getAttributeInfo(Map<String, T> attributes, String jpaAttributeName) {
		T result = null;
		Iterator<T> attributeIterator = attributes.values().iterator();

		while (attributeIterator.hasNext()) {
			T attributeInfo = attributeIterator.next();

			String joinInfoJpaAttributeName = attributeInfo.getJpaAttributeName();

			if (joinInfoJpaAttributeName.equals(jpaAttributeName)) {
				result = attributeInfo;
				break;
			}
		}

		return result;
	}

	public <T> QueryInfoAttributeInfo getAttributeInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> attributeHost,
			Attribute<? super T, ?> attribute,
			QueryInfoAttributeType attributeType) {
		QueryInfoAttributeInfo result = null;

		if (attributeType == ATTRIBUTE_TYPE_NOT_SPECIFIED) {
			result = getFieldInfo(entityContextRegistry, attributeHost, attribute);

			if (result == null) {
				result = getJoinInfo(entityContextRegistry, attributeHost, attribute);
			}
		} else if (QueryInfoAttributeType.JOIN.equals(attributeType)) {
			result = getJoinInfo(entityContextRegistry, attributeHost, attribute);
		} else if (QueryInfoAttributeType.FIELD.equals(attributeType)) {
			result = getFieldInfo(entityContextRegistry, attributeHost, attribute);
		}

		return result;
	}

	public <T> String getAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<T, ?> fieldAttribute,
			QueryInfoAttributeType attributeType) {
		Class<T> attributeHost = getAttributeHostType(fieldAttribute);
		return getAttributeName(entityContextRegistry, attributeHost, fieldAttribute, attributeType);
	}

	public <T> String getAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> attributeHost,
			Attribute<? super T, ?> attribute,
			QueryInfoAttributeType attributeType) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = attribute.getName();
		} else {
			QueryInfoAttributeInfo attributeInfo = getAttributeInfo(entityContextRegistry,
					attributeHost,
					attribute,
					attributeType);

			if (attributeInfo == null) {
				String rawAttributeType = (attributeType == null
						? "unspecified"
						: attributeType.name());

				String message = createAttributeNotFoundMessage(attributeHost,
						attribute);
				message = String.format("%s using attribute type [%s]", message, rawAttributeType);

				throw new QueryInfoRuntimeException(message);
			}

			result = attributeInfo.getName();
		}

		return result;
	}

	public <T> String getFieldAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<T, ?> fieldAttribute) {
		Class<T> attributeHost = getAttributeHostType(fieldAttribute);
		return getFieldAttributeName(entityContextRegistry, attributeHost, fieldAttribute);
	}

	public <T> String getFieldAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> attributeHost,
			Attribute<? super T, ?> fieldAttribute) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = fieldAttribute.getName();
		} else {
			QueryInfoFieldInfo fieldInfo = getFieldInfo(entityContextRegistry,
					attributeHost,
					fieldAttribute);

			if (fieldInfo == null) {
				String message = createAttributeNotFoundMessage(attributeHost, fieldAttribute);
				throw new QueryInfoRuntimeException(message);
			}

			result = fieldInfo.getName();
		}

		return result;
	}

	public <T> String createAttributeNotFoundMessage(Class<T> attributeHost,
			Attribute<? super T, ?> attribute) {
		String attributeHostClass = attributeHost.getCanonicalName();
		String attributeName = attribute.getName();
		return String.format("No attribute found for [%s] on [%s]",
				attributeName,
				attributeHostClass);
	}

	public <T> QueryInfoFieldInfo getFieldInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<T, ?> fieldAttribute) {
		Class<T> attributeHost = getAttributeHostType(fieldAttribute);
		return getFieldInfo(entityContextRegistry, attributeHost, fieldAttribute);
	}

	public <T> QueryInfoFieldInfo getFieldInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> attributeHost,
			Attribute<? super T, ?> fieldAttribute) {
		QueryInfoAttributeContext attributeContext = getAttributeContext(entityContextRegistry,
				attributeHost);
		return getFieldInfo(attributeContext, fieldAttribute);
	}

	public QueryInfoFieldInfo getFieldInfo(QueryInfoAttributeContext attributeContext,
			Attribute<?, ?> jpaAttribute) {
		Map<String, QueryInfoFieldInfo> fieldInfos = attributeContext.getFields();
		return getAttributeInfo(fieldInfos, jpaAttribute);
	}

	public QueryInfoFieldInfo getFieldInfo(QueryInfoAttributeContext attributeContext,
			String jpaAttributeName) {
		Map<String, QueryInfoFieldInfo> fieldInfos = attributeContext.getFields();
		return getAttributeInfo(fieldInfos, jpaAttributeName);
	}

	public <T> String getJoinAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<T, ?> joinAttribute) {
		Class<T> attributeHost = getAttributeHostType(joinAttribute);
		return getJoinAttributeName(entityContextRegistry, attributeHost, joinAttribute);
	}

	public <T> String getJoinAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> attributeHost,
			Attribute<? super T, ?> joinAttribute) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = joinAttribute.getName();
		} else {
			QueryInfoJoinInfo joinInfo = getJoinInfo(entityContextRegistry,
					attributeHost,
					joinAttribute);
			result = joinInfo.getName();
		}

		return result;
	}

	public <Z, X> Join<Z, X> getJoinForAttributePath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<?, ?> jpaContext,
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

	public <T> QueryInfoJoinInfo getJoinInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<T, ?> joinAttribute) {
		Class<T> attributeHost = getAttributeHostType(joinAttribute);
		return getJoinInfo(entityContextRegistry, attributeHost, joinAttribute);
	}

	public <T> QueryInfoJoinInfo getJoinInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Class<T> attributeHost,
			Attribute<? super T, ?> joinAttribute) {
		QueryInfoAttributeContext attributeContext = getAttributeContext(entityContextRegistry,
				attributeHost);
		return getJoinInfo(attributeContext, joinAttribute);
	}

	public QueryInfoJoinInfo getJoinInfo(QueryInfoAttributeContext attributeContext,
			Attribute<?, ?> jpaAttribute) {
		Map<String, QueryInfoJoinInfo> joinInfos = attributeContext.getJoins();
		return getAttributeInfo(joinInfos, jpaAttribute);
	}

	public QueryInfoJoinInfo getJoinInfo(QueryInfoAttributeContext attributeContext,
			String jpaAttributeName) {
		Map<String, QueryInfoJoinInfo> joinInfos = attributeContext.getJoins();
		return getAttributeInfo(joinInfos, jpaAttributeName);
	}

}
