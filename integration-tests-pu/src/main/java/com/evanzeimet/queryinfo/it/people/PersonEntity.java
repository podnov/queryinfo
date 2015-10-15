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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoField;
import com.evanzeimet.queryinfo.jpa.join.QueryInfoJoin;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@Entity
@Table(name = "people")
public class PersonEntity extends DefaultPerson {

	private OrganizationEntity employerEntity;

	@JsonIgnore
	@QueryInfoJoin(name = "employer")
	@ManyToOne
	@JoinColumn(name = "employer_organization_id",
			referencedColumnName = "id",
			insertable = false,
			updatable = false)
	public OrganizationEntity getEmployerEntity() {
		return employerEntity;
	}

	public void setEmployerEntity(OrganizationEntity employerEntity) {
		this.employerEntity = employerEntity;
	}

	@Override
	@Column(name = "employer_organization_id")
	public Long getEmployerOrganizationId() {
		return super.getEmployerOrganizationId();
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
		postLoadEmployer();
	}

	protected void postLoadEmployer() {
		setEmployer(getEmployerEntity());
	}
}
