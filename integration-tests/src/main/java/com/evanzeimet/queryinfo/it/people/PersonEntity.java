package com.evanzeimet.queryinfo.it.people;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.it.companies.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "people")
public class PersonEntity extends DefaultPerson {

	@JsonIgnore
	private List<Company> employerEntities;

	public List<Company> getEmployerEntities() {
		return employerEntities;
	}

	public void setEmployerEntities(List<Company> employerEntities) {
		this.employerEntities = employerEntities;
	}

	@Override
	@Column(name = "first_name")
	public String getFirstName() {
		return super.getFirstName();
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return super.getId();
	}

	@Override
	@Column(name = "last_name")
	public String getLastName() {
		return super.getLastName();
	}

	@PostLoad
	protected void postLoad() {
		postLoadEmployers();
	}

	protected void postLoadEmployers() {
		if (employerEntities != null) {
			int employerCount = employerEntities.size();

			List<Company> employers = new ArrayList<>(employerCount);
			employers.addAll(employerEntities);

			setEmployers(employers);
		}
	}
}
