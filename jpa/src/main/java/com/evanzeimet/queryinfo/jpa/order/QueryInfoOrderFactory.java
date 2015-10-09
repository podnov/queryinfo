package com.evanzeimet.queryinfo.jpa.order;

import java.util.List;

import javax.persistence.criteria.Order;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public interface QueryInfoOrderFactory<RootEntity> {

	List<Order> createOrders(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException;

}
