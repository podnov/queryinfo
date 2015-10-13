package com.evanzeimet.queryinfo.jpa.predicate;

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

/**
 * It appears that hibernate parses at least numeric types out of the box:
 *
 * <pre>
 * http://grepcode.com/file/repo1.maven.org/maven2/org.hibernate/hibernate-entitymanager/4.3.10.Final/org/hibernate/jpa/criteria/predicate/ComparisonPredicate.java#ComparisonPredicate.%3Cinit%3E%28org.hibernate.jpa.criteria.CriteriaBuilderImpl%2Corg.hibernate.jpa.criteria.predicate.ComparisonPredicate.ComparisonOperator%2Cjavax.persistence.criteria.Expression%2Cjava.lang.Object%29
 * </pre>
 *
 * Let's parse everything else.
 */
import javax.persistence.criteria.Expression;

public class FieldValueParser {

	// TODO datetime parsing

	public Object parse(Expression<?> path, String fieldValue) {
		Object result;
		Class<?> javaType = path.getJavaType();

		if (Boolean.class.isAssignableFrom(javaType)) {
			result = parseBoolean(fieldValue);
		} else {
			result = fieldValue;
		}

		return result;
	}

	protected Boolean parseBoolean(String fieldValue) {
		return Boolean.valueOf(fieldValue);
	}

}
