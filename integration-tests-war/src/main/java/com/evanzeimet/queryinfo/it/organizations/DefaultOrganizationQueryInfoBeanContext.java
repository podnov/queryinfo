package com.evanzeimet.queryinfo.it.organizations;

import javax.annotation.PostConstruct;

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


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import com.evanzeimet.queryinfo.it.QueryInfoEntityManager;
import com.evanzeimet.queryinfo.jpa.bean.entity.AbstractEntityQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;

@Stateless
public class DefaultOrganizationQueryInfoBeanContext
		extends AbstractEntityQueryInfoBeanContext<OrganizationEntity>
		implements OrganizationQueryInfoBeanContext {

	@Inject
	@QueryInfoEntityManager
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<OrganizationEntity> getRootEntityClass() {
		return OrganizationEntity.class;
	}

	@PostConstruct
	@Inject
	protected void postConstruct(QueryInfoEntityContextRegistry entityContextRegistry) {
		setEntityContextRegistry(entityContextRegistry);
	}
}
