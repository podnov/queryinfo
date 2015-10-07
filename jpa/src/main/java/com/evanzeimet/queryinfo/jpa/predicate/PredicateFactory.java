package com.evanzeimet.queryinfo.jpa.predicate;

import javax.persistence.criteria.Predicate;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public interface PredicateFactory<RootEntity> {

	QueryInfoPathFactory<RootEntity> getPathFactory();

	void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory);

	Predicate[] createPredicates(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException;

}
