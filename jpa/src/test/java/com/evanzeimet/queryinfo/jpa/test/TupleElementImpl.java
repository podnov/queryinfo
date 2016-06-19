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


import javax.persistence.TupleElement;

public class TupleElementImpl<X> implements TupleElement<X> {

	private String alias;
	private Class<? extends X> javaType;

	public TupleElementImpl(String alias, Class<? extends X> javaType) {
		this.alias = alias;
		this.javaType = javaType;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public Class<? extends X> getJavaType() {
		return javaType;
	}

}
