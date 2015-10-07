package com.evanzeimet.queryinfo.jpa.order;

import java.util.List;

import javax.persistence.criteria.Order;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public interface QueryInfoOrderFactory<RootEntity> {

	// TODO path factory regitry?
	QueryInfoPathFactory<RootEntity> getPathFactory();

	void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory);

	List<Order> createOrders(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException;
}
