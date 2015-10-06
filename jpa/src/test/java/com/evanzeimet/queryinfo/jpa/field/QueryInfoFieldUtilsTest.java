package com.evanzeimet.queryinfo.jpa.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionGroup;
import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.condition.ConditionBuilder;
import com.evanzeimet.queryinfo.condition.ConditionGroupBuilder;
import com.evanzeimet.queryinfo.QueryInfoBuilder;
import com.evanzeimet.queryinfo.sort.SortBuilder;

/*
 * TODO refactor the given builders to be given json
 */
public class QueryInfoFieldUtilsTest {

	private QueryInfoFieldUtils utils;

	@Before
	public void setUp() {
		utils = new QueryInfoFieldUtils();
	}
	
	@Test
	public void hasAnyFieldName_nestedConditionGroup_false() {
		Condition condition = ConditionBuilder.create()
				.leftHandSide("field2")
				.build();

		ConditionGroup conditionGroup = ConditionGroupBuilder.createForCondition(condition)
				.build();
		conditionGroup = ConditionGroupBuilder.createForConditionGroup(conditionGroup).build();
		conditionGroup = ConditionGroupBuilder.createForConditionGroup(conditionGroup).build();

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.conditionGroup(conditionGroup)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_nestedConditionGroup_true() {
		
		Condition condition = ConditionBuilder.create()
				.leftHandSide("field3")
				.build();

		ConditionGroup conditionGroup = ConditionGroupBuilder.createForCondition(condition)
				.build();
		conditionGroup = ConditionGroupBuilder.createForConditionGroup(conditionGroup).build();
		conditionGroup = ConditionGroupBuilder.createForConditionGroup(conditionGroup).build();
		
		QueryInfo queryInfo = QueryInfoBuilder.create()
			.conditionGroup(conditionGroup)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");
		
		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);
		
		assertTrue(actual);
	}

	@Test
	public void hasAnyFieldName_requestedFields_true() {
		List<String> requestedFields = Arrays.asList("field3");

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.requestedFields(requestedFields)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");
		
		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertTrue(actual);
	}

	@Test
	public void hasAnyFieldName_requestedFields_false() {
		List<String> requestedFields = Arrays.asList("field2");

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.requestedFields(requestedFields)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_rootConditionGroup_false() {
		Condition condition = ConditionBuilder.create()
				.leftHandSide("field2")
				.build();

		ConditionGroup conditionGroup = ConditionGroupBuilder.createForCondition(condition)
				.build();

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.conditionGroup(conditionGroup)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_rootConditionGroup_true() {
		
		Condition condition = ConditionBuilder.create()
				.leftHandSide("field3")
				.build();

		ConditionGroup conditionGroup = ConditionGroupBuilder.createForCondition(condition)
				.build();
		
		QueryInfo queryInfo = QueryInfoBuilder.create()
			.conditionGroup(conditionGroup)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");
		
		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);
		
		assertTrue(actual);
	}
	
	@Test
	public void hasAnyFieldName_sorts_false() {
		Sort sort = SortBuilder.create()
			.fieldName("field2")
			.build();

		List<Sort> sorts = Arrays.asList(sort);

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.sorts(sorts)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");

		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);

		assertFalse(actual);
	}

	@Test
	public void hasAnyFieldName_sorts_true() {
		Sort sort = SortBuilder.create()
				.fieldName("field3")
				.build();

		List<Sort> sorts = Arrays.asList(sort);

		QueryInfo queryInfo = QueryInfoBuilder.create()
			.sorts(sorts)
			.build();

		List<String> fieldNames = Arrays.asList("field1", "field3");
		
		boolean actual = utils.hasAnyFieldName(queryInfo, fieldNames);
		
		assertTrue(actual);
	}
	
	@Test
	public void hasRequestedAllFields_emptyList() {
		QueryInfo queryInfo = new DefaultQueryInfo();
		
		List<String> requestedFieldNames = Collections.emptyList();
		
		queryInfo.setRequestedFieldNames(requestedFieldNames);
		
		boolean actual = utils.hasRequestedAllFields(queryInfo);
		
		assertFalse(actual);
	}
	
	@Test
	public void hasRequestedAllFields_notEmptyList() {
		QueryInfo queryInfo = new DefaultQueryInfo();
		
		List<String> requestedFieldNames = Arrays.asList("field1");
		
		queryInfo.setRequestedFieldNames(requestedFieldNames);
		
		boolean actual = utils.hasRequestedAllFields(queryInfo);
		
		assertFalse(actual);
	}
	
	@Test
	public void hasRequestedAllFields_null() {
		QueryInfo queryInfo = new DefaultQueryInfo();
		
		List<String> requestedFieldNames = null;
		
		queryInfo.setRequestedFieldNames(requestedFieldNames);
		
		boolean actual = utils.hasRequestedAllFields(queryInfo);
		
		assertTrue(actual);
	}
}
