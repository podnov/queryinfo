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


import javax.persistence.Tuple;

import com.evanzeimet.queryinfo.jpa.bean.DefaultTupleQueryInfoBean;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBeanContext;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DefaultTupleToJSONQueryInfoBean<RootEntity> extends DefaultTupleQueryInfoBean<RootEntity, ObjectNode> {

	public DefaultTupleToJSONQueryInfoBean() {
		super();
	}

	public DefaultTupleToJSONQueryInfoBean(QueryInfoBeanContext<RootEntity, Tuple, ObjectNode> context) {
		super(context);
	}
}
