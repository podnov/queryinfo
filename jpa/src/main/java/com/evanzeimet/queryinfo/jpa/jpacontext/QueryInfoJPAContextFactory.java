package com.evanzeimet.queryinfo.jpa.jpacontext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.evanzeimet.queryinfo.jpa.beancontext.CriteriaQueryBeanContext;

public interface QueryInfoJPAContextFactory<RootEntity> {

	<CriteriaQueryResultType> QueryInfoJPAContext<RootEntity> createJpaContext(CriteriaBuilder criteriaBuilder,
			CriteriaQueryBeanContext<RootEntity, ?, ?> beanContext,
			CriteriaQuery<CriteriaQueryResultType> criteriaQuery);

}
