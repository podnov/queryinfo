package com.evanzeimet.queryinfo.it.people;

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

@Stateless
public class DefaultPeopleQueryInfoBeanContext extends AbstractEntityQueryInfoBeanContext<PersonEntity>
		implements PeopleQueryInfoBeanContext {

	@Inject
	@QueryInfoEntityManager
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Class<PersonEntity> getRootEntityClass() {
		return PersonEntity.class;
	}
}
