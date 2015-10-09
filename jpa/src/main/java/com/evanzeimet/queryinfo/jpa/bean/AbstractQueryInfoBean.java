package com.evanzeimet.queryinfo.jpa.bean;

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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.QueryInfoEntityManager;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public abstract class AbstractQueryInfoBean<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	protected static final int DEFAULT_PAGE_INDEX = 0;
	protected static final int DEFAULT_MAX_RESULTS = 20;

	protected QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> beanContext;
	protected CriteriaBuilder criteriaBuilder;

	@Inject
	@QueryInfoEntityManager
	protected EntityManager entityManager;

	public AbstractQueryInfoBean(QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> context) {
		this.beanContext = context;
	}

	public Long count(QueryInfo queryInfo) throws QueryInfoException {
		Boolean useDistinctSelections = beanContext.getUseDistinctSelections();

		if (useDistinctSelections) {
			String message = "Distinct selections cannot be used with count queries because the count result will always be distinct";
			throw new QueryInfoException(message);
		}

		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		QueryInfoJPAContext<RootEntity> jpaContext = createJpaContext(criteriaQuery);
		setQueryPredicates(jpaContext, queryInfo);

		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

		return typedQuery.getSingleResult();
	}

	protected QueryInfoJPAContext<RootEntity> createJpaContext(CriteriaQuery<?> criteriaQuery) {
		QueryInfoJPAContextFactory<RootEntity> jpaContextFactory = beanContext.getJpaContextFactory();
		return jpaContextFactory.createJpaContext(criteriaBuilder,
				beanContext,
				criteriaQuery);
	}

	@PostConstruct
	protected void postConstruct() {
		criteriaBuilder = entityManager.getCriteriaBuilder();
	}

	public List<QueryInfoResult> query(QueryInfo queryInfo) throws QueryInfoException {
		// TODO queryinfo coalesce parts?

		Class<CriteriaQueryResult> resultClass = beanContext.getCriteriaQueryResultClass();
		CriteriaQuery<CriteriaQueryResult> criteriaQuery = criteriaBuilder.createQuery(resultClass);

		Boolean distinct = beanContext.getUseDistinctSelections();
		criteriaQuery.distinct(distinct);

		QueryInfoJPAContext<RootEntity> jpaContext = createJpaContext(criteriaQuery);

		setQuerySelections(jpaContext, queryInfo);
		setQueryPredicates(jpaContext, queryInfo);
		setQueryOrders(jpaContext, queryInfo);

		TypedQuery<CriteriaQueryResult> typedQuery = entityManager.createQuery(criteriaQuery);

		setPaginationInfo(typedQuery, queryInfo);

		List<CriteriaQueryResult> criteriaQueryResults = typedQuery.getResultList();

		QueryInfoResultConverter<CriteriaQueryResult, QueryInfoResult> resultConveter = beanContext.getResultConverter();
		return resultConveter.convert(criteriaQueryResults);
	}

	public PaginatedResult<QueryInfoResult> queryForPaginatedResult(QueryInfo queryInfo)
			throws QueryInfoException {
		PaginatedResult<QueryInfoResult> result = new DefaultPaginatedResult<>();

		Long totalCount = count(queryInfo);
		result.setTotalCount(totalCount);

		if (totalCount > 0) {
			List<QueryInfoResult> pageResults = query(queryInfo);
			result.setPageResults(pageResults);
		}

		return result;
	}

	protected void setPaginationInfo(TypedQuery<?> typedQuery,
			QueryInfo queryInfo) {
		PaginationInfo paginationInfo = queryInfo.getPaginationInfo();

		int firstResult;
		int maxResults;

		if (paginationInfo == null) {
			firstResult = DEFAULT_PAGE_INDEX;
			maxResults = DEFAULT_MAX_RESULTS;
		} else {
			Integer pageIndex = paginationInfo.getPageIndex();
			Integer pageSize = paginationInfo.getPageSize();

			if (pageIndex == null) {
				pageIndex = DEFAULT_PAGE_INDEX;
			}

			if (pageSize == null) {
				pageSize = DEFAULT_MAX_RESULTS;
			}

			firstResult = (pageIndex * pageSize);
			maxResults = pageSize;
		}

		typedQuery.setFirstResult(firstResult);
		typedQuery.setMaxResults(maxResults);
	}

	protected void setQueryOrders(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		QueryInfoOrderFactory<RootEntity> orderFactory = beanContext.getOrderFactory();
		orderFactory.createOrders(jpaContext, queryInfo);
	}

	protected void setQueryPredicates(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		QueryInfoPredicateFactory<RootEntity> predicateFactory = beanContext.getPredicateFactory();
		Predicate[] predicates = predicateFactory.createPredicates(jpaContext, queryInfo);

		CriteriaQuery<?> criteriaQuery = jpaContext.getCriteriaQuery();

		criteriaQuery.where(predicates);
	}

	protected void setQuerySelections(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		QueryInfoSelectionSetter<RootEntity> selectionSetter = beanContext.getSelectionSetter();
		selectionSetter.setSelection(jpaContext, queryInfo);
	}
}
