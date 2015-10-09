package com.evanzeimet.queryinfo.it.companies;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.it.people.Person;
import com.evanzeimet.queryinfo.it.people.PersonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "companies")
public class CompanyEntity extends DefaultCompany {

	@JsonIgnore
	private List<PersonEntity> employeeEntities;

	@Override
	@Column(name = "address1")
	public String getAddress1() {
		return super.getAddress1();
	}

	@Override
	@Column(name = "address2")
	public String getAddress2() {
		return super.getAddress2();
	}

	@Override
	@Column(name = "city")
	public String getCity() {
		return super.getCity();
	}

	@OneToMany(mappedBy = "company")
	public List<PersonEntity> getEmployeesEntities() {
		return employeeEntities;
	}

	public void setEmployeesEntities(List<PersonEntity> employeeEntities) {
		this.employeeEntities = employeeEntities;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return super.getId();
	}

	@Override
	@Column(name = "name")
	public String getName() {
		return super.getName();
	}

	@Override
	@Column(name = "state")
	public String getState() {
		return super.getState();
	}

	@Override
	@Column(name = "zip")
	public String getZip() {
		return super.getZip();
	}

	@PostLoad
	protected void postLoad() {
		postLoadEmployeeEntities();
	}

	protected void postLoadEmployeeEntities() {
		if (employeeEntities != null) {
			int employeeCount = employeeEntities.size();

			List<Person> employees = new ArrayList<>(employeeCount);
			employees.addAll(employeeEntities);

			setEmployees(employees);
		}
	}
}
