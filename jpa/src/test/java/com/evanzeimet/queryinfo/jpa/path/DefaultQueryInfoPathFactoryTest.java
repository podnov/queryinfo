package com.evanzeimet.queryinfo.jpa.path;

/*
 * #%L
 * queryinfo-jpa
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import javax.persistence.criteria.From;

import org.junit.Before;
import org.junit.Test;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.DefaultQueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultQueryInfoPathFactoryTest {

	private Class<Entity> entityClass = Entity.class;
	private QueryInfoEntityContextRegistry entityContextRegistry;
	private From<?, Entity> from;
	private QueryInfoJPAContext<?> jpaContext;
	private DefaultQueryInfoPathFactory<Entity> pathFactory;
	private QueryInfoAttributeContext queryInfoAttributeContext;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		pathFactory = new DefaultQueryInfoPathFactory<>(entityContextRegistry, entityClass);
		pathFactory = spy(pathFactory);

		from = mock(From.class);
		jpaContext = mock(QueryInfoJPAContext.class);

		queryInfoAttributeContext = mock(QueryInfoAttributeContext.class);
		doReturn(queryInfoAttributeContext).when(pathFactory).getAttributeContext(from);
	}

	@Test
	public void validateFieldInfo_isField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isNotField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = null;
		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field not defined for name [myAttributeName]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isNotOrderField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.ORDER;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsOrderable(false);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field [myAttributeName] not valid for [ORDER]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isNotPredicateField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.PREDICATE;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(false);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field [myAttributeName] not valid for [PREDICATE]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isNotSelectField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(false);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field [myAttributeName] not valid for [SELECT]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isOrderField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.ORDER;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsOrderable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isPredicateField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.PREDICATE;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isSelectField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_joinTypeSpecifiedField() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);
		givenFieldInfo.setJoinType(QueryInfoJoinType.LEFT);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);

		verify(jpaContext).getJoin(from, givenFieldInfo);
	}

	@Test
	public void validateFieldInfo_joinTypeNull() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);
		givenFieldInfo.setJoinType(null);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);

		verify(jpaContext, never()).getJoin(any(From.class), any(QueryInfoFieldInfo.class));
	}

	@Test
	public void validateFieldInfo_joinTypeUnspecified() {
		String givenQueryInfoFieldAttributeName = "myAttributeName";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);
		givenFieldInfo.setJoinType(QueryInfoJoinType.UNSPECIFIED);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenQueryInfoFieldAttributeName);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(jpaContext,
					from,
					givenQueryInfoFieldAttributeName,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);

		verify(jpaContext, never()).getJoin(any(From.class), any(QueryInfoFieldInfo.class));
	}

	private static class Entity {

	}
}
