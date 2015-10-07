package com.evanzeimet.queryinfo.jpa.from;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.evanzeimet.queryinfo.jpa.CriteriaQueryBeanContext;

public interface QueryInfoJPAContextFactory<RootEntity> {

	<CriteriaQueryResultType> QueryInfoJPAContext<RootEntity> createJpaContext(CriteriaBuilder criteriaBuilder,
			CriteriaQueryBeanContext<RootEntity, ?, ?> beanContext,
			CriteriaQuery<CriteriaQueryResultType> criteriaQuery);

}
