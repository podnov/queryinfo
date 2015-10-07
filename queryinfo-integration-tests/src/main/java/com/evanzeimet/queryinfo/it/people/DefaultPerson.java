package com.evanzeimet.queryinfo.it.people;

public class DefaultPerson implements Person {

	private Long companyId;
	private String firstName;
	private Long id;
	private String lastName;

	public DefaultPerson() {

	}

	@Override
	public Long getCompanyId() {
		return companyId;
	}

	@Override
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
