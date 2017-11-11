package com.evanzeimet.queryinfo.it.cucumber;

import java.io.IOException;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.evanzeimet.queryinfo.it.feature.DataTableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import cucumber.api.DataTable;
import cucumber.api.Transformer;
import cucumber.deps.com.thoughtworks.xstream.XStream;
import cucumber.deps.com.thoughtworks.xstream.converters.Converter;
import cucumber.runtime.ParameterInfo;
import cucumber.runtime.table.TableConverter;
import cucumber.runtime.table.TableDiffer;
import cucumber.runtime.xstream.LocalizedXStreams;
import cucumber.runtime.xstream.LocalizedXStreams.LocalizedXStream;

public class CucumberUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public CucumberUtils() {

	}

	public void assertEquals(DataTable expected,
			List<?> actual,
			String[] columnNames) {
		DataTable actualDataTable = convertListToDataTable(actual, columnNames);
		assertEquals(expected, actualDataTable);
	}

	public void assertEquals(DataTable expected,
			DataTable actual) {
		new TableDiffer(expected, actual).calculateDiffs();
	}

	public DataTable convertListToDataTable(List<?> actual, List<String> columnNames) {
		String[] columnNameArray = columnNames.toArray(new String[0]);
		return convertListToDataTable(actual, columnNameArray);
	}
	
	public DataTable convertListToDataTable(List<?> actual, String[] columnNames) {
		TableConverter tableConverter = createTableConverter();
		return tableConverter.toTable(actual, columnNames);
	}

	protected LocalizedXStream createBaseXStream() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Locale locale = Locale.getDefault();
		return new LocalizedXStreams(classLoader).get(locale);
	}

	protected ParameterInfo createParameterInfo() {
		Type type = null;
		String delimiter = null;
		Transformer<?> transformer = null;

		return new ParameterInfo(type, DATE_FORMAT, delimiter, transformer);
	}

	public TableConverter createTableConverter() {
		LocalizedXStream xStream = createrXStream();
		ParameterInfo parameterInfo = createParameterInfo();

		return new TableConverter(xStream, parameterInfo);
	}

	protected LocalizedXStream createrXStream() {
		LocalizedXStream result = createBaseXStream();

		List<Converter> converters = createXStreamConverters();

		for (Converter converter : converters) {
			result.registerConverter(converter, XStream.PRIORITY_VERY_HIGH);
		}

		return result;
	}

	protected List<Converter> createXStreamConverters() {
		List<Converter> result = new ArrayList<>();

		result.add(new BigDecimalConverter());
		result.add(new BooleanConverter());
		result.add(new DoubleConverter());
		result.add(new IntegerConverter());
		result.add(new LongConverter());

		return result;
	}

	/**
	 * Invoke {@link DataTableUtils#readJsonArray(String)} with default table converter.
	 *
	 * @param json
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public DataTable readJsonArray(String json) throws JsonProcessingException, IOException {
		TableConverter tableConverter = createTableConverter();

		DataTableUtils dataTableUtils = new DataTableUtils();
		dataTableUtils.setTableConverter(tableConverter);

		return dataTableUtils.readJsonArray(json);
	}
}
