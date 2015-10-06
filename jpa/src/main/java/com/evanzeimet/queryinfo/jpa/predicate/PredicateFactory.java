package com.evanzeimet.queryinfo.jpa.predicate;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoFromContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public interface PredicateFactory<RootEntity> {

	CriteriaBuilder getCriteriaBuilder();

	void setCriteriaBuilder(CriteriaBuilder criteriaBuilder);

	QueryInfoPathFactory<RootEntity> getPathFactory();

	void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory);

	Predicate[] createPredicates(AbstractQuery<?> query,
			Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			QueryInfo queryInfo);

}
