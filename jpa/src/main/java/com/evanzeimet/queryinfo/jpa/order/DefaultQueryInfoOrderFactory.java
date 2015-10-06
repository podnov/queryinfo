package com.evanzeimet.queryinfo.jpa.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoFromContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortDirection;

public class DefaultQueryInfoOrderFactory<RootEntity> implements QueryInfoOrderFactory<RootEntity> {

	private CriteriaBuilder criteriaBuilder;
	private QueryInfoPathFactory<RootEntity> pathFactory;

	public DefaultQueryInfoOrderFactory() {

	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	@Override
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	@Override
	public QueryInfoPathFactory<RootEntity> getPathFactory() {
		return pathFactory;
	}

	@Override
	public void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory) {
		this.pathFactory = pathFactory;
	}

	protected Order createOrder(Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			Sort sort) throws QueryInfoException {
		Order result;

		String fieldName = sort.getFieldName();

		Expression<?> path = getPathForField(root, fromContext, fieldName);

		SortDirection direction = SortDirection.fromSort(sort);

		if (SortDirection.DESC.equals(direction)) {
			result = criteriaBuilder.desc(path);
		} else {
			result = criteriaBuilder.asc(path);
		}

		return result;
	}

	@Override
	public List<Order> createOrders(Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<Sort> sorts = queryInfo.getSorts();
		return createOrders(root, fromContext, sorts);
	}

	protected List<Order> createOrders(Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			List<Sort> sorts) throws QueryInfoException {
		List<Order> result;

		if (sorts == null) {
			result = new ArrayList<>(0);
		} else {
			int sortCount = sorts.size();

			result = new ArrayList<>(sortCount);

			for (Sort sort : sorts) {
				Order order = createOrder(root, fromContext, sort);
				result.add(order);
			}
		}

		return result;
	}

	protected Expression<?> getPathForField(Root<RootEntity> from,
			QueryInfoFromContext fromContext,
			String fieldName) throws QueryInfoException {
		return pathFactory.getPathForField(from, fromContext, fieldName);
	}
}
