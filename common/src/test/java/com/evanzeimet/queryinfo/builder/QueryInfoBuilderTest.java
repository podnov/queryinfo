package com.evanzeimet.queryinfo.builder;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoBuilder;
import com.evanzeimet.queryinfo.QueryInfoTestUtils;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionBuilder;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupBuilder;
import com.evanzeimet.queryinfo.condition.ConditionGroupOperator;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfoBuilder;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortBuilder;
import com.evanzeimet.queryinfo.sort.SortDirection;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QueryInfoBuilderTest {

	private QueryInfoBuilder builder;

	@Before
	public void setUp() {
		builder = new QueryInfoBuilder();
	}

	@Test
	public void build()
			throws IOException {
		Condition givenNestedConditionGroupCondition1 = ConditionBuilder.create()
				.leftHandSide("category")
				.operator(ConditionOperator.EQUAL_TO)
				.rightHandSide("Awesome Stuff")
				.build();

		Condition givenNestedConditionGroupCondition2 = ConditionBuilder.create()
				.leftHandSide("priority")
				.operator(ConditionOperator.GREATER_THAN)
				.rightHandSide("2")
				.build();

		List<Condition> givenNestedConditionGroupConditions = Arrays.asList(givenNestedConditionGroupCondition1, givenNestedConditionGroupCondition2);

		ConditionGroup givenNestedConditionGroup = ConditionGroupBuilder.create()
				.conditions(givenNestedConditionGroupConditions)
				.operator(ConditionGroupOperator.OR)
				.build();

		Condition givenNestedCondition = ConditionBuilder.create()
				.leftHandSide("active")
				.operator(ConditionOperator.EQUAL_TO)
				.rightHandSide("true")
				.build();

		List<ConditionGroup> givenNestedConditionGroups = Arrays.asList(givenNestedConditionGroup);
		List<Condition> givenNestedConditions = Arrays.asList(givenNestedCondition);

		ConditionGroup givenConditionGroup = ConditionGroupBuilder.create()
				.conditionGroups(givenNestedConditionGroups)
				.conditions(givenNestedConditions)
				.operator(ConditionGroupOperator.AND)
				.build();

		PaginationInfo givenPaginationInfo = PaginationInfoBuilder.createForAll().build();
		List<String> givenRequestedFields = Arrays.asList("a", "b", "c");

		Sort givenSort = SortBuilder.create()
				.direction(SortDirection.ASC)
				.fieldName("x")
				.build();

		List<Sort> givenSorts = Arrays.asList(givenSort);

		QueryInfo actualQueryInfo = builder.conditionGroup(givenConditionGroup)
				.paginationInfo(givenPaginationInfo)
				.requestedFields(givenRequestedFields)
				.sorts(givenSorts)
				.build();

		String expectedJson = QueryInfoTestUtils.getExpectedJson(getClass(),
				"QueryInfoBuilderTest_build_expected.json");
		String actualJson = QueryInfoTestUtils.createActualJson(actualQueryInfo);

		assertEquals(expectedJson, actualJson);
	}
}
