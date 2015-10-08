package com.evanzeimet.queryinfo.it.people;

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
