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


import java.util.List;

import com.evanzeimet.queryinfo.it.people.DefaultPerson;
import com.evanzeimet.queryinfo.it.people.Person;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultCompany implements Company {

	private String address1;
	private String address2;
	private String city;
	private Long id;
	private String name;
	private String state;
	private String zip;

	@JsonDeserialize(contentAs = DefaultPerson.class)
	private List<Person> employees;

	public DefaultCompany() {

	}

	@Override
	public String getAddress1() {
		return address1;
	}

	@Override
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Override
	public String getAddress2() {
		return address2;
	}

	@Override
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public List<Person> getEmployees() {
		return employees;
	}

	@Override
	public void setEmployees(List<Person> employees) {
		this.employees = employees;
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
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String getZip() {
		return zip;
	}

	@Override
	public void setZip(String zip) {
		this.zip = zip;
	}

}
