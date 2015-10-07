package com.evanzeimet.queryinfo.jpa.predicate;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupOperator;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public class DefaultQueryInfoPredicateFactory<RootEntity> implements QueryInfoPredicateFactory<RootEntity> {

	private QueryInfoPathFactory<RootEntity> pathFactory;

	public DefaultQueryInfoPredicateFactory() {

	}

	@Override
	public QueryInfoPathFactory<RootEntity> getPathFactory() {
		return pathFactory;
	}

	@Override
	public void setPathFactory(QueryInfoPathFactory<RootEntity> pathFactory) {
		this.pathFactory = pathFactory;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Predicate cretePredicate(QueryInfoJPAContext jpaContext,
			String fieldName,
			ConditionOperator conditionOperator,
			String fieldValue) throws QueryInfoException {
		Predicate result = null;

		CriteriaBuilder criteriaBuilder = jpaContext.getCriteriaBuilder();
		Root<RootEntity> root = jpaContext.getRoot();

		Expression<?> path = pathFactory.getPathForField(root,
				jpaContext,
				fieldName,
				QueryInfoFieldPurpose.PREDICATE);

		// TODO date parsing

		switch (conditionOperator) {
			case EQUAL_TO:
				result = criteriaBuilder.equal(path, fieldValue);
				break;

			case GREATER_THAN:
				result = criteriaBuilder.greaterThan((Expression<Comparable>) path, fieldValue);
				break;

			case GREATER_THAN_OR_EQUAL_TO:
				result = criteriaBuilder.greaterThanOrEqualTo((Expression<Comparable>) path,
						fieldValue);
				break;

			case IN:
				// TODO
				break;

			case LESS_THAN:
				result = criteriaBuilder.lessThan((Expression<Comparable>) path, fieldValue);
				break;

			case LESS_THAN_OR_EQUAL_TO:
				result = criteriaBuilder.lessThanOrEqualTo((Expression<Comparable>) path,
						fieldValue);
				break;

			case NOT_EQUAL_TO:
				result = criteriaBuilder.notEqual(path, fieldValue);
				break;

			case NOT_IN:
				// TODO
				break;

			case NOT_NULL:
				result = criteriaBuilder.isNotNull(path);
				break;

			case NULL:
				result = criteriaBuilder.isNull(path);
				break;
		}

		return result;
	}

	protected Predicate createPredicateForCondition(QueryInfoJPAContext<RootEntity> jpaContext,
			Condition condition) throws QueryInfoException {
		Predicate result = null;
		String fieldName = condition.getLeftHandSide();
		String operator = condition.getOperator();
		String fieldValue = condition.getRightHandSide();

		ConditionOperator conditionOperator = ConditionOperator.fromText(operator);

		boolean hasFieldName = StringUtils.isNotBlank(fieldName);
		boolean hasOperator = (conditionOperator != null);

		if (hasFieldName && hasOperator) {
			result = cretePredicate(jpaContext,
					fieldName,
					conditionOperator,
					fieldValue);
		}

		if (result == null) {
			String message = String.format(
					"Invalid condition supplied, fieldName: [%s], operator: [%s], fieldValue [%s]",
					fieldName,
					operator,
					fieldValue);
			throw new QueryInfoException(message);
		}

		return result;
	}

	protected Predicate createPredicateForConditionGroup(QueryInfoJPAContext<RootEntity> jpaContext,
			ConditionGroup conditionGroup) throws QueryInfoException {
		Predicate result;
		List<Predicate> predicateList = new ArrayList<>();

		List<ConditionGroup> conditionGroups = conditionGroup.getConditionGroups();
		List<Predicate> conditionGroupsPredicates = createPredicatesForConditionGroups(jpaContext,
				conditionGroups);
		predicateList.addAll(conditionGroupsPredicates);

		List<Condition> conditions = conditionGroup.getConditions();
		List<Predicate> conditionsPredicates = createPredicatesForConditions(jpaContext,
				conditions);
		predicateList.addAll(conditionsPredicates);

		if (predicateList.isEmpty()) {
			result = null;
		} else {
			CriteriaBuilder criteriaBuilder = jpaContext.getCriteriaBuilder();
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
	public Predicate[] createPredicates(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		Predicate[] result;
		ConditionGroup conditionGroup = queryInfo.getConditionGroup();

		Predicate predicate = createPredicateForConditionGroup(jpaContext,
				conditionGroup);

		if (predicate == null) {
			result = new Predicate[0];
		} else {
			result = new Predicate[] { predicate };
		}

		return result;
	}

	protected List<Predicate> createPredicatesForConditionGroups(QueryInfoJPAContext<RootEntity> jpaContext,
			List<ConditionGroup> conditionGroups) throws QueryInfoException {
		List<Predicate> result = new ArrayList<>();

		if (conditionGroups != null) {
			for (ConditionGroup conditionGroup : conditionGroups) {
				Predicate predicate = createPredicateForConditionGroup(jpaContext,
						conditionGroup);

				if (predicate != null) {
					result.add(predicate);
				}
			}
		}

		return result;
	}

	protected List<Predicate> createPredicatesForConditions(QueryInfoJPAContext<RootEntity> jpaContext,
			List<Condition> conditions) throws QueryInfoException {
		List<Predicate> result = new ArrayList<>();

		if (conditions != null) {
			for (Condition condition : conditions) {
				Predicate predicate = createPredicateForCondition(jpaContext,
						condition);
				result.add(predicate);
			}
		}

		return result;
	}
}
