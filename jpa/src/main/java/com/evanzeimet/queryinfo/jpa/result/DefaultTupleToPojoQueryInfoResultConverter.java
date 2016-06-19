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

import com.evanzeimet.queryinfo.QueryInfoRuntimeException;

public class DefaultTupleToPojoQueryInfoResultConverter<T>
		extends AbstractTupleToPojoQueryInfoResultConverter<T> {

	public DefaultTupleToPojoQueryInfoResultConverter(Class<T> resultClass) {
		super(resultClass, new DefaultBaseInstanceFactory<T>(resultClass));
	}

	public DefaultTupleToPojoQueryInfoResultConverter(Class<T> resultClass,
			QueryInfoBaseInstanceFactory<T> baseInstanceFactory) {
		super(resultClass, baseInstanceFactory);
	}

	private static class DefaultBaseInstanceFactory<T> implements QueryInfoBaseInstanceFactory<T> {

		private Class<T> clazz;

		public DefaultBaseInstanceFactory(Class<T> clazz) {
			this.clazz = clazz;
		}

		@Override
		public T create() {
			T result;

			try {
				result = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				String message = String.format("Failed invoking newInstance on [%s]", clazz.getCanonicalName());
				throw new QueryInfoRuntimeException(message, e);
			}

			return result;
		}

	}
}
