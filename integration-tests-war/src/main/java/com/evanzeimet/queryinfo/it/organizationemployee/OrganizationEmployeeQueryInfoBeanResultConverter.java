package com.evanzeimet.queryinfo.it.organizationemployee;

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


import com.evanzeimet.queryinfo.jpa.result.AbstractTupleToPojoQueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoBaseInstanceFactory;

public class OrganizationEmployeeQueryInfoBeanResultConverter
		extends AbstractTupleToPojoQueryInfoResultConverter<OrganizationEmployee> {

	public OrganizationEmployeeQueryInfoBeanResultConverter() {
		super(DefaultOrganizationEmployee.class, new BaseInstanceFactory());
	}

	private static class BaseInstanceFactory implements QueryInfoBaseInstanceFactory<OrganizationEmployee> {

		@Override
		public OrganizationEmployee create() {
			return new DefaultOrganizationEmployee();
		}

	}
}