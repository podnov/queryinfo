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

import javax.ejb.Stateless;
import javax.inject.Inject;
import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity;
import com.evanzeimet.queryinfo.jpa.bean.tuple.DefaultTupleQueryInfoBean;

@Stateless
public class OrganizationEmployeeQueryInfoBean
	extends DefaultTupleQueryInfoBean<OrganizationEntity, DefaultOrganizationEmployee> {

	@Inject
	public void setBeanContext(OrganizationEmployeeQueryInfoBeanContext beanContext) {
		super.setBeanContext(beanContext);
	}

}
