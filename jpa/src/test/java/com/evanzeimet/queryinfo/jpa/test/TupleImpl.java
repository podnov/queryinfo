package com.evanzeimet.queryinfo.jpa.test;

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


import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

/**
 * Modelled after org.hibernate.pa.spi.AbstractEntityManagerImpl$CriteriaQueryTransformer$TupleImpl.
 */
public class TupleImpl implements Tuple {

	private final List<TupleElement<?>> tupleElements;
	private final Object[] tuples;

	public TupleImpl(List<TupleElement<?>> tupleElements, Object[] tuples) {
		this.tupleElements = tupleElements;
		this.tuples = tuples;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X> X get(TupleElement<X> tupleElement) {
		int index = tupleElements.indexOf(tupleElement);
		if (index < 0) {
			throw new IllegalArgumentException("Requested tuple element did not correspond to element in the result tuple");
		}
		// index should be "in range" by nature of size check in ctor
		return (X) tuples[index];
	}

	@Override
	public Object get(String alias) {
		int index = -1;
		if (alias != null) {
			alias = alias.trim();
			if (alias.length() > 0) {
				int i = 0;
				for (TupleElement<?> selection : tupleElements) {
					if (alias.equals(selection.getAlias())) {
						index = i;
						break;
					}
					i++;
				}
			}
		}
		if (index < 0) {
			throw new IllegalArgumentException(
					"Given alias [" + alias + "] did not correspond to an element in the result tuple");
		}
		// index should be "in range" by nature of size check in ctor
		return tuples[index];
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X> X get(String alias, Class<X> type) {
		final Object untyped = get(alias);
		if (untyped != null) {
			if (!type.isInstance(untyped)) {
				String message = String.format("Requested tuple value [alias=%s, value=%s] cannot be assigned to requested type [%s]",
						alias,
						untyped,
						type.getName());
				throw new IllegalArgumentException(message);
			}
		}
		return (X) untyped;
	}

	@Override
	public Object get(int i) {
		if (i >= tuples.length) {
			String message = "Given index [" + i + "] was outside the range of result tuple size [" + tuples.length + "] ";
			throw new IllegalArgumentException(message);
		}
		return tuples[i];
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X> X get(int i, Class<X> type) {
		final Object result = get(i);
		if ((result != null) && !type.isInstance(result)) {
			String message = String.format("Requested tuple value [index=%s, realType=%s] cannot be assigned to requested type [%s]",
					i,
					result.getClass().getName(),
					type.getName());
			throw new IllegalArgumentException(message);
		}
		return (X) result;
	}

	@Override
	public Object[] toArray() {
		return tuples;
	}

	@Override
	public List<TupleElement<?>> getElements() {
		return tupleElements;
	}

}
