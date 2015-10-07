package com.evanzeimet.queryinfo.jpa;

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
import com.evanzeimet.queryinfo.jpa.from.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.PredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoOriginalResultTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public abstract class AbstractCriteriaQueryBean<RootEntity, InitialResultType, FinalResultType> {

	protected static final int DEFAULT_PAGE_INDEX = 0;
	protected static final int DEFAULT_MAX_RESULTS = 20;

	protected CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType> beanContext;
	protected CriteriaBuilder criteriaBuilder;

	@Inject
	@QueryInfoEntityManager
	protected EntityManager entityManager;


	public AbstractCriteriaQueryBean(CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType> context) {
		this.beanContext = context;
	}

	/**
	 * TODO: select distinct count(*) isn't going to work.
	 *
	 * Should we:
	 *
	 * select count(select distinct ... from real query results)?
	 **/
	public Long count(QueryInfo queryInfo) throws QueryInfoException {
		Boolean useDistinctSelections = beanContext.getUseDistinctSelections();

		if (useDistinctSelections) {
			String message = "Distinct selections cannot be used with count queries because the count result will always be distinct";
			throw new QueryInfoException(message);
		}

		// TODO queryinfo coalesce parts?
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		QueryInfoJPAContext<RootEntity> jpaContext = createJpaContext(criteriaQuery);
		setQueryPredicates(criteriaQuery, jpaContext, queryInfo);

		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

		return typedQuery.getSingleResult();

	}

	protected QueryInfoJPAContext<RootEntity> createJpaContext(CriteriaQuery<?> criteriaQuery) {
		QueryInfoJPAContextFactory<RootEntity> jpaContextFactory = beanContext.getJpaContextFactory();
		return jpaContextFactory.createJpaContext(criteriaBuilder, beanContext, criteriaQuery);
	}

	@PostConstruct
	protected void postConstruct() {
		criteriaBuilder = entityManager.getCriteriaBuilder();
	}

	public List<FinalResultType> query(QueryInfo queryInfo) throws QueryInfoException {
		Class<InitialResultType> InitialResultTypeClass = beanContext.getInitialResultTypeClass();
		CriteriaQuery<InitialResultType> criteriaQuery = criteriaBuilder.createQuery(InitialResultTypeClass);

		Boolean distinct = beanContext.getUseDistinctSelections();
		criteriaQuery.distinct(distinct);

		QueryInfoJPAContext<RootEntity> jpaContext = createJpaContext(criteriaQuery);

		setQuerySelections(criteriaQuery, jpaContext, queryInfo);
		setQueryPredicates(criteriaQuery, jpaContext, queryInfo);
		setQueryOrders(criteriaQuery, jpaContext, queryInfo);

		TypedQuery<InitialResultType> typedQuery = entityManager.createQuery(criteriaQuery);

		setPaginationInfo(typedQuery, queryInfo);

		List<InitialResultType> initialResults = typedQuery.getResultList();

		return transformInitialResults(initialResults);
	}

	protected void setPaginationInfo(TypedQuery<InitialResultType> typedQuery,
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
				pageIndex = 0;
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

	protected void setQueryOrders(CriteriaQuery<InitialResultType> criteriaQuery,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		QueryInfoOrderFactory<RootEntity> orderFactory = beanContext.getOrderFactory();
		orderFactory.createOrders(jpaContext, queryInfo);
	}

	protected void setQueryPredicates(CriteriaQuery<?> criteriaQuery,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		PredicateFactory<RootEntity> predicateFactory = beanContext.getPredicateFactory();
		Predicate[] predicates = predicateFactory.createPredicates(jpaContext, queryInfo);

		criteriaQuery.where(predicates);
	}

	protected void setQuerySelections(CriteriaQuery<InitialResultType> criteriaQuery,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) {
		QueryInfoSelectionSetter<RootEntity> selectionSetter = beanContext.getSelectionSetter();
		selectionSetter.setSelection(jpaContext, queryInfo);
	}
	
	protected List<FinalResultType> transformInitialResults(List<InitialResultType> originalResults) {
		QueryInfoOriginalResultTransformer<InitialResultType, FinalResultType> originalResultTransormer = beanContext.getOriginalResultTransformer();
		return originalResultTransormer.transform(originalResults);
	}
}
