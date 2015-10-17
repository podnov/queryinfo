package com.evanzeimet.queryinfo.jpa.join;

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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.JoinType;

import org.junit.Test;

public class QueryInfoJoinTypeTest {

	@Test
	public void toJpaType() {
		Throwable throwable = null;
		Map<QueryInfoJoinType, JoinType> typeMap = new HashMap<>();

		try {
			for (QueryInfoJoinType queryInfoJoinType : QueryInfoJoinType.values()) {
				JoinType jpaJoinType = queryInfoJoinType.toJpaType();
				typeMap.put(queryInfoJoinType, jpaJoinType);
			}
		} catch (Throwable t) {
			throwable = t;
		}

		assertNull(throwable);

		JoinType actual = typeMap.get(QueryInfoJoinType.INNER);
		JoinType expected = JoinType.INNER;

		assertEquals(expected, actual);

		actual = typeMap.get(QueryInfoJoinType.LEFT);
		expected = JoinType.LEFT;

		assertEquals(expected, actual);

		actual = typeMap.get(QueryInfoJoinType.RIGHT);
		expected = JoinType.RIGHT;

		assertEquals(expected, actual);

		actual = typeMap.get(QueryInfoJoinType.UNSPECIFIED);
		expected = JoinType.INNER;

		assertEquals(expected, actual);
	}
}
