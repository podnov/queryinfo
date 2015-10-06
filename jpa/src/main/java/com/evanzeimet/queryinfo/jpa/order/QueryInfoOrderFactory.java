package com.evanzeimet.queryinfo.jpa.order;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoFromContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public interface QueryInfoOrderFactory<RootEntity> {
	
	CriteriaBuilder getCriteriaBuilder();
	
	void setCriteriaBuilder(CriteriaBuilder criteriaBuilder);

	QueryInfoPathFactory<RootEntity> getPathFactory();
	
	void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory);
	
	List<Order> createOrders(Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			QueryInfo queryInfo) throws QueryInfoException;
}
