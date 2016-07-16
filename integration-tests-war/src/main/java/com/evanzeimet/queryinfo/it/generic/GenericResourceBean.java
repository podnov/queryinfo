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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.it.QueryInfoITUtils;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;

@Stateless
public class GenericResourceBean implements GenericResource {

	@Inject
	protected GenericQueryInfoBean genericQueryInfoBean;

	@Inject
	protected Logger logger;

	@Inject
	protected EntityURIToClassMap uriToClassMap;

	@Override
	public Response query(String entityUri, DefaultQueryInfo queryInfo) {
		Response result;
		Class<?> entityClass = uriToClassMap.get(entityUri);

		if (entityClass == null) {
			result = Response.status(Status.BAD_REQUEST).build();
		} else {
			PaginatedResult<?> queryResults = null;
			QueryInfoException exception = null;

			try {
				queryResults = genericQueryInfoBean.queryForPaginatedResult(entityClass, queryInfo);
			} catch (QueryInfoException e) {
				exception = e;
			}

			if (exception == null) {
				result = Response.ok(queryResults).build();
			} else {
				logger.error("Query failed", exception);
				result = QueryInfoITUtils.createUnprocessableEntityResponse();
			}
		}

		return result;
	}

}
