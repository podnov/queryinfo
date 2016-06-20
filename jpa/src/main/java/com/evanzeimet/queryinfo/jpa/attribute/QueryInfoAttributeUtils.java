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

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class QueryInfoAttributeUtils {

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

	protected QueryInfoAttributeContext getAttributeDeclaringTypeAttributeContext(
			QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<?, ?> attribute) {
		Class<?> declaringTypeClass = attribute.getDeclaringType().getJavaType();
		QueryInfoEntityContext<?> entityContext = entityContextRegistry.getContext(declaringTypeClass);
		return entityContext.getAttributeContext();
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

	public String getFieldAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<?, ?> fieldAttribute) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = fieldAttribute.getName();
		} else {
			QueryInfoFieldInfo fieldInfo = getFieldInfo(entityContextRegistry,
					fieldAttribute);
			result = fieldInfo.getName();
		}

		return result;
	}

	public QueryInfoFieldInfo getFieldInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<?, ?> fieldAttribute) {
		QueryInfoAttributeContext attributeContext = getAttributeDeclaringTypeAttributeContext(entityContextRegistry,
				fieldAttribute);
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

	public String getJoinAttributeName(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<?, ?> joinAttribute) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = joinAttribute.getName();
		} else {
			QueryInfoJoinInfo joinInfo = getJoinInfo(entityContextRegistry, joinAttribute);
			result = joinInfo.getName();
		}

		return result;
	}

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

	public QueryInfoJoinInfo getJoinInfo(QueryInfoEntityContextRegistry entityContextRegistry,
			Attribute<?, ?> joinAttribute) {
		QueryInfoAttributeContext attributeContext = getAttributeDeclaringTypeAttributeContext(entityContextRegistry,
				joinAttribute);
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
