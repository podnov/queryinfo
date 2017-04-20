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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeContext;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributePurpose;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.DefaultQueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.join.DefaultQueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinInfo;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoinType;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public class DefaultQueryInfoPathFactoryTest {

	private final Class<Entity> entityClass = Entity.class;
	private QueryInfoEntityContextRegistry entityContextRegistry;
	private From<?, Entity> from;
	private QueryInfoJPAContext<?, ?> jpaContext;
	private DefaultQueryInfoPathFactory<Entity> pathFactory;
	private QueryInfoAttributeContext queryInfoAttributeContext;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		pathFactory = new DefaultQueryInfoPathFactory<>(entityClass);
		pathFactory = spy(pathFactory);

		from = mock(From.class);
		jpaContext = mock(QueryInfoJPAContext.class);

		queryInfoAttributeContext = mock(QueryInfoAttributeContext.class);
		doReturn(queryInfoAttributeContext).when(pathFactory).getAttributeContext(entityContextRegistry,
				from);
	}

	@Test
	public void getSubqueryRootPath_isAField_invalidPurpose() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SUBQUERY_ROOT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(false);
		givenFieldInfo.setName(givenAttributePath);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;

		try {
			pathFactory.getSubqueryRootPath(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field name [myAttributePath] is not valid for [SUBQUERY_ROOT]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void getSubqueryRootPath_isAField_validPurpose() throws QueryInfoException {
		String givenAttributePath = "myAttributePath";
		String givenJpaAttributeName = givenAttributePath;

		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SUBQUERY_ROOT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(true);
		givenFieldInfo.setJpaAttributeName(givenJpaAttributeName);
		givenFieldInfo.setName(givenAttributePath);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		@SuppressWarnings("unchecked")
		Path<Object> givenPath = mock(Path.class);
		doReturn(givenPath).when(from).get(givenJpaAttributeName);

		Path<Object> actual = pathFactory.getSubqueryRootPath(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);

		Path<Object> expected = givenPath;

		assertEquals(expected, actual);
	}

	@Test
	public void getSubqueryRootPath_isAFieldAndAJoin() throws QueryInfoException {
		String givenAttributePath = "myAttributePath";
		String givenFieldJpaAttributeName = "myFieldJpaAttributePath";
		String givenJoinJpaAttributeName = "myJoinJpaAttributePath";

		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SUBQUERY_ROOT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(true);
		givenFieldInfo.setJpaAttributeName(givenFieldJpaAttributeName);
		givenFieldInfo.setName(givenAttributePath);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		@SuppressWarnings("unchecked")
		Path<Object> givenFieldPath = mock(Path.class);
		doReturn(givenFieldPath).when(from).get(givenFieldJpaAttributeName);

		QueryInfoJoinInfo givenJoinInfo = new DefaultQueryInfoJoinInfo();
		givenJoinInfo.setJpaAttributeName(givenJoinJpaAttributeName);
		givenJoinInfo.setName(givenAttributePath);

		doReturn(givenJoinInfo).when(queryInfoAttributeContext).getJoin(givenAttributePath);

		@SuppressWarnings("unchecked")
		Join<Object, Object> givenJoinPath = mock(Join.class);
		doReturn(givenJoinPath).when(jpaContext).getJoin(from, givenJoinInfo);

		Path<Object> actual = pathFactory.getSubqueryRootPath(entityContextRegistry,
				jpaContext,
				from,
				givenAttributePath,
				givenPurpose);

		Path<Object> expected = givenFieldPath;

		assertEquals(expected, actual);
	}

	@Test
	public void getSubqueryRootPath_isAJoin() throws QueryInfoException {
		String givenAttributePath = "myAttributePath";
		String givenJpaAttributeName = givenAttributePath;

		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SUBQUERY_ROOT;

		QueryInfoJoinInfo givenJoinInfo = new DefaultQueryInfoJoinInfo();
		givenJoinInfo.setJpaAttributeName(givenJpaAttributeName);
		givenJoinInfo.setName(givenAttributePath);

		doReturn(null).when(queryInfoAttributeContext).getField(givenAttributePath);
		doReturn(givenJoinInfo).when(queryInfoAttributeContext).getJoin(givenAttributePath);

		@SuppressWarnings("unchecked")
		Join<Object, Object> givenPath = mock(Join.class);
		doReturn(givenPath).when(jpaContext).getJoin(from, givenJoinInfo);

		Path<Object> actual = pathFactory.getSubqueryRootPath(entityContextRegistry,
				jpaContext,
				from,
				givenAttributePath,
				givenPurpose);

		Path<Object> expected = givenPath;

		assertEquals(expected, actual);
	}

	@Test
	public void getSubqueryRootPath_notAField_notAJoin() throws QueryInfoException {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SUBQUERY_ROOT;

		doReturn(null).when(queryInfoAttributeContext).getField(givenAttributePath);
		doReturn(null).when(queryInfoAttributeContext).getJoin(givenAttributePath);

		Path<Object> actual = pathFactory.getSubqueryRootPath(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);

		assertNull(actual);
	}

	@Test
	public void validateFieldInfo_isField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isNotField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = null;
		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field not defined for attribute path [myAttributePath]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isNotOrderField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.ORDER;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsOrderable(false);
		givenFieldInfo.setName(givenAttributePath);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field name [myAttributePath] is not valid for [ORDER]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isNotPredicateField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.PREDICATE;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(false);
		givenFieldInfo.setName(givenAttributePath);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field name [myAttributePath] is not valid for [PREDICATE]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isNotSelectField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(false);
		givenFieldInfo.setName(givenAttributePath);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;

		try {
			pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Field name [myAttributePath] is not valid for [SELECT]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void validateFieldInfo_isOrderField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.ORDER;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsOrderable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isPredicateField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.PREDICATE;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsPredicateable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isSelectField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
					givenPurpose);
		} catch (QueryInfoException e) {
			actualException = e;
		}

		assertNull(actualException);

		assertEquals(givenFieldInfo, actualFieldInfo);
	}

	@Test
	public void validateFieldInfo_isJoinedField() {
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);
		givenFieldInfo.setJoinType(QueryInfoJoinType.LEFT);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
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
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);
		givenFieldInfo.setJoinType(null);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
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
		String givenAttributePath = "myAttributePath";
		QueryInfoAttributePurpose givenPurpose = QueryInfoAttributePurpose.SELECT;

		QueryInfoFieldInfo givenFieldInfo = new DefaultQueryInfoFieldInfo();
		givenFieldInfo.setIsSelectable(true);
		givenFieldInfo.setJoinType(QueryInfoJoinType.UNSPECIFIED);

		doReturn(givenFieldInfo).when(queryInfoAttributeContext).getField(givenAttributePath);

		QueryInfoException actualException = null;
		QueryInfoFieldInfo actualFieldInfo = null;

		try {
			actualFieldInfo = pathFactory.validateFieldInfo(entityContextRegistry,
					jpaContext,
					from,
					givenAttributePath,
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
