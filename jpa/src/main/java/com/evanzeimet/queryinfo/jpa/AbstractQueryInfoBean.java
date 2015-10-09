package com.evanzeimet.queryinfo.jpa;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoResultType;
import com.evanzeimet.queryinfo.jpa.beancontext.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoTupleTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;

public abstract class AbstractQueryInfoBean<RootEntity, FinalTupleResultType> {

	protected static final int DEFAULT_PAGE_INDEX = 0;
	protected static final int DEFAULT_MAX_RESULTS = 20;

	protected QueryInfoBeanContext<RootEntity, FinalTupleResultType> beanContext;
	protected CriteriaBuilder criteriaBuilder;

	@Inject
	@QueryInfoEntityManager
	protected EntityManager entityManager;


	public AbstractQueryInfoBean(QueryInfoBeanContext<RootEntity, FinalTupleResultType> context) {
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
		setQueryPredicates(criteriaQuery, jpaContext, queryInfo);

		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

		return typedQuery.getSingleResult();
	}

	protected <ResultClass> List<ResultClass> executeQueryInfoQuery(CriteriaQuery<ResultClass> criteriaQuery,
			QueryInfo queryInfo) throws QueryInfoException {
		// TODO queryinfo coalesce parts?
		Boolean distinct = beanContext.getUseDistinctSelections();
		criteriaQuery.distinct(distinct);

		QueryInfoJPAContext<RootEntity> jpaContext = createJpaContext(criteriaQuery);

		setQuerySelections(criteriaQuery, jpaContext, queryInfo);
		setQueryPredicates(criteriaQuery, jpaContext, queryInfo);
		setQueryOrders(criteriaQuery, jpaContext, queryInfo);

		TypedQuery<ResultClass> typedQuery = entityManager.createQuery(criteriaQuery);

		setPaginationInfo(typedQuery, queryInfo);

		return typedQuery.getResultList();
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

	@SuppressWarnings("unchecked")
	public <T> List<T> query(QueryInfo queryInfo, QueryInfoResultType resultType)
			throws QueryInfoException {
		if (resultType == null) {
			String message = "Result type not specified";
			throw new QueryInfoException(message);
		}

		List<T> result = null;

		switch (resultType) {
			case FLAT:
				result = (List<T>) queryForTuples(queryInfo);
				break;

			case HIERARCHICAL:
				result = (List<T>) queryForEntities(queryInfo);
				break;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> PaginatedResult<T> queryForPaginatedResult(QueryInfo queryInfo,
			QueryInfoResultType resultType)
			throws QueryInfoException {
		if (resultType == null) {
			String message = "Result type not specified";
			throw new QueryInfoException(message);
		}

		PaginatedResult<T> result = null;

		switch (resultType) {
			case FLAT:
				result = (PaginatedResult<T>) queryForPaginatedTuples(queryInfo);
				break;
			case HIERARCHICAL:
				result = (PaginatedResult<T>) queryForPaginatedEntities(queryInfo);
				break;
		}

		return result;
	}

	public PaginatedResult<FinalTupleResultType> queryForPaginatedTuples(QueryInfo queryInfo)
			throws QueryInfoException {
		PaginatedResult<FinalTupleResultType> result = new DefaultPaginatedResult<>();

		Long totalCount = count(queryInfo);
		result.setTotalCount(totalCount);

		if (totalCount > 0) {
			List<FinalTupleResultType> pageResults = queryForTuples(queryInfo);
			result.setPageResults(pageResults);
		}

		return result;
	}

	public PaginatedResult<RootEntity> queryForPaginatedEntities(QueryInfo queryInfo)
			throws QueryInfoException {
		PaginatedResult<RootEntity> result = new DefaultPaginatedResult<>();

		Long totalCount = count(queryInfo);
		result.setTotalCount(totalCount);

		if (totalCount > 0) {
			List<RootEntity> pageResults = queryForEntities(queryInfo);
			result.setPageResults(pageResults);
		}

		return result;
	}

	public List<RootEntity> queryForEntities(QueryInfo queryInfo) throws QueryInfoException {
		Class<RootEntity> rootEntityClass = beanContext.getRootEntityClass();
		CriteriaQuery<RootEntity> criteriaQuery = criteriaBuilder.createQuery(rootEntityClass);

		return executeQueryInfoQuery(criteriaQuery, queryInfo);
	}

	public List<FinalTupleResultType> queryForTuples(QueryInfo queryInfo) throws QueryInfoException {
		CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

		List<Tuple> initialResults = executeQueryInfoQuery(criteriaQuery, queryInfo);

		return transformInitialResults(initialResults);
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

	protected void setQueryOrders(CriteriaQuery<?> criteriaQuery,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		QueryInfoOrderFactory<RootEntity> orderFactory = beanContext.getOrderFactory();
		orderFactory.createOrders(jpaContext, queryInfo);
	}

	protected void setQueryPredicates(CriteriaQuery<?> criteriaQuery,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		QueryInfoPredicateFactory<RootEntity> predicateFactory = beanContext.getPredicateFactory();
		Predicate[] predicates = predicateFactory.createPredicates(jpaContext, queryInfo);

		criteriaQuery.where(predicates);
	}

	protected void setQuerySelections(CriteriaQuery<?> criteriaQuery,
			QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) {
		QueryInfoSelectionSetter<RootEntity> selectionSetter = beanContext.getSelectionSetter();
		selectionSetter.setSelection(jpaContext, queryInfo);
	}

	protected List<FinalTupleResultType> transformInitialResults(List<Tuple> originalResults) {
		QueryInfoTupleTransformer<FinalTupleResultType> originalResultTransormer = beanContext.getTupleTransformer();
		return originalResultTransormer.transform(originalResults);
	}
}
