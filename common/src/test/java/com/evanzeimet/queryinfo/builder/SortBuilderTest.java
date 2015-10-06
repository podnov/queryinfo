package com.evanzeimet.queryinfo.builder;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortBuilder;
import com.evanzeimet.queryinfo.sort.SortDirection;

import static org.junit.Assert.*;

public class SortBuilderTest {

	private SortBuilder builder;

	@Before
	public void setUp() {
		builder = new SortBuilder();
	}

	@Test
	public void build() {
		String givenFieldName = "my field name";
		SortDirection givenDirection = SortDirection.ASC;

		Sort actualSort = builder.direction(givenDirection)
				.fieldName(givenFieldName)
				.build();

		String actualFieldName = actualSort.getFieldName();
		assertEquals(givenFieldName, actualFieldName);

		String actualDirection = actualSort.getDirection();
		String expectedDirection = givenDirection.getText();
		assertEquals(expectedDirection, actualDirection);
	}
}
