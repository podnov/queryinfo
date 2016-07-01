package com.evanzeimet.queryinfo.it.generic;

/*
 * #%L
 * queryinfo-integration-tests-war
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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.it.QueryInfoTest;
import com.evanzeimet.queryinfo.jpa.bean.tuple.DefaultTupleQueryInfoBean;
import com.evanzeimet.queryinfo.jpa.bean.tuple.DefaultTupleQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoProvided;
import com.evanzeimet.queryinfo.jpa.result.DefaultTupleToPojoQueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;

@Stateless
public class GenericQueryInfoBean {

	@Inject
	@QueryInfoProvided
	protected QueryInfoEntityContextRegistry entityContextRegistry;

	@Inject
	@QueryInfoTest
	protected EntityManager entityManager;

	protected <E> DefaultTupleQueryInfoBean<E, E> createQueryInfoBean(Class<E> entityClass) {
		QueryInfoResultConverter<Tuple, E> resultConverter = new DefaultTupleToPojoQueryInfoResultConverter<>(entityClass);

		DefaultTupleQueryInfoBeanContext<E, E> context = new DefaultTupleQueryInfoBeanContext<E, E>();

		context.setEntityContextRegistry(entityContextRegistry);
		context.setEntityManager(entityManager);
		context.setResultConverter(resultConverter);
		context.setRootEntityClass(entityClass);

		DefaultTupleQueryInfoBean<E, E> result = new DefaultTupleQueryInfoBean<>();
		result.setBeanContext(context);

		return result;
	}

	public <E> List<E> query(Class<E> entityClass, QueryInfo queryInfo) throws QueryInfoException {
		DefaultTupleQueryInfoBean<E, E> bean = createQueryInfoBean(entityClass);
		return bean.query(queryInfo);
	}

	public <E> E queryForOne(Class<E> entityClass, QueryInfo queryInfo) throws QueryInfoException {
		DefaultTupleQueryInfoBean<E, E> bean = createQueryInfoBean(entityClass);
		return bean.queryForOne(queryInfo);
	}

	public <E> PaginatedResult<E> queryForPaginatedResult(Class<E> entityClass, QueryInfo queryInfo)
			throws QueryInfoException {
		DefaultTupleQueryInfoBean<E, E> bean = createQueryInfoBean(entityClass);
		return bean.queryForPaginatedResult(queryInfo);
	}
}
