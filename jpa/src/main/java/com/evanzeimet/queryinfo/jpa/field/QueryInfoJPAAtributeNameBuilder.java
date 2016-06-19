package com.evanzeimet.queryinfo.jpa.field;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeUtils;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;

public class QueryInfoJPAAtributeNameBuilder<T> {

	private static final QueryInfoEntityContextRegistry ENTITY_CONTEXT_REGISTRY_NOT_USED = null;

	private static final QueryInfoAttributeUtils attributeUtils = new QueryInfoAttributeUtils();

	private final QueryInfoEntityContextRegistry entityContextRegistry;
	private final List<Attribute<?, ?>> attributes;

	public QueryInfoJPAAtributeNameBuilder(QueryInfoEntityContextRegistry entityContextRegistry,
			PluralAttribute<?, ?, T> attribute) {
		this.entityContextRegistry = entityContextRegistry;
		this.attributes = asAttributeList(attribute);
	}

	public QueryInfoJPAAtributeNameBuilder(QueryInfoEntityContextRegistry entityContextRegistry,
			SingularAttribute<?, T> attribute) {
		this.entityContextRegistry = entityContextRegistry;
		this.attributes = asAttributeList(attribute);
	}

	protected QueryInfoJPAAtributeNameBuilder(QueryInfoEntityContextRegistry entityContextRegistry,
			List<Attribute<?, ?>> parents,
			PluralAttribute<?, ?, T> attribute) {
		this.entityContextRegistry = entityContextRegistry;
		this.attributes = asAttributeList(parents, attribute);
	}

	protected QueryInfoJPAAtributeNameBuilder(QueryInfoEntityContextRegistry entityContextRegistry,
			List<Attribute<?, ?>> parents,
			SingularAttribute<?, T> attribute) {
		this.entityContextRegistry = entityContextRegistry;
		this.attributes = asAttributeList(parents, attribute);
	}

	public <NT> QueryInfoJPAAtributeNameBuilder<NT> add(PluralAttribute<? super T, ?, NT> attribute) {
		return new QueryInfoJPAAtributeNameBuilder<NT>(entityContextRegistry, attributes, attribute);
	}

	public <NT> QueryInfoJPAAtributeNameBuilder<NT> add(SingularAttribute<? super T, NT> attribute) {
		return new QueryInfoJPAAtributeNameBuilder<NT>(entityContextRegistry, attributes, attribute);
	}

	protected List<Attribute<?, ?>> asAttributeList(Attribute<?, ?> attribute) {
		List<Attribute<?, ?>> parents = Collections.emptyList();
		return asAttributeList(parents, attribute);
	}

	protected List<Attribute<?, ?>> asAttributeList(List<Attribute<?, ?>> parents, Attribute<?, ?> attribute) {
		int attributeCount = (parents.size() + 1);
		List<Attribute<?, ?>> result = new ArrayList<>(attributeCount);

		result.addAll(parents);
		result.add(attribute);

		return result;
	}

	public List<String> buildList() {
		int attributeCount = attributes.size();
		List<String> result = new ArrayList<>(attributeCount);

		int joinCount = (attributeCount - 1);

		for (int attributeIndex = 0; attributeIndex < joinCount; attributeIndex++)  {
			Attribute<?, ?> joinAttribute = attributes.get(attributeIndex);
			String joinAttributeName = getJoinAttributeName(joinAttribute);
			result.add(joinAttributeName);
		}

		Attribute<?, ?> fieldAttribute = attributes.get(attributeCount -1);
		String fieldAttributeName = getFieldAttributeName(fieldAttribute);
		result.add(fieldAttributeName);

		return result;
	}

	public String buildString() {
		List<String> attributeNames = buildList();
		return StringUtils.join(attributeNames, ".");
	}

	public static RootBuilder create() {
		return create(ENTITY_CONTEXT_REGISTRY_NOT_USED);
	}

	public static RootBuilder create(QueryInfoEntityContextRegistry entityContextRegistry) {
		return new RootBuilder(entityContextRegistry);
	}

	protected String getFieldAttributeName(Attribute<?, ?> fieldAttribute) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = fieldAttribute.getName();
		} else {
			QueryInfoFieldInfo fieldInfo = attributeUtils.getFieldInfo(entityContextRegistry, fieldAttribute);
			result = fieldInfo.getName();
		}

		return result;
	}

	protected String getJoinAttributeName(Attribute<?, ?> joinAttribute) {
		String result;

		if (entityContextRegistry == ENTITY_CONTEXT_REGISTRY_NOT_USED) {
			result = joinAttribute.getName();
		} else {
			QueryInfoJoinInfo joinInfo = attributeUtils.getJoinInfo(entityContextRegistry, joinAttribute);
			result = joinInfo.getName();
		}

		return result;
	}

	public static class RootBuilder {

		private QueryInfoEntityContextRegistry entityContextRegistry;

		private RootBuilder(QueryInfoEntityContextRegistry entityContextRegistry) {
			this.entityContextRegistry = entityContextRegistry;
		}

		public <NT> QueryInfoJPAAtributeNameBuilder<NT> add(PluralAttribute<?, ?, NT> attribute) {
			return new QueryInfoJPAAtributeNameBuilder<NT>(entityContextRegistry, attribute);
		}

		public <NT> QueryInfoJPAAtributeNameBuilder<NT> add(SingularAttribute<?, NT> attribute) {
			return new QueryInfoJPAAtributeNameBuilder<NT>(entityContextRegistry, attribute);
		}
	}
}
