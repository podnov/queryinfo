package com.evanzeimet.queryinfo.jpa.test.entity;

/*
 * #%L
 * queryinfo-jpa
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

import java.util.ArrayList;
import java.util.List;

import com.evanzeimet.queryinfo.jpa.entity.DefaultQueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContext;


public class TestQueryInfoEntityContextRegistry extends DefaultQueryInfoEntityContextRegistry {

    protected TestQueryInfoEntityContextRegistry(List<QueryInfoEntityContext<?>> entityContexts) {
        super(entityContexts);
    }

    public static TestQueryInfoEntityContextRegistry create() {
        List<QueryInfoEntityContext<?>> entityContexts = new ArrayList<QueryInfoEntityContext<?>>();

        entityContexts.add(new TestEmployeeEntityContext());
        entityContexts.add(new TestOrganizationEntityContext());
        entityContexts.add(new TestPersonEntityContext());

        return new TestQueryInfoEntityContextRegistry(entityContexts);
    }
}
