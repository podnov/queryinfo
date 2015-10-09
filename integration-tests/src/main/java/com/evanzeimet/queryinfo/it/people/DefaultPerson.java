package com.evanzeimet.queryinfo.it.people;

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

import java.util.List;

import com.evanzeimet.queryinfo.it.companies.Company;
import com.evanzeimet.queryinfo.it.companies.DefaultCompany;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultPerson implements Person {

	@JsonDeserialize(contentAs = DefaultCompany.class)
	private List<Company> employers;
	private String firstName;
	private Long id;
	private String lastName;

	public DefaultPerson() {

	}

	@Override
	public List<Company> getEmployers() {
		return employers;
	}

	@Override
	public void setEmployers(List<Company> employers) {
		this.employers = employers;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
