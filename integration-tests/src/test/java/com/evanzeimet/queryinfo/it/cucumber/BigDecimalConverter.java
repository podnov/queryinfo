package com.evanzeimet.queryinfo.it.cucumber;

/*
 * #%L
 * queryinfo-integration-tests
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


import java.math.BigDecimal;
import cucumber.deps.com.thoughtworks.xstream.converters.UnmarshallingContext;
import cucumber.deps.com.thoughtworks.xstream.io.HierarchicalStreamReader;


public class BigDecimalConverter extends AbstractDecimalFormatConverter {

	public BigDecimalConverter() {
		this("#0.0");
	}

	public BigDecimalConverter(String pattern) {
		super(pattern);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return BigDecimal.class.isAssignableFrom(clazz);
	}

	@Override
	protected Object unmarshalNonNull(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		String value = reader.getValue();
		return new BigDecimal(value);
	}
}
