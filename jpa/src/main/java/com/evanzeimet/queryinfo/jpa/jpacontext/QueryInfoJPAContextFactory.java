package com.evanzeimet.queryinfo.jpa.jpacontext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContext;

public interface QueryInfoJPAContextFactory<RootEntity> {

	<CriteriaQueryResultType> QueryInfoJPAContext<RootEntity> createJpaContext(CriteriaBuilder criteriaBuilder,
			QueryInfoBeanContext<RootEntity, ?, ?> beanContext,
			CriteriaQuery<CriteriaQueryResultType> criteriaQuery);

}
