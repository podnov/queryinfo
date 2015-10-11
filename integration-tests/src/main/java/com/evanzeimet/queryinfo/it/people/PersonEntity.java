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


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.it.companies.Company;
import com.evanzeimet.queryinfo.it.companies.CompanyEntity;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "people")
public class PersonEntity extends DefaultPerson {

	@JsonIgnore
	private List<CompanyEntity> employerEntities;


	@ManyToMany
	@JoinTable(name = "people_to_companies",
			joinColumns = { @JoinColumn(name = "person_id",
					referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "company_id",
					referencedColumnName = "id") })
	public List<CompanyEntity> getEmployerEntities() {
		return employerEntities;
	}

	public void setEmployerEntities(List<CompanyEntity> employerEntities) {
		this.employerEntities = employerEntities;
	}

	@Override
	@QueryInfoField
	@Column(name = "first_name")
	public String getFirstName() {
		return super.getFirstName();
	}

	@Override
	@QueryInfoField
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return super.getId();
	}

	@Override
	@QueryInfoField
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
