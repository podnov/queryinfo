package com.evanzeimet.queryinfo.jpa.field;

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
import java.util.Arrays;
import java.util.List;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.StringUtils;

public class QueryInfoFieldNameBuilder<T> {

	private final List<String> attributeNames;

	public QueryInfoFieldNameBuilder(PluralAttribute<?, ?, T> attribute) {
		attributeNames = createAttributeNamesForRootAttribute(attribute);
	}

	public QueryInfoFieldNameBuilder(SingularAttribute<?, T> attribute) {
		attributeNames = createAttributeNamesForRootAttribute(attribute);
	}

	protected QueryInfoFieldNameBuilder(List<String> parents, PluralAttribute<?, ?, T> attribute) {
		attributeNames = createAttributeNamesForChildAttribute(parents, attribute);
	}

	protected QueryInfoFieldNameBuilder(List<String> parents, SingularAttribute<?, T> attribute) {
		attributeNames = createAttributeNamesForChildAttribute(parents, attribute);
	}

	public <NT> QueryInfoFieldNameBuilder<NT> add(PluralAttribute<? super T, ?, NT> attribute) {
		return new QueryInfoFieldNameBuilder<NT>(attributeNames, attribute);
	}

	public <NT> QueryInfoFieldNameBuilder<NT> add(SingularAttribute<? super T, NT> attribute) {
		return new QueryInfoFieldNameBuilder<NT>(attributeNames, attribute);
	}

	public String build() {
		return StringUtils.join(attributeNames, ".");
	}

	public static <T> QueryInfoFieldNameBuilder<T> create(SingularAttribute<?, T> rootAttribute) {
		return new QueryInfoFieldNameBuilder<T>(rootAttribute);
	}

	public static <T> QueryInfoFieldNameBuilder<T> create(PluralAttribute<?, ?, T> rootAttribute) {
		return new QueryInfoFieldNameBuilder<T>(rootAttribute);
	}

	protected List<String> createAttributeNamesForChildAttribute(List<String> parents,
			Attribute<?, ?> attribute) {
		int attributeNameCount = (parents.size() + 1);
		List<String> result = new ArrayList<String>(attributeNameCount);
		result.addAll(parents);

		String attributeName = attribute.getName();
		result.add(attributeName);

		return result;
	}

	protected List<String> createAttributeNamesForRootAttribute(Attribute<?, ?> attribute) {
		String attributeName = attribute.getName();
		return Arrays.asList(attributeName);
	}
}
