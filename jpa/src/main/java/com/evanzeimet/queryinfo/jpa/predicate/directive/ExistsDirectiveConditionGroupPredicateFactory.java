package com.evanzeimet.queryinfo.jpa.predicate.directive;

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

import static com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose.PREDICATE;
import static com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose.SUBQUERY_ROOT;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupBuilder;
import com.evanzeimet.queryinfo.jpa.condition.directive.ExistsConditionGroupDirectiveConfig;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContexts;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathUtils;
import com.evanzeimet.queryinfo.jpa.predicate.DefaultQueryInfoPredicateFactory;

/**
 * This class creates predicates for exists subqueries.
 */
public class ExistsDirectiveConditionGroupPredicateFactory<RootEntity> {

	private QueryInfoUtils queryInfoUtils;
	private QueryInfoPathUtils queryInfoPathUtils;

	public ExistsDirectiveConditionGroupPredicateFactory() {
		queryInfoUtils = new QueryInfoUtils();
		queryInfoPathUtils = new QueryInfoPathUtils();
	}

	protected <SubqueryRoot> QueryInfoJPAContext<SubqueryRoot, Subquery<SubqueryRoot>> createJpaContextForSubquery(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity, ?> enclosingQueryJpaContext,
			ExistsConditionGroupDirectiveConfig existsConfig) throws QueryInfoException {
		CriteriaBuilder criteriaBuilder = enclosingQueryJpaContext.getCriteriaBuilder();
		QueryInfoJPAContextFactory jpaContextFactory = enclosingQueryJpaContext.getFactory();
		AbstractQuery<?> enclosingQuery = enclosingQueryJpaContext.getCriteriaQuery();

		String subqueryRootAttributePath = existsConfig.getSubqueryRootAttributePath();
		Class<SubqueryRoot> subqueryRootEntityClass = getSubqueryRootEntityClass(entityContextRegistry,
				enclosingQueryJpaContext,
				subqueryRootAttributePath);

		return jpaContextFactory.createSubqueryJpaContext(criteriaBuilder,
						subqueryRootEntityClass,
						enclosingQuery);
	}

	protected <PathEntity> Expression<Object> createPathForJpaContext(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<PathEntity, ?> jpaContext,
			String attributePath) throws QueryInfoException {
		Root<PathEntity> root = jpaContext.getRoot();
		QueryInfoEntityContext<PathEntity> entityContgext = entityContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory<PathEntity> pathFactory = entityContgext.getPathFactory();

		return pathFactory.getPathForAttribute(entityContextRegistry,
				jpaContext,
				root,
				attributePath,
				PREDICATE);
	}

	public <SubqueryRoot> Predicate createPredicate(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<RootEntity, ?> currentJpaContext,
			ConditionGroup existsConditionGroup) throws QueryInfoException {
		CriteriaBuilder criteriaBuilder = currentJpaContext.getCriteriaBuilder();

		ExistsConditionGroupDirectiveConfig existsConfig = deserializeDirectiveConfig(existsConditionGroup);
		QueryInfoJPAContext<SubqueryRoot, Subquery<SubqueryRoot>> subqueryJpaContext = createJpaContextForSubquery(entityContextRegistry,
				currentJpaContext,
				existsConfig);

		String subqueryName = existsConfig.getSubqueryName();

		if (subqueryName != null) {
			jpaContexts.putNamedSubqueryContext(subqueryName, subqueryJpaContext);
		}

		Subquery<SubqueryRoot> subquery = createSubquery(subqueryJpaContext);

		Predicate[] predicates = createPredicatesForExistsConditionGroup(entityContextRegistry,
				jpaContexts,
				subqueryJpaContext,
				existsConditionGroup);
		subquery.where(predicates);

		return criteriaBuilder.exists(subquery);
	}

	protected <SubqueryRoot> Predicate[] createPredicatesForExistsConditionGroup(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContexts<?, ?> jpaContexts,
			QueryInfoJPAContext<SubqueryRoot, ?> currentJpaContext,
			ConditionGroup existsConditionGroup) throws QueryInfoException {
		DefaultQueryInfoPredicateFactory<SubqueryRoot> subqueryPredicateFactory = new DefaultQueryInfoPredicateFactory<>();

		ConditionGroup subqueryConditionGroup = createConditionGroupWithoutDirective(existsConditionGroup);

		return subqueryPredicateFactory.createPredicates(entityContextRegistry,
				jpaContexts,
				currentJpaContext,
				subqueryConditionGroup);
	}

	protected ConditionGroup createConditionGroupWithoutDirective(ConditionGroup conditionGroup) {
		return ConditionGroupBuilder.create()
				.builderReferenceInstance(conditionGroup)
				.directive(null)
				.directiveConfig(null)
				.build();
	}

	protected <SubqueryRoot> Subquery<SubqueryRoot> createSubquery(QueryInfoJPAContext<SubqueryRoot, Subquery<SubqueryRoot>> subqueryJpaContext) {
		Subquery<SubqueryRoot> result = subqueryJpaContext.getCriteriaQuery();
		Root<SubqueryRoot> subqueryRoot = subqueryJpaContext.getRoot();

		result.select(subqueryRoot);

		return result;
	}

	protected ExistsConditionGroupDirectiveConfig deserializeDirectiveConfig(ConditionGroup conditionGroup)
			throws QueryInfoException {
		String rawDirectiveConfig = conditionGroup.getDirectiveConfig();
		return queryInfoUtils.objectify(rawDirectiveConfig, ExistsConditionGroupDirectiveConfig.class);
	}

	protected <SubqueryRoot> Class<SubqueryRoot> getSubqueryRootEntityClass(
			QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<RootEntity, ?> enclosingQueryJpaContext,
			String subqueryRootAttributePath) throws QueryInfoException {
		Root<RootEntity> enclosingQueryRoot = enclosingQueryJpaContext.getRoot();
		QueryInfoEntityContext<RootEntity> enclosingQueryRootEntityContext = entityContextRegistry.getContextForRoot(enclosingQueryJpaContext);
		QueryInfoPathFactory<RootEntity> enclosingQueryRootEntityPathFactory = enclosingQueryRootEntityContext.getPathFactory();

		Path<SubqueryRoot> path = enclosingQueryRootEntityPathFactory.getPathForAttribute(entityContextRegistry,
				enclosingQueryJpaContext,
				enclosingQueryRoot,
				subqueryRootAttributePath,
				SUBQUERY_ROOT);

		return queryInfoPathUtils.getBindableJavaType(path);
	}
}
