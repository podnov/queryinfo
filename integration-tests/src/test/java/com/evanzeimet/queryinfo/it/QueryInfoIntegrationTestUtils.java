package com.evanzeimet.queryinfo.it;

import java.lang.reflect.Type;

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


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.QueryInfoTestUtils;

public class QueryInfoIntegrationTestUtils {

	private EntityManager entityManager;

	public QueryInfoIntegrationTestUtils(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public static QueryInfoIntegrationTestUtils create() {
		EntityManager entityManager = createEntityManager();
		return new QueryInfoIntegrationTestUtils(entityManager);
	}

	public static EntityManager createEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("queryinfo_test_it");
		EntityManager entityManager = factory.createEntityManager();
		return entityManager;
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

	public <T> T objectify(String json, Class<T> clazz)
			throws QueryInfoException {
		return QueryInfoTestUtils.objectify(json, clazz);
	}

	public <T> T objectify(String json, Type typeOfT)
			throws QueryInfoException {
		return QueryInfoTestUtils.objectify(json, typeOfT);
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
