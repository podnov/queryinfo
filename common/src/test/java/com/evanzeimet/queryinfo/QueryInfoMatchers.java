package com.evanzeimet.queryinfo;

/*
 * #%L
 * queryinfo-common
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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.evanzeimet.queryinfo.condition.Condition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

public class QueryInfoMatchers {

	public static TypeSafeMatcher<Condition> equalsCondition(final Condition expected) {
		return new TypeSafeMatcher<Condition>() {

			private boolean leftHandSideMatches;
			private boolean operatorMatches;
			private boolean rightHandSideMatches;

			@Override
			protected boolean matchesSafely(Condition actual) {
				String expectedLhs = expected.getLeftHandSide();
				String actualLhs = actual.getLeftHandSide();

				leftHandSideMatches = matchString(expectedLhs, actualLhs);

				String expectedOperator = expected.getOperator();
				String actualOperator = actual.getOperator();
				operatorMatches = matchString(expectedOperator, actualOperator);


				String expectedRhs = expected.getRightHandSide().toString();
				String actualRhs = actual.getRightHandSide().toString();
				rightHandSideMatches = matchString(expectedRhs, actualRhs);

				return (leftHandSideMatches && operatorMatches && rightHandSideMatches);
			}

			@Override
			protected void describeMismatchSafely(Condition actual, Description description) {
				boolean hasMismatch = false;

				if (!leftHandSideMatches) {
					description.appendText("left hand side was ")
						.appendValue(actual.getLeftHandSide());
					hasMismatch = true;
				}

				if (!operatorMatches) {
					if (hasMismatch) {
						description.appendText(", ");
					}

					description.appendText("operator was ")
						.appendValue(actual.getOperator());

					hasMismatch = true;
				}

				if (!rightHandSideMatches) {
					if (hasMismatch) {
						description.appendText(", ");
					}

					description.appendText("right hand side was ")
						.appendValue(actual.getRightHandSide());

					hasMismatch = true;
				}
			}

			@Override
			public void describeTo(Description description) {
				boolean hasMismatch = false;

				if (!leftHandSideMatches) {
					description.appendText("left hand side to equal ")
						.appendValue(expected.getLeftHandSide());
					hasMismatch = true;
				}

				if (!operatorMatches) {
					if (hasMismatch) {
						description.appendText(", ");
					}

					description.appendText("operator to equal ")
						.appendValue(expected.getOperator());

					hasMismatch = true;
				}

				if (!rightHandSideMatches) {
					if (hasMismatch) {
						description.appendText(", ");
					}

					description.appendText("right hand side to equal ")
						.appendValue(expected.getRightHandSide());
					hasMismatch = true;
				}
			}
		};
	}

	public static TypeSafeMatcher<Object> equalsJson(final Object expected) {
		return new TypeSafeMatcher<Object>() {

			private String actualJson;
			private String expectedJson;

			@Override
			protected void describeMismatchSafely(Object item, Description description) {
				description.appendText("was ").appendValue(actualJson);
			}

			@Override
			public void describeTo(Description description) {
				description.appendValue(expectedJson);
			}

			@Override
			protected boolean matchesSafely(Object actual) {
				boolean result = false;
				ObjectWriter objectWriter = QueryInfoTestUtils.createObjectWriter();

				try {
					actualJson = objectWriter.writeValueAsString(actual);
					expectedJson = objectWriter.writeValueAsString(expected);

					result = expectedJson.equals(actualJson);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					result = false;
				}

				return result;
			}
		};
	}

	protected static boolean matchString(String expected, String actual) {
		boolean result;

		if (expected == null) {
			result = (actual == null);
		} else {
			result = expected.equals(actual);
		}

		return result;
	}
}
