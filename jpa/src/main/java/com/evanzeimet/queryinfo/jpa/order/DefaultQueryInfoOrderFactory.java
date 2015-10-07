package com.evanzeimet.queryinfo.jpa.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortDirection;

public class DefaultQueryInfoOrderFactory<RootEntity> implements QueryInfoOrderFactory<RootEntity> {

	private CriteriaBuilder criteriaBuilder;
	private QueryInfoPathFactory<RootEntity> pathFactory;

	public DefaultQueryInfoOrderFactory() {

	}

	@Override
	public QueryInfoPathFactory<RootEntity> getPathFactory() {
		return pathFactory;
	}

	@Override
	public void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory) {
		this.pathFactory = pathFactory;
	}

	protected Order createOrder(QueryInfoJPAContext<RootEntity> jpaContext,
			Sort sort) throws QueryInfoException {
		Order result;

		String fieldName = sort.getFieldName();

		Expression<?> path = getPathForField(jpaContext, fieldName);

		SortDirection direction = SortDirection.fromSort(sort);

		if (SortDirection.DESC.equals(direction)) {
			result = criteriaBuilder.desc(path);
		} else {
			result = criteriaBuilder.asc(path);
		}

		return result;
	}

	@Override
	public List<Order> createOrders(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<Sort> sorts = queryInfo.getSorts();
		return createOrders(jpaContext, sorts);
	}

	protected List<Order> createOrders(QueryInfoJPAContext<RootEntity> jpaContext,
			List<Sort> sorts) throws QueryInfoException {
		List<Order> result;

		if (sorts == null) {
			result = new ArrayList<>(0);
		} else {
			int sortCount = sorts.size();

			result = new ArrayList<>(sortCount);

			for (Sort sort : sorts) {
				Order order = createOrder(jpaContext, sort);
				result.add(order);
			}
		}

		return result;
	}

	protected Expression<?> getPathForField(QueryInfoJPAContext<RootEntity> jpaContext,
			String fieldName) throws QueryInfoException {
		Root<RootEntity> root = jpaContext.getRoot();
		return pathFactory.getPathForField(root,
				jpaContext,
				fieldName,
				QueryInfoFieldPurpose.ORDER);
	}
}
