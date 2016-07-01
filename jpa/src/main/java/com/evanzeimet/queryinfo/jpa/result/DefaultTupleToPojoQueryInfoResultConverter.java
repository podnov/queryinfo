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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.evanzeimet.queryinfo.QueryInfoRuntimeException;

public class DefaultTupleToPojoQueryInfoResultConverter<T>
		extends AbstractTupleToPojoQueryInfoResultConverter<T> {

	private QueryInfoBaseInstanceFactory<T> baseInstanceFactory;
	private Class<T> resultClass;

	public DefaultTupleToPojoQueryInfoResultConverter(Class<T> resultClass) {
		this(resultClass, new DefaultBaseInstanceFactory<T>(resultClass));
	}

	public DefaultTupleToPojoQueryInfoResultConverter(Class<T> resultClass,
			QueryInfoBaseInstanceFactory<T> baseInstanceFactory) {
		setResultClass(resultClass);
		setBaseInstanceFactory(baseInstanceFactory);
	}

	@Override
	public QueryInfoBaseInstanceFactory<T> getBaseInstanceFactory() {
		return baseInstanceFactory;
	}

	public void setBaseInstanceFactory(QueryInfoBaseInstanceFactory<T> baseInstanceFactory) {
		this.baseInstanceFactory = baseInstanceFactory;
	}

	@Override
	public Class<T> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<T> resultClass) {
		this.resultClass = resultClass;
	}

	protected static class DefaultBaseInstanceFactory<T> implements QueryInfoBaseInstanceFactory<T> {

		private Class<T> clazz;
		private MethodHandle constructorMethodHandle;

		public DefaultBaseInstanceFactory(Class<T> clazz) {
			this.clazz = clazz;
			findConstructor();
		}

		protected void findConstructor() {
			MethodType methodType = MethodType.methodType(void.class);

			try {
				constructorMethodHandle = MethodHandles.lookup().findConstructor(clazz, methodType);
			} catch (NoSuchMethodException | IllegalAccessException e) {
				String message = String.format("Could not find accessible no-arg constructor on [%s]", clazz.getCanonicalName());
				throw new QueryInfoRuntimeException(message, e);
			}
		}

		@Override
		public T create() {
			T result;

			try {
				result = (T) constructorMethodHandle.invoke();
			} catch (Throwable e) {
				String message = String.format("Failed invoking constructor on [%s]", clazz.getCanonicalName());
				throw new QueryInfoRuntimeException(message, e);
			}

			return result;
		}

	}
}
