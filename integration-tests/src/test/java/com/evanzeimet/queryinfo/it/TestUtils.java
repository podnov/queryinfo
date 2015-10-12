package com.evanzeimet.queryinfo.it;

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
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.it.companies.DefaultCompany;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.DataTable;
import cucumber.runtime.table.TableDiffer;

public class TestUtils {

	private EntityManager entityManager;
	private ObjectMapper objectMapper;

	public TestUtils(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.objectMapper = createObjectMapper();
	}

	public void assertEquals(DataTable expected,
			List<DefaultCompany> actual,
			String[] columnNames) {
		Locale locale = Locale.getDefault();
		DataTable actualDataTable = DataTable.create(actual, locale, columnNames);
		assertEquals(expected, actualDataTable, columnNames);
	}

	public void assertEquals(DataTable expected,
			DataTable actual,
			String[] companiesFields) {
		new TableDiffer(expected, actual).calculateDiffs();
	}

	public static TestUtils create() {
		EntityManager entityManager = createEntityManager();
		return new TestUtils(entityManager);
	}

	public static EntityManager createEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("queryinfo_test_it");
		EntityManager entityManager = factory.createEntityManager();
		return entityManager;
	}

	@SuppressWarnings("deprecation")
	public static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		objectMapper.setDateFormat(dateFormat);

		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter());

		objectMapper.writer(prettyPrinter);

		return objectMapper;
	}

	public String getEntityTable(Class<?> entityClass) {
		String result;
		Table table = entityClass.getAnnotation(Table.class);

		if (table == null) {
			String message = String.format("Could not find table information on [%s]",
					entityClass.getCanonicalName());
			throw new QueryInfoRuntimeException(message);
		} else {
			result = table.name();
		}

		return result;
	}

	public <T> T objectify(String json, Class<T> clazz) throws QueryInfoException {
		T result;

		try {
			result = objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new QueryInfoException(e);
		}

		return result;
	}

	public <T> T objectify(String json, Type typeOfT) throws QueryInfoException {
		T result;

		try {
			JavaType javaType = objectMapper.constructType(typeOfT);
			result = objectMapper.readValue(json, javaType);
		} catch (IOException e) {
			throw new QueryInfoException(e);
		}

		return result;
	}

	public void persistEntities(List<?> entities) {
		if ((entities != null) && !entities.isEmpty()) {
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();

			for (Object entity : entities) {
				entityManager.persist(entity);
			}

			transaction.commit();
		}
	}

	public String stringify(Object object) throws QueryInfoException {
		String result;

		try {
			result = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new QueryInfoException(e);
		}

		return result;
	}

	public void truncateTable(Class<?> entityClass) {
		String tableName = getEntityTable(entityClass);
		truncateTable(tableName);
	}

	public void truncateTable(String tableName) {
		String sql = String.format("truncate table %s", tableName);

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.createNativeQuery(sql).executeUpdate();

		transaction.commit();
	}
}
