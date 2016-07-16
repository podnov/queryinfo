package com.evanzeimet.queryinfo.it.feature;

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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.it.QueryInfoIntegrationTestUtils;
import com.evanzeimet.queryinfo.it.cucumber.CucumberUtils;
import com.evanzeimet.queryinfo.it.organizations.DefaultOrganization;
import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity;
import com.evanzeimet.queryinfo.it.people.DefaultPerson;
import com.evanzeimet.queryinfo.it.people.PersonEntity;
import com.evanzeimet.queryinfo.it.people.PersonToEmployerOrganizationIdMapper;
import com.evanzeimet.queryinfo.it.people.TestPerson;
import com.evanzeimet.queryinfo.pagination.DefaultPaginatedResult;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import cucumber.api.DataTable;
import cucumber.api.Format;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class QueryInfoSteps {

	private static final String[] ORGANIZATION_FIELDS = new String[] {
			"name",
			"address1",
			"address2",
			"city",
			"state",
			"zip",
			"yearFounded",
			"active"
	};

	private static final String[] PERSON_FIELDS = new String[] {
			"firstName",
			"lastName"
	};

	private static final Type ORGANIZATION_LIST_RESULT_TYPE = new TypeReference<List<DefaultOrganization>>() {
	}.getType();

	private static final Type ORGANIZATION_PAGINATED_RESULT_TYPE = new TypeReference<DefaultPaginatedResult<DefaultOrganization>>() {
	}.getType();

	private static final Type PERSON_LIST_RESULT_TYPE = new TypeReference<List<DefaultPerson>>() {
	}.getType();

	private static final Type PERSON_PAGINATED_RESULT_TYPE = new TypeReference<DefaultPaginatedResult<DefaultPerson>>() {
	}.getType();

	private static boolean needToPersistOrganizations = true;
	private static boolean needToPersistPeople = true;

	private Response actualResponse;
	private final CucumberUtils cucumberUtils;
	private String path;
	private final PersonToEmployerOrganizationIdMapper personToEmployerOrganizationIdMapper = new PersonToEmployerOrganizationIdMapper();
	private final QueryInfoIntegrationTestUtils testUtils;

	public QueryInfoSteps() {
		setUpRestAssured();
		cucumberUtils = new CucumberUtils();
		testUtils = QueryInfoIntegrationTestUtils.create();
	}

	protected void setUpRestAssured() {
		RestAssured.basePath = "queryinfo-it";
	}

	@Given("^these organizations:$")
	public void Given_these_organizations(@Format(CucumberUtils.DATE_FORMAT) List<OrganizationEntity> organizations) {
		if (needToPersistOrganizations) {
			needToPersistOrganizations = false;
			testUtils.truncateTable(OrganizationEntity.class);
			testUtils.persistEntities(organizations);

			personToEmployerOrganizationIdMapper.mapReferenceIds(organizations, organizations);
		}
	}

	@Given("^these people:$")
	public void Given_these_people(List<TestPerson> people) {
		if (needToPersistPeople) {
			needToPersistPeople = false;

			List<PersonEntity> personEntities = createPersonEntities(people);

			testUtils.truncateTable(PersonEntity.class);
			testUtils.persistEntities(personEntities);
		}
	}

	@Given("^the generic query info web service for the \"([^\"]*)\"$")
	public void Given_the_generic_query_info_web_service_for_the__(String entityUri) {
		path = "/generic/" + entityUri;
	}

	@Given("^the organization employees query info web service$")
	public void Given_the_organization_employees_query_info_web_service() {
		path = "/organizationemployees";
	}

	@Given("^the organizations query info web service$")
	public void Given_the_organizations_query_info_web_service() {
		path = "/organizations";
	}

	@Given("^the people query info web service$")
	public void Given_the_people_query_info_web_service() {
		path = "/people";
	}

	@When("^I send the query:$")
	public void When_I_send_the_query(String rawQueryInfo) throws QueryInfoException {
		actualResponse = given()
				.contentType(ContentType.JSON)
				.body(rawQueryInfo)
				.when()
				.post(path);
	}

	@Then("^the http response code should be (\\d*)$")
	public void Then_the_http_response_code_should_be(int expected) {
		int actual = actualResponse.getStatusCode();

		assertEquals(expected, actual);
	}

	@Then("^I should receive this json:$")
	public void I_should_receive_this_json(String expected)
			throws QueryInfoException {
		String actual = actualResponse.getBody().asString();
		actual = testUtils.formatJson(actual);

		assertEquals(expected, actual);
	}

	@Then("^I should receive these paginated organizations:$")
	public void Then_I_should_receive_these_paginated_organizations(DataTable expected)
			throws QueryInfoException {
		String actualReponseJson = actualResponse.getBody().asString();

		PaginatedResult<DefaultOrganization> actualPaginatedResult = testUtils.objectify(actualReponseJson,
				ORGANIZATION_PAGINATED_RESULT_TYPE);

		List<DefaultOrganization> actual = actualPaginatedResult.getPageResults();

		cucumberUtils.assertEquals(expected, actual, ORGANIZATION_FIELDS);
	}

	@Then("^I should receive these organizations:$")
	public void Then_I_should_receive_these_organizations(DataTable expected)
			throws QueryInfoException {
		String actualReponseJson = actualResponse.getBody().asString();

		List<DefaultOrganization> actual = testUtils.objectify(actualReponseJson,
				ORGANIZATION_LIST_RESULT_TYPE);

		cucumberUtils.assertEquals(expected, actual, ORGANIZATION_FIELDS);
	}

	@Then("^I should receive these paginated people:$")
	public void Then_I_should_receive_these_paginated_people(DataTable expected)
			throws QueryInfoException {
		String actualReponseJson = actualResponse.getBody().asString();

		PaginatedResult<DefaultPerson> actualPaginatedResult = testUtils.objectify(actualReponseJson,
				PERSON_PAGINATED_RESULT_TYPE);

		List<DefaultPerson> actual = actualPaginatedResult.getPageResults();

		cucumberUtils.assertEquals(expected, actual, PERSON_FIELDS);
	}

	@Then("^I should receive these people:$")
	public void Then_I_should_receive_these_people(DataTable expected)
			throws QueryInfoException {
		String actualReponseJson = actualResponse.getBody().asString();

		List<DefaultPerson> actual = testUtils.objectify(actualReponseJson,
				PERSON_LIST_RESULT_TYPE);

		cucumberUtils.assertEquals(expected, actual, PERSON_FIELDS);
	}

	@Then("^I should receive these tuples:$")
	public void Then_I_should_receive_these_tuples(DataTable expected)
			throws QueryInfoException, JsonProcessingException, IOException {
		String actualReponseJson = actualResponse.getBody().asString();

		DataTable actual = cucumberUtils.readJsonArray(actualReponseJson);

		cucumberUtils.assertEquals(expected, actual);
	}

	protected List<PersonEntity> createPersonEntities(List<TestPerson> testPeople) {
		personToEmployerOrganizationIdMapper.setReferrerReferencePersistenceIdsForTestIds(testPeople);
		int personCount = testPeople.size();

		List<PersonEntity> result = new ArrayList<>(personCount);

		for (TestPerson person : testPeople) {
			PersonEntity personEntity = new PersonEntity();
			try {
				PropertyUtils.copyProperties(personEntity, person);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new QueryInfoRuntimeException(e);
			}
			result.add(personEntity);
		}

		return result;
	}
}
