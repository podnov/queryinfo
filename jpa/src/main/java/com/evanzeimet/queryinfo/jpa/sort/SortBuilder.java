package com.evanzeimet.queryinfo.jpa.sort;

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

import static com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeUtils.ENTITY_CONTEXT_REGISTRY_NOT_USED;

import javax.persistence.metamodel.Attribute;

import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeUtils;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortDirection;

public class SortBuilder extends com.evanzeimet.queryinfo.sort.SortBuilder {

	private static final QueryInfoAttributeUtils attributeUtils = new QueryInfoAttributeUtils();

	private final QueryInfoEntityContextRegistry entityContextRegistry;

	public SortBuilder(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;
	}

	@Override
	public SortBuilder builderReferenceInstance(Sort builderReferenceInstance) {
		super.builderReferenceInstance(builderReferenceInstance);
		return this;
	}

	public static SortBuilder create() {
		return create(ENTITY_CONTEXT_REGISTRY_NOT_USED);
	}

	public static SortBuilder create(QueryInfoEntityContextRegistry entityContextRegistry) {
		return new SortBuilder(entityContextRegistry);
	}

	@Override
	public SortBuilder direction(SortDirection direction) {
		super.direction(direction);
		return this;
	}

	@Override
	public SortBuilder direction(String direction) {
		super.direction(direction);
		return this;
	}

	public SortBuilder fieldName(Attribute<?, ?> attribute) {
		String name = attributeUtils.getFieldAttributeName(entityContextRegistry, attribute);
		return fieldName(name);
	}

	protected <T> SortBuilder fieldName(Class<T> attributeHost,
			Attribute<? super T, ?> attribute) {
		String name = attributeUtils.getFieldAttributeName(entityContextRegistry,
				attributeHost,
				attribute);
		return fieldName(name);
	}

	public <T> AttributeHostBuilder<T> fieldNameHost(Class<T> attributeHost) {
		return new AttributeHostBuilder<T>(this, attributeHost);
	}

	@Override
	public SortBuilder fieldName(String fieldName) {
		super.fieldName(fieldName);
		return this;
	}

	public static class AttributeHostBuilder<T> {

		private final Class<T> attributeHost;
		private final SortBuilder sort;

		protected AttributeHostBuilder(SortBuilder sortBuilder, Class<T> attributeHost) {
			this.sort = sortBuilder;
			this.attributeHost = attributeHost;
		}

		public SortBuilder fieldName(Attribute<? super T, ?> leftHandSide) {
			return sort.fieldName(attributeHost, leftHandSide);
		}

	}
}
