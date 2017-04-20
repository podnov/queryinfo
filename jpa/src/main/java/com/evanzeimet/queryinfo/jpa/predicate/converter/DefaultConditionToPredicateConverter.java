package com.evanzeimet.queryinfo.jpa.predicate.converter;

import static com.evanzeimet.queryinfo.condition.OperandType.ATTRIBUTE_PATH;
import static com.evanzeimet.queryinfo.condition.OperandType.LITERAL;
import static com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose.PREDICATE;

import javax.persistence.criteria.AbstractQuery;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.condition.OperandType;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContexts;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoFieldValueParser;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateUtils;
import com.fasterxml.jackson.databind.JsonNode;

public class DefaultConditionToPredicateConverter<RootEntity>
		implements ConditionToPredicateConverter<RootEntity> {

	private final QueryInfoFieldValueParser fieldValueParser = new QueryInfoFieldValueParser();
	private final QueryInfoPredicateUtils predicateUtils = new QueryInfoPredicateUtils();
	private final QueryInfoUtils queryInfoUtils;

	public DefaultConditionToPredicateConverter() {
		queryInfoUtils = new QueryInfoUtils();
	}

	@Override
	public Predicate convert(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition) throws QueryInfoException {
		Predicate result;
		ConditionOperator conditionOperator = parseConditionOperator(condition);
		boolean hasInvalidConditionOperator = (conditionOperator == null);

		if (hasInvalidConditionOperator) {
			result = null;
		} else {
			OperandType leftHandSideType = getLeftHandSideType(condition);
			OperandType rightHandSideType = getRightHandSideType(condition);

			boolean conditionHasLiteral = (LITERAL.equals(leftHandSideType) || LITERAL.equals(rightHandSideType));
			Path<?> literalReferenceAttributePath;

			if (conditionHasLiteral) {
				literalReferenceAttributePath = getLiteralReferenceAttributePath(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition);
			} else {
				literalReferenceAttributePath = null;
			}

			Expression<?> leftHandSide = createLeftHandSideExpression(entityContextRegistry,
					jpaContexts,
					currentJpaContext,
					condition,
					literalReferenceAttributePath);

			Expression<?> rightHandSide = createRightHandSideExpression(entityContextRegistry,
					jpaContexts,
					currentJpaContext,
					condition,
					literalReferenceAttributePath);

			boolean hasInvalidOperand = (leftHandSide == null || rightHandSide == null);

			if (hasInvalidOperand) {
				result = null;
			} else {
				CriteriaBuilder criteriaBuilder = currentJpaContext.getCriteriaBuilder();

				result = predicateUtils.createPredicate(criteriaBuilder,
						leftHandSide,
						conditionOperator,
						rightHandSide);
			}
		}

		return result;
	}

	protected Expression<?> createLiteralExpression(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			JsonNode jsonNode,
			ConditionOperator conditionOperator,
			Expression<?> literalReferenceAttributePath) throws QueryInfoException {
		Object literal;

		if (literalReferenceAttributePath == null) {
			literal = fieldValueParser.parseLiteral(jsonNode);
		} else {
			literal = fieldValueParser.parseLiteralForExpression(literalReferenceAttributePath,
					conditionOperator,
					jsonNode);
		}

		CriteriaBuilder criteriaBuilder = currentJpaContext.getCriteriaBuilder();

		return criteriaBuilder.literal(literal);
	}

	protected Expression<?> createLeftHandSideExpression(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition,
			Path<?> literalReferenceAttributePath) throws QueryInfoException {
		Expression<?> result = null;
		OperandType leftHandSideType = getLeftHandSideType(condition);

		switch (leftHandSideType) {
			case ATTRIBUTE_PATH:
				result = getLeftHandSidePathForAttribute(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition);
				break;

			case LITERAL:
				result = createLeftHandSideLiteralExpression(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition,
						literalReferenceAttributePath);
				break;
		}

		return result;
	}

	protected Expression<?> createLeftHandSideLiteralExpression(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition,
			Expression<?> literalReferenceAttributePath) throws QueryInfoException {
		JsonNode leftHandSide = condition.getLeftHandSide();
		ConditionOperator conditionOperator = parseConditionOperator(condition);
		return createLiteralExpression(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				leftHandSide,
				conditionOperator,
				literalReferenceAttributePath);
	}

	protected Expression<?> createRightHandSideExpression(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition,
			Expression<?> literalReferenceAttributePath) throws QueryInfoException {
		Expression<?> result = null;
		OperandType rightHandSideType = getRightHandSideType(condition);

		switch (rightHandSideType) {
			case ATTRIBUTE_PATH:
				result = getRightHandSidePathForAttribute(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition);
				break;

			case LITERAL:
				result = createRightHandSideLiteralExpression(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition,
						literalReferenceAttributePath);
				break;
		}

		return result;
	}

	protected Expression<?> createRightHandSideLiteralExpression(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition,
			Expression<?> literalReferenceAttributePath) throws QueryInfoException {
		JsonNode rightHandSide = condition.getRightHandSide();
		ConditionOperator conditionOperator = parseConditionOperator(condition);
		return createLiteralExpression(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				rightHandSide,
				conditionOperator,
				literalReferenceAttributePath);
	}

	protected Path<?> getLeftHandSidePathForAttribute(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition) throws QueryInfoException {
		JsonNode leftHandSide = condition.getLeftHandSide();
		String leftHandSideTypeConfig = condition.getLeftHandSideTypeConfig();
		return getPathForAttribute(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				leftHandSideTypeConfig,
				leftHandSide);
	}

	protected OperandType getLeftHandSideType(Condition condition) {
		OperandType leftHandSideType = condition.getLeftHandSideType();
		return queryInfoUtils.coalesce(leftHandSideType, ATTRIBUTE_PATH);
	}

	/**
	 * In order to deserialize literals properly, we need to know if they refer
	 * to a queryinfo field. If they do, determine which attribute so we know
	 * how to deserialize literals.<br>
	 * <br>
	 * Example:<br>
	 * <br>
	 * Given the following condition where "randomFieldName" is a valid
	 * queryinfo field name:
	 * 
	 * <pre>
	 * {
	 * 	"leftHandSide": "randomFieldName",
	 * 	"operator": ">",
	 * 	"rightHandSide": "2016-05-09T00:00:00"
	 * }
	 * </pre>
	 * 
	 * We need to determine the data type of the underlying jpa element to
	 * determine whether "2016-05-09T00:00:00" should be deserialized as a
	 * string or a date.
	 */
	protected Path<?> getLiteralReferenceAttributePath(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition) throws QueryInfoException {
		OperandType leftHandSideType = getLeftHandSideType(condition);
		Path<?> result = null;

		if (ATTRIBUTE_PATH.equals(leftHandSideType)) {
			result = getLeftHandSidePathForAttribute(entityContextRegistry,
					jpaContexts,
					currentJpaContext,
					condition);
		} else {
			OperandType rightHandSideType = getRightHandSideType(condition);

			if (ATTRIBUTE_PATH.equals(rightHandSideType)) {
				result = getRightHandSidePathForAttribute(entityContextRegistry,
						jpaContexts,
						currentJpaContext,
						condition);
			}
		}

		return result;
	}

	protected <ReferencedEntity> Path<?> getPathForAttribute(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			String operandTypeConfig,
			JsonNode operand) throws QueryInfoException {
		Path<?> result;
		String attributePath = queryInfoUtils.stringifyJsonNode(operand);

		if (attributePath == null) {
			result = null;
		} else {
			QueryInfoJPAContext<ReferencedEntity, AbstractQuery<?>> referenceJpaContext = getReferencedJpaContext(jpaContexts,
					currentJpaContext,
					operandTypeConfig);

			QueryInfoEntityContext<ReferencedEntity> referenceEntityContext = entityContextRegistry.getContextForRoot(referenceJpaContext);
			QueryInfoPathFactory<ReferencedEntity> referencePathFactory = referenceEntityContext.getPathFactory();
			Root<ReferencedEntity> referenceRoot = referenceJpaContext.getRoot();

			result = referencePathFactory.getPathForAttribute(entityContextRegistry,
					referenceJpaContext,
					referenceRoot,
					attributePath,
					PREDICATE);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	protected <ReferencedEntity> QueryInfoJPAContext<ReferencedEntity, AbstractQuery<?>> getReferencedJpaContext(QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			String leftHandSideTypeConfig) {
		QueryInfoJPAContext<ReferencedEntity, AbstractQuery<?>> result;

		if (leftHandSideTypeConfig == null) {
			result = (QueryInfoJPAContext<ReferencedEntity, AbstractQuery<?>>) currentJpaContext;

		} else if (QueryInfoJPAContexts.ROOT_QUERY_NAME.equals(leftHandSideTypeConfig)) {
			QueryInfoJPAContext<?, ?> rootContext = jpaContexts.getRootContext();
			result = (QueryInfoJPAContext<ReferencedEntity, AbstractQuery<?>>) rootContext;

		} else {
			result = jpaContexts.getNamedSubqueryContext(leftHandSideTypeConfig);
		}

		return result;
	}

	protected Path<?> getRightHandSidePathForAttribute(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			Condition condition) throws QueryInfoException {
		JsonNode rightHandSide = condition.getRightHandSide();
		String rightHandSideTypeConfig = condition.getRightHandSideTypeConfig();
		return getPathForAttribute(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				rightHandSideTypeConfig,
				rightHandSide);
	}

	protected OperandType getRightHandSideType(Condition condition) {
		OperandType leftHandSideType = condition.getRightHandSideType();
		return queryInfoUtils.coalesce(leftHandSideType, LITERAL);
	}

	protected ConditionOperator parseConditionOperator(Condition condition) {
		String operator = condition.getOperator();
		return ConditionOperator.fromText(operator);
	}

}
