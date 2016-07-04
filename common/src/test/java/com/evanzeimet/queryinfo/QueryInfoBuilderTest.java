package com.evanzeimet.queryinfo;

/*
 * #%L
 * queryinfo-common
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoBuilder;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionBuilder;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.condition.ConditionGroupBuilder;
import com.evanzeimet.queryinfo.condition.ConditionGroupOperator;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.pagination.PaginationInfo;
import com.evanzeimet.queryinfo.pagination.PaginationInfoBuilder;
import com.evanzeimet.queryinfo.selection.AggregateFunction;
import com.evanzeimet.queryinfo.selection.Selection;
import com.evanzeimet.queryinfo.selection.SelectionBuilder;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortBuilder;
import com.evanzeimet.queryinfo.sort.SortDirection;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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
		List<Selection> givenSelections = new ArrayList<>();

		Selection givenSelection = SelectionBuilder.create()
				.aggregateFunction(AggregateFunction.COUNT)
				.attributePath("a")
				.build();
		givenSelections.add(givenSelection);

		givenSelection = SelectionBuilder.create()
				.attributePath("b")
				.build();
		givenSelections.add(givenSelection);

		Sort givenSort = SortBuilder.create()
				.attributePath("x")
				.direction(SortDirection.ASC)
				.build();

		List<Sort> givenSorts = Arrays.asList(givenSort);

		QueryInfo actualQueryInfo = builder.conditionGroup(givenConditionGroup)
				.paginationInfo(givenPaginationInfo)
				.selections(givenSelections)
				.sorts(givenSorts)
				.build();

		String expectedJson = QueryInfoTestUtils.getFormattedJson(getClass(),
				"QueryInfoBuilderTest_build_expected.json");
		String actualJson = QueryInfoTestUtils.createActualJson(actualQueryInfo);

		assertEquals(expectedJson, actualJson);
	}
}
