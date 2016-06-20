package com.evanzeimet.queryinfo.jpa.condition;

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

import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeUtils;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.fasterxml.jackson.databind.JsonNode;

public class ConditionBuilder extends com.evanzeimet.queryinfo.condition.ConditionBuilder {

	private static final QueryInfoAttributeUtils attributeUtils = new QueryInfoAttributeUtils();

	private final QueryInfoEntityContextRegistry entityContextRegistry;

	public ConditionBuilder(QueryInfoEntityContextRegistry entityContextRegistry) {
		this.entityContextRegistry = entityContextRegistry;
	}

	@Override
	public ConditionBuilder builderReferenceInstance(Condition builderReferenceInstance) {
		super.builderReferenceInstance(builderReferenceInstance);
		return this;
	}

	public static ConditionBuilder create() {
		return create(ENTITY_CONTEXT_REGISTRY_NOT_USED);
	}

	public static ConditionBuilder create(QueryInfoEntityContextRegistry entityContextRegistry) {
		return new ConditionBuilder(entityContextRegistry);
	}

	public ConditionBuilder leftHandSide(Attribute<?, ?> leftHandSide) {
		String name = attributeUtils.getFieldAttributeName(entityContextRegistry,
				leftHandSide);
		return leftHandSide(name);
	}

	protected <T> ConditionBuilder leftHandSide(Class<T> attributeHost,
			Attribute<? super T, ?> leftHandSide) {
		String name = attributeUtils.getFieldAttributeName(entityContextRegistry,
				attributeHost,
				leftHandSide);
		return leftHandSide(name);
	}

	public <T> AttributeHostBuilder<T> leftHandSideHost(Class<T> attributeHost) {
		return new AttributeHostBuilder<T>(this, attributeHost);
	}

	@Override
	public ConditionBuilder leftHandSide(String leftHandSide) {
		super.leftHandSide(leftHandSide);
		return this;
	}

	@Override
	public ConditionBuilder operator(ConditionOperator operator) {
		super.operator(operator);
		return this;
	}

	@Override
	public ConditionBuilder operator(String operator) {
		super.operator(operator);
		return this;
	}

	@Override
	public ConditionBuilder rightHandSide(Object rightHandSide) {
		super.rightHandSide(rightHandSide);
		return this;
	}

	@Override
	public ConditionBuilder rightHandSide(JsonNode rightHandSide) {
		super.rightHandSide(rightHandSide);
		return this;
	}

	public static class AttributeHostBuilder<T> {

		private final Class<T> attributeHost;
		private final ConditionBuilder conditionBuilder;

		protected AttributeHostBuilder(ConditionBuilder conditionBuilder, Class<T> attributeHost) {
			this.conditionBuilder = conditionBuilder;
			this.attributeHost = attributeHost;
		}
		
		public ConditionBuilder leftHandSide(Attribute<? super T, ?> leftHandSide) {
			return conditionBuilder.leftHandSide(attributeHost, leftHandSide);
		}

	}
}
