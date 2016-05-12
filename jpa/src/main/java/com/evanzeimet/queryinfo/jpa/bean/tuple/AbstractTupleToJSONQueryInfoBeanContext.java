package com.evanzeimet.queryinfo.jpa.bean.tuple;

/*
 * #%L
 * queryinfo-jpa
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

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.result.TupleToObjectNodeQueryInfoResultConverter;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractTupleToJSONQueryInfoBeanContext<RootEntity>
		extends AbstractTupleQueryInfoBeanContext<RootEntity, ObjectNode> {

	private final QueryInfoResultConverter<Tuple, ObjectNode> resultConverter = new TupleToObjectNodeQueryInfoResultConverter();

	public AbstractTupleToJSONQueryInfoBeanContext() {
		super();
	}

	public AbstractTupleToJSONQueryInfoBeanContext(EntityManager entityManager) {
		super(entityManager);
	}

	public AbstractTupleToJSONQueryInfoBeanContext(EntityManager entityManager,
			QueryInfoEntityContextRegistry entityContextRegistry) {
		super(entityManager, entityContextRegistry);
	}

	@Override
	public QueryInfoResultConverter<Tuple, ObjectNode> getResultConverter() {
		return resultConverter;
	}

}
