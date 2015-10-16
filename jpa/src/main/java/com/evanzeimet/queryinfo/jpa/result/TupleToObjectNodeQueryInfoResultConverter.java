package com.evanzeimet.queryinfo.jpa.result;

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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TupleToObjectNodeQueryInfoResultConverter
		implements QueryInfoResultConverter<Tuple, ObjectNode> {

	protected QueryInfoUtils queryInfoUtils = new QueryInfoUtils();

	public TupleToObjectNodeQueryInfoResultConverter() {

	}

	@Override
	public List<ObjectNode> convert(List<Tuple> tuples) {
		int tupleCount = tuples.size();
		List<ObjectNode> result = new ArrayList<>(tupleCount);
		ObjectMapper objectMapper = queryInfoUtils.createObjectMapper();

		for (Tuple tuple : tuples) {
			ObjectNode jsonObject = convertTuple(tuple, objectMapper);
			result.add(jsonObject);
		}

		return result;
	}

	protected ObjectNode convertTuple(Tuple tuple, ObjectMapper objectMapper) {
		ObjectNode objectNode = objectMapper.createObjectNode();

		List<TupleElement<?>> elements = tuple.getElements();

		for (TupleElement<?> element : elements) {
			String fieldName = element.getAlias();
			Object fieldValue = tuple.get(element);

			objectNode.putPOJO(fieldName, fieldValue);
		}

		return objectNode;
	}

}
