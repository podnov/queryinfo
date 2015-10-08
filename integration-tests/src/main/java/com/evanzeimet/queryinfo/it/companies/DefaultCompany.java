package com.evanzeimet.queryinfo.it.companies;

import java.util.List;

import com.evanzeimet.queryinfo.it.people.DefaultPerson;
import com.evanzeimet.queryinfo.it.people.Person;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultCompany implements Company {

	private String address1;
	private String address2;
	private String city;
	@JsonDeserialize(contentAs = DefaultPerson.class)
	private List<Person> employees;
	private Long id;
	private String name;
	private String state;
	private String zip;

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
