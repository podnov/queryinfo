package com.evanzeimet.queryinfo.it.people;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

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

import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.it.PeopleQueryInfoBeanSelector;
import com.evanzeimet.queryinfo.it.QueryInfoITUtils;

@Stateless
public class PeopleResourceBean implements PeopleResource {

	@Inject
	private Logger logger;

	@Inject
	private PeopleQueryInfoBeanSelector peopleQueryInfoBean;

	@Override
	public Response query(DefaultQueryInfo queryInfo) {
		Response result;

		try {
			List<?> people = peopleQueryInfoBean.query(queryInfo);
			result = Response.ok(people).build();
		} catch (QueryInfoException e) {
			logger.error("Could not retrieve people", e);
			result = QueryInfoITUtils.createUnprocessableEntityResponse();
		}

		return result;
	}

}
