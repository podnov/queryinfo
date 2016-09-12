package com.evanzeimet.queryinfo.it.feature.iterator;

/*
 * #%L
 * queryinfo-integration-tests
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


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;

import com.evanzeimet.queryinfo.DefaultQueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoRuntimeException;
import com.evanzeimet.queryinfo.it.QueryInfoIntegrationTestUtils;
import com.evanzeimet.queryinfo.it.TestQueryInfoBeanFactory;
import com.evanzeimet.queryinfo.it.cucumber.CucumberUtils;
import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity;
import com.evanzeimet.queryinfo.it.people.PersonEntity;
import com.evanzeimet.queryinfo.it.people.PersonToEmployerOrganizationIdMapper;
import com.evanzeimet.queryinfo.it.people.TestPerson;
import com.evanzeimet.queryinfo.jpa.bean.QueryInfoBean;
import com.google.common.collect.Lists;

import cucumber.api.DataTable;
import cucumber.api.Format;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class IteratorSteps {

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

	private static boolean needToPersistOrganizations = true;
	private static boolean needToPersistPeople = true;

	private List<Object> actualQueryResults;
	private final CucumberUtils cucumberUtils;
	private final PersonToEmployerOrganizationIdMapper personToEmployerOrganizationIdMapper = new PersonToEmployerOrganizationIdMapper();
	private QueryInfoBean<?, ?, ? extends Object> queryInfoBean;
	private final QueryInfoIntegrationTestUtils testUtils;

	public IteratorSteps() {
		cucumberUtils = new CucumberUtils();
		testUtils = QueryInfoIntegrationTestUtils.create();
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

	@Given("^an organization entity query info bean$")
	public void Given_an_organization_entity_query_info_bean() {
		EntityManager entityManager = testUtils.getEntityManager();
		queryInfoBean = TestQueryInfoBeanFactory.createOrganizationEntityQueryInfoBean(entityManager);
	}

	@When("^I execute this query for an iterator:$")
	public void When_I_execute_this_query_for_an_iterator(String rawQueryInfo) throws QueryInfoException {
		DefaultQueryInfo queryInfo = testUtils.objectify(rawQueryInfo,
				DefaultQueryInfo.class);

		Iterator<? extends Object> iterator = queryInfoBean.queryForIterator(queryInfo);

		actualQueryResults = Lists.newArrayList(iterator);
	}

	@Then("^I should receive these organizations:$")
	public void Then_I_should_receive_these_organizations(DataTable expected)
			throws QueryInfoException {

		cucumberUtils.assertEquals(expected,
				actualQueryResults,
				ORGANIZATION_FIELDS);
	}

	protected List<PersonEntity> createPersonEntities(List<TestPerson> testPeople) {
		personToEmployerOrganizationIdMapper.setReferrerReferencePersistenceIdsForTestIds(testPeople);
		int personCount = testPeople.size();

		List<PersonEntity> result = new ArrayList<>(personCount);

		for (TestPerson person : testPeople) {
			PersonEntity personEntity = new PersonEntity();
			try {
				PropertyUtils.copyProperties(personEntity,
						person);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new QueryInfoRuntimeException(e);
			}
			result.add(personEntity);
		}

		return result;
	}
}
