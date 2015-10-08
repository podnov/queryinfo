package com.evanzeimet.queryinfo.it.people;

import java.util.List;

import com.evanzeimet.queryinfo.it.companies.Company;

public interface Person {

	List<Company> getEmployers();

	void setEmployers(List<Company> employers);

	String getFirstName();

	void setFirstName(String firstName);

	Long getId();

	void setId(Long id);

	String getLastName();

	void setLastName(String lastName);

}
