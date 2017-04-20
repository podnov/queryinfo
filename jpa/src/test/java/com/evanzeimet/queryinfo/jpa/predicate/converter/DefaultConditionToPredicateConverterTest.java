package com.evanzeimet.queryinfo.jpa.predicate.converter;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import static com.evanzeimet.queryinfo.condition.ConditionOperator.EQUAL_TO;
import static com.evanzeimet.queryinfo.condition.OperandType.ATTRIBUTE_PATH;
import static com.evanzeimet.queryinfo.condition.OperandType.LITERAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.condition.Condition;
import com.evanzeimet.queryinfo.condition.ConditionBuilder;
import com.evanzeimet.queryinfo.condition.ConditionOperator;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContexts;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public class DefaultConditionToPredicateConverterTest {

	private DefaultConditionToPredicateConverter<Object> converter;

	@Before
	public void setUp() {
		converter = new DefaultConditionToPredicateConverter<Object>();
		converter = spy(converter);
	}

	@Test
	public void convert_missingOperator() throws QueryInfoException {
		QueryInfoEntityContextRegistry givenEntityContextRegistry = null;
		QueryInfoJPAContexts<Object, ?> givenJpaContexts = null;
		QueryInfoJPAContext<Object, ?> givenJpaContext = null;

		Condition givenCondition = ConditionBuilder.create()
				.leftHandSide("myAttributePath")
				.operator((String) null)
				.rightHandSide("test")
				.build();

		Predicate actual = converter.convert(givenEntityContextRegistry,
				givenJpaContexts,
				givenJpaContext,
				givenCondition);

		assertNull(actual);
	}

	@Test
	public void convert_leftHandSide_queryReference_missingAttributePath() throws QueryInfoException {
		QueryInfoEntityContextRegistry givenEntityContextRegistry = mock(QueryInfoEntityContextRegistry.class);
		QueryInfoJPAContexts<Object, ?> givenJpaContexts = null;

		QueryInfoJPAContext<Object, ?> givenJpaContext = new QueryInfoJPAContext<>();
		givenJpaContext = spy(givenJpaContext);
		doReturn(mock(CriteriaBuilder.class)).when(givenJpaContext).getCriteriaBuilder();

		Condition givenCondition = ConditionBuilder.create()
				.leftHandSide(null)
				.leftHandSideType(ATTRIBUTE_PATH)
				.operator(EQUAL_TO)
				.rightHandSide("test")
				.rightHandSideType(LITERAL)
				.build();

		Predicate actual = converter.convert(givenEntityContextRegistry,
				givenJpaContexts,
				givenJpaContext,
				givenCondition);

		assertNull(actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void convert_rightHandSide_queryReference_missingAttributePath() throws QueryInfoException {
		QueryInfoEntityContextRegistry givenEntityContextRegistry = mock(QueryInfoEntityContextRegistry.class);
		QueryInfoJPAContext<?, ?> givenReferenceJpaContext = mock(QueryInfoJPAContext.class);

		doReturn(givenReferenceJpaContext).when(converter).getReferencedJpaContext(any(QueryInfoJPAContexts.class),
				any(QueryInfoJPAContext.class),
				any(String.class));

		QueryInfoPathFactory<?> givenReferencePathFactory = mock(QueryInfoPathFactory.class);
		QueryInfoEntityContext<?> givenReferenceEntityContext = mock(QueryInfoEntityContext.class);
		doReturn(givenReferencePathFactory).when(givenReferenceEntityContext).getPathFactory();

		doReturn(givenReferenceEntityContext).when(givenEntityContextRegistry)
				.getContextForRoot(givenReferenceJpaContext);

		QueryInfoJPAContexts<Object, ?> givenJpaContexts = null;

		QueryInfoJPAContext<Object, ?> givenJpaContext = new QueryInfoJPAContext<>();
		givenJpaContext = spy(givenJpaContext);
		doReturn(mock(CriteriaBuilder.class)).when(givenJpaContext).getCriteriaBuilder();

		Condition givenCondition = ConditionBuilder.create()
				.leftHandSide("test")
				.leftHandSideType(LITERAL)
				.operator(ConditionOperator.EQUAL_TO)
				.rightHandSide(null)
				.rightHandSideType(ATTRIBUTE_PATH)
				.build();

		Predicate actual = converter.convert(givenEntityContextRegistry,
				givenJpaContexts,
				givenJpaContext,
				givenCondition);

		assertNull(actual);
	}

	@Test
	public void getReferencedJpaContext_typeConfig_custom() {
		String givenLeftHandSideTypeConfig = "mySubqueryName";
		QueryInfoJPAContext<Object, ?> givenCustomJpaContext = new QueryInfoJPAContext<>();

		QueryInfoJPAContexts<?, ?> givenJpaContexts = new QueryInfoJPAContexts<>();
		givenJpaContexts.putNamedSubqueryContext(givenLeftHandSideTypeConfig, givenCustomJpaContext);

		QueryInfoJPAContext<Object, ?> givenCurrentJpaContext = new QueryInfoJPAContext<>();

		QueryInfoJPAContext<Object, AbstractQuery<?>> actual = converter.getReferencedJpaContext(givenJpaContexts,
				givenCurrentJpaContext,
				givenLeftHandSideTypeConfig);
		QueryInfoJPAContext<Object, ?> expected = givenCustomJpaContext;

		assertEquals(expected, actual);
	}

	@Test
	public void getReferencedJpaContext_typeConfig_null() {
		QueryInfoJPAContexts<?, ?> givenJpaContexts = new QueryInfoJPAContexts<>();
		QueryInfoJPAContext<Object, ?> givenCurrentJpaContext = new QueryInfoJPAContext<>();
		String givenLeftHandSideTypeConfig = null;

		QueryInfoJPAContext<Object, AbstractQuery<?>> actual = converter.getReferencedJpaContext(givenJpaContexts,
				givenCurrentJpaContext, 
				givenLeftHandSideTypeConfig);
		QueryInfoJPAContext<Object, ?> expected = givenCurrentJpaContext;

		assertEquals(expected, actual);
	}

	@Test
	public void getReferencedJpaContext_typeConfig_root() {
		QueryInfoJPAContext<Object, CriteriaQuery<Object>> givenRootJpaContext = new QueryInfoJPAContext<>();

		QueryInfoJPAContexts<Object, Object> givenJpaContexts = new QueryInfoJPAContexts<>();
		givenJpaContexts.setRootContext(givenRootJpaContext);

		QueryInfoJPAContext<Object, ?> givenCurrentJpaContext = new QueryInfoJPAContext<>();
		String givenLeftHandSideTypeConfig = "root";

		QueryInfoJPAContext<Object, AbstractQuery<?>> actual = converter.getReferencedJpaContext(givenJpaContexts,
				givenCurrentJpaContext,
				givenLeftHandSideTypeConfig);
		QueryInfoJPAContext<Object, ?> expected = givenRootJpaContext;

		assertEquals(expected, actual);
	}
}
