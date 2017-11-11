package com.evanzeimet.queryinfo.it.feature;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import cucumber.api.DataTable;
import cucumber.runtime.table.TableConverter;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;

public class DataTableUtils {

	public static final String DATA_TABLE_NULL_PLACEHOLDER = "[[NULL]]";
	public static final String DATA_TABLE_OBJECT_PLACEHOLDER = "[[ANY_OBJECT]]";

	private static final Integer FAKE_DATA_TABLE_LINE_NUMBER = -1;
	private static final List<Comment> FAKE_DATA_TABLE_ROW_COMMENTS = Collections.emptyList();

	private ObjectMapper objectMapper = new ObjectMapper();
	private Boolean stringifyNestedJsonObjects = false;
	private TableConverter tableConverter;

	public DataTableUtils() {

	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public Boolean getStringifyNestedJsonObjects() {
		return stringifyNestedJsonObjects;
	}

	public void setStringifyNestedJsonObjects(Boolean stringifyNestedJsonObjects) {
		this.stringifyNestedJsonObjects = stringifyNestedJsonObjects;
	}

	public TableConverter getTableConverter() {
		return tableConverter;
	}

	public void setTableConverter(TableConverter tableConverter) {
		this.tableConverter = tableConverter;
	}

	protected DataTableRow createDataTableRow(List<String> cells) {
		return new DataTableRow(FAKE_DATA_TABLE_ROW_COMMENTS, cells, FAKE_DATA_TABLE_LINE_NUMBER);
	}

	protected List<String> createJsonArrayContentColumnNames(ArrayNode arrayNode) {
		List<String> result = new ArrayList<>();
		Iterator<JsonNode> arrayElements = arrayNode.iterator();

		if (arrayElements.hasNext()) {
			JsonNode arrayElement = arrayElements.next();
			Iterator<String> fieldNames = arrayElement.fieldNames();

			while (fieldNames.hasNext()) {
				String fieldName = fieldNames.next();
				result.add(fieldName);
			}
		}

		return result;
	}

	protected String createJsonNodeCell(JsonNode jsonNode, String columnName) {
		String cell;
		JsonNode columnNode = jsonNode.get(columnName);

		if (columnNode.isObject()) {
			if (stringifyNestedJsonObjects) {
				cell = columnNode.toString();
			} else {
				cell = DATA_TABLE_OBJECT_PLACEHOLDER;
			}
		} else if (columnNode.isNull()) {
			cell = DATA_TABLE_NULL_PLACEHOLDER;
		} else {
			cell = columnNode.asText();
		}
		return cell;
	}

	protected List<String> createJsonNodeCells(JsonNode jsonNode, List<String> columnNames) {
		int columnCount = columnNames.size();
		List<String> row = new ArrayList<>(columnCount);
		String cell;

		for (String columnName : columnNames) {
			cell = createJsonNodeCell(jsonNode, columnName);
			row.add(cell);
		}

		return row;
	}

	public List<String> getColumnNames(DataTable dataTable) {
		List<DataTableRow> gherkinRows = dataTable.getGherkinRows();
		DataTableRow firstRow = gherkinRows.get(0);
		return firstRow.getCells();
	}

	public boolean isDataTableNull(String value) {
		boolean result;

		if (DATA_TABLE_NULL_PLACEHOLDER.equalsIgnoreCase(value)) {
			result = true;
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * Read json string as array. Use first row as reference for data table
	 * columns.
	 */
	public DataTable readJsonArray(String json) throws JsonProcessingException, IOException {
		ArrayNode arrayNode = readJsonAsArrayNode(json);
		List<String> columnNames = createJsonArrayContentColumnNames(arrayNode);

		return readJsonArrayNode(arrayNode, columnNames);
	}

	/**
	 * Read json string as array. Use first row of reference data table for data
	 * table columns.
	 */
	public DataTable readJsonArray(String json, DataTable referenceDataTable)
			throws JsonProcessingException, IOException {
		List<DataTableRow> dataTableRows = referenceDataTable.getGherkinRows();
		List<String> columnNames;

		if (dataTableRows.isEmpty()) {
			String message = "No reference header found";
			throw new IOException(message);
		} else {
			DataTableRow header = dataTableRows.iterator().next();
			columnNames = header.getCells();
		}

		return readJsonArray(json, columnNames);
	}

	public DataTable readJsonArray(String json, List<String> columnNames) throws JsonProcessingException, IOException {
		ArrayNode arrayNode = readJsonAsArrayNode(json);
		return readJsonArrayNode(arrayNode, columnNames);
	}

	protected DataTable readJsonArrayNode(ArrayNode arrayNode, List<String> columnNames) {
		List<DataTableRow> dataTableRows = new ArrayList<>();

		Iterator<JsonNode> arrayElements = arrayNode.iterator();

		DataTableRow header = createDataTableRow(columnNames);
		dataTableRows.add(header);

		while (arrayElements.hasNext()) {
			JsonNode arrayElement = arrayElements.next();

			List<String> cells = createJsonNodeCells(arrayElement, columnNames);
			DataTableRow dataTableRow = createDataTableRow(cells);

			dataTableRows.add(dataTableRow);
		}

		return new DataTable(dataTableRows, tableConverter);
	}

	protected ArrayNode readJsonAsArrayNode(String json) throws IOException, JsonProcessingException {
		JsonNode jsonNode = objectMapper.readTree(json);

		if (!jsonNode.isArray()) {
			String message = String.format("Expected json array, got [%s]", json);
			throw new IOException(message);
		}

		return (ArrayNode) jsonNode;
	}
}
