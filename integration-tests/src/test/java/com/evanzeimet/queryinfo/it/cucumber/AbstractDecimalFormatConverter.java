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


import java.text.DecimalFormat;

import cucumber.deps.com.thoughtworks.xstream.converters.Converter;
import cucumber.deps.com.thoughtworks.xstream.converters.MarshallingContext;
import cucumber.deps.com.thoughtworks.xstream.converters.UnmarshallingContext;
import cucumber.deps.com.thoughtworks.xstream.io.HierarchicalStreamReader;
import cucumber.deps.com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public abstract class AbstractDecimalFormatConverter implements Converter {

	private CucumberUtils cucumberUtils;
	private DecimalFormat decimalFormat;

	public AbstractDecimalFormatConverter(String pattern) {
		cucumberUtils = new CucumberUtils();
		decimalFormat = new DecimalFormat(pattern);
	}

	@Override
	public void marshal(Object object,
			HierarchicalStreamWriter writer,
			MarshallingContext context) {
		String marshalledValue = decimalFormat.format(object);
		writer.setValue(marshalledValue);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Object result;
		String value = reader.getValue();

		if (cucumberUtils.isDataTableNull(value)) {
			result = null;
		} else {
			result = unmarshalNonNull(reader, context);
		}

		return result;
	}

	protected abstract Object unmarshalNonNull(HierarchicalStreamReader reader,
			UnmarshallingContext context);
}
