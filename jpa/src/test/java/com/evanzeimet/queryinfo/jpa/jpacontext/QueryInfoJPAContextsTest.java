package com.evanzeimet.queryinfo.jpa.jpacontext;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2017 Evan Zeimet
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

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.queryinfo.QueryInfoRuntimeException;

public class QueryInfoJPAContextsTest {

	private QueryInfoJPAContexts<Object, Object> contexts;

	@Before
	public void setUp() {
		contexts = new QueryInfoJPAContexts<>();
	}

	@Test
	public void putNamedSubqueryContext_root() {
		String givenSubqueryName = QueryInfoJPAContexts.ROOT_QUERY_NAME;
		QueryInfoJPAContext<?, ?> givenContext = new QueryInfoJPAContext<>();
		QueryInfoRuntimeException actualException = null;

		try {
			contexts.putNamedSubqueryContext(givenSubqueryName, givenContext);
		} catch (QueryInfoRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);
		Matcher<Collection<? extends Object>> empty = empty();
		assertThat(contexts.namedSubqueryContexts.keySet(), empty);
	}

	@Test
	public void putNamedSubqueryContext_valid() {
		String givenSubqueryName = "mySubqueryName";
		QueryInfoJPAContext<?, ?> givenContext = new QueryInfoJPAContext<>();

		contexts.putNamedSubqueryContext(givenSubqueryName, givenContext);

		QueryInfoJPAContext<?, ?> actual = contexts.getNamedSubqueryContext(givenSubqueryName);
		QueryInfoJPAContext<?, ?> expected = givenContext;

		assertEquals(expected, actual);
	}

}
