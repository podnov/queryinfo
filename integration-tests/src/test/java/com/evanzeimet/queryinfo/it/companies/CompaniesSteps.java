package com.evanzeimet.queryinfo.it.companies;

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


import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.List;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.it.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CompaniesSteps {

	private static final Type COMPANIES_LIST_RESULT_TYPE = new TypeReference<List<DefaultCompany>>() {
	}.getType();

	private Response actualResponse;
	private TestUtils testUtils;

	public CompaniesSteps() {
		setUpRestAssured();
		testUtils = TestUtils.create();
	}

	protected void setUpRestAssured() {
		RestAssured.basePath = "queryinfo-integration-tests";
	}

	@Given("^these companies:$")
	public void Given_these_companies(List<CompanyEntity> companies) {
		testUtils.persistEntities(companies);
	}

	@Given("^the companies query info web service$")
	public void Given_the_companies_query_info_web_service() {
		// no-op
	}

	@When("^I send the query:$")
	public void When_I_send_the_query(String rawQueryInfo) throws QueryInfoException {
		actualResponse = given()
				.contentType(ContentType.JSON)
				.body(rawQueryInfo)
				.when()
				.post("/companies");

	}

	@Then("^the http response code should be (\\d*)$")
	public void Then_the_http_response_code_should_be(int expected) {
		int actual = actualResponse.getStatusCode();

		assertEquals(expected, actual);
	}

	@Then("^I should receive these companies:$")
	public void Then_I_should_receive_these_companies(DataTable expected)
			throws QueryInfoException {
		String actualReponseJson = actualResponse.getBody().asString();

		List<DefaultCompany> actual = testUtils.objectify(actualReponseJson,
				COMPANIES_LIST_RESULT_TYPE);

		testUtils.assertEquals(expected, actual);
	}
}
