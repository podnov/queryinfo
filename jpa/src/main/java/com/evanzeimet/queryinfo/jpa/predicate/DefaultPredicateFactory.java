package com.evanzeimet.queryinfo.jpa.predicate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupOperator;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoFromContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public class DefaultPredicateFactory<RootEntity> implements PredicateFactory<RootEntity> {

	protected CriteriaBuilder criteriaBuilder;
	private QueryInfoPathFactory<RootEntity> pathFactory;

	public DefaultPredicateFactory() {

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

	protected Predicate createPredicateForConditionGroup(AbstractQuery<?> query,
			Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			ConditionGroup conditionGroup) {
		Predicate result;
		List<Predicate> predicateList = new ArrayList<>();

		if (predicateList.isEmpty()) {
			result = null;
		} else {
			String rawConditionGroupOperator = conditionGroup.getOperator();

			ConditionGroupOperator conditionGroupOperator = ConditionGroupOperator.fromText(
					rawConditionGroupOperator);

			int predicateCount = predicateList.size();
			Predicate[] predicateArray = predicateList.toArray(new Predicate[predicateCount]);

			if (ConditionGroupOperator.OR.equals(conditionGroupOperator)) {
				result = criteriaBuilder.or(predicateArray);
			} else {
				result = criteriaBuilder.and(predicateArray);
			}
		}
		return result;
	}

	@Override
	public Predicate[] createPredicates(AbstractQuery<?> query,
			Root<RootEntity> root,
			QueryInfoFromContext fromContext,
			QueryInfo queryInfo) {
		Predicate[] result;
		ConditionGroup conditionGroup = queryInfo.getConditionGroup();

		Predicate predicate = createPredicateForConditionGroup(query,
				root,
				fromContext,
				conditionGroup);

		if (predicate == null) {
			result = new Predicate[0];
		} else {
			result = new Predicate[] { predicate };
		}

		return null;
	}
}
