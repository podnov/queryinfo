package com.evanzeimet.queryinfo.jpa.result;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
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


import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.attribute.QueryInfoAttributeUtils;

public abstract class AbstractTupleToPojoQueryInfoResultConverter<QueryInfoResultType>
		implements QueryInfoResultConverter<Tuple, QueryInfoResultType> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTupleToPojoQueryInfoResultConverter.class);

	protected QueryInfoAttributeUtils attributeUtils = new QueryInfoAttributeUtils();
	protected QueryInfoBaseInstanceFactory<QueryInfoResultType> baseInstanceFactory;
	protected Class<? extends QueryInfoResultType> resultClass;


	public AbstractTupleToPojoQueryInfoResultConverter(Class<? extends QueryInfoResultType> resultClass, QueryInfoBaseInstanceFactory<QueryInfoResultType> baseInstanceFactory) {
		this.resultClass = resultClass;
		this.baseInstanceFactory = baseInstanceFactory;
	}

	@Override
	public List<QueryInfoResultType> convert(List<Tuple> tuples) throws QueryInfoException {
		List<QueryInfoResultType> result;

		if (tuples.isEmpty()) {
			result = Collections.emptyList();
		} else {
			Tuple peek = tuples.get(0);
			List<TupleElement<?>> tupleElements = peek.getElements();

			try {
				Map<String, MethodHandle> methodHandles = mapElementMethodHandles(tupleElements);
				result = convertTuples(methodHandles, tuples);
			} catch (Throwable e) {
				throw new QueryInfoException(e);
			}
		}

		return result;
	}

	protected QueryInfoResultType convertTuple(Map<String, MethodHandle> methodHandles,
			Tuple tuple) throws Throwable {
		QueryInfoResultType result = baseInstanceFactory.create();
		Iterator<Entry<String, MethodHandle>> methodHandlesEntryIterator = methodHandles.entrySet().iterator();

		while (methodHandlesEntryIterator.hasNext()) {
			Entry<String, MethodHandle> methodHandleEntry = methodHandlesEntryIterator.next();

			String elementAlias = methodHandleEntry.getKey();
			Object value = tuple.get(elementAlias);

			MethodHandle methodHandle = methodHandleEntry.getValue();

			methodHandle.invoke(result, value);
		}

		return result;
	}

	protected List<QueryInfoResultType> convertTuples(Map<String, MethodHandle> methodHandles, List<Tuple> tuples)
			throws Throwable {
		int tupleCount = tuples.size();
		List<QueryInfoResultType> result = new ArrayList<>(tupleCount);

		for (Tuple tuple : tuples) {
			QueryInfoResultType converted = convertTuple(methodHandles, tuple);
			result.add(converted);
		}

		return result;
	}

	protected MethodHandle findFieldPutHandle(String memberName, Class<?> elementJavaType) {
		return findFieldPutHandle(resultClass, memberName, elementJavaType);
	}

	protected MethodHandle findFieldPutHandle(Class<?> hostClass,
			String memberName,
			Class<?> elementJavaType) {
		MethodHandle result = null;

		try {
			Field field = hostClass.getDeclaredField(memberName);
			field.setAccessible(true);
			result = MethodHandles.lookup().unreflectSetter(field);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			String message = String.format("Could not find field put handle for field [%s]", memberName);
			logger.debug(message);
		}

		if (result == null) {
			Class<?> hostSuperclass = hostClass.getSuperclass();

			if (hostSuperclass != null) {
				result = findFieldPutHandle(hostSuperclass, memberName, elementJavaType);
			}
		}

		return result;
	}

	protected MethodHandle findFieldSetterHandle(String memberName, Class<?> elementJavaType) {
		MethodHandle result = null;
		String methodSuffix = StringUtils.capitalize(memberName);
		String setterName = String.format("set%s", methodSuffix);
		MethodType methodType = methodType(void.class, elementJavaType);

		try {
			result = MethodHandles.lookup().findVirtual(resultClass,
					setterName,
					methodType);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			String message = String.format("Could not find field setter handle [%s] for field [%s]",
					setterName,
					memberName);
			logger.debug(message);
		}

		return result;
	}

	protected MethodHandle getMethodHandleForElement(TupleElement<?> element)
			throws QueryInfoException {
		String elementAlias = element.getAlias();
		String memberName = attributeUtils.convertAttributeNameToMemberName(elementAlias);
		Class<?> elementJavaType = element.getJavaType();

		MethodHandle result = findFieldSetterHandle(memberName, elementJavaType);

		if (result == null) {
			result = findFieldPutHandle(memberName, elementJavaType);
		}

		if (result == null) {
			String message = String.format("Could not find accessible setter or field for attribute [%s]",
					elementAlias);
			throw new QueryInfoException(message);
		}

		return result;
	}

	protected Map<String, MethodHandle> mapElementMethodHandles(List<TupleElement<?>> tupleElements)
			throws QueryInfoException {
		int elementCount = tupleElements.size();
		Map<String, MethodHandle> result = new HashMap<>(elementCount);

		Iterator<TupleElement<?>> elementIterator = tupleElements.iterator();

		while (elementIterator.hasNext()) {
			TupleElement<?> element = elementIterator.next();
			String elementAlias = element.getAlias();

			MethodHandle methodHandle = getMethodHandleForElement(element);

			result.put(elementAlias, methodHandle);
		}

		return result;
	}

}
