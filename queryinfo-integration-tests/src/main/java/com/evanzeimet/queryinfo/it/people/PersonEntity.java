package com.evanzeimet.queryinfo.it.people;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.evanzeimet.queryinfo.it.companies.CompanyEntity;

@Entity
@Table(name = "people")
public class PersonEntity extends DefaultPerson {

	private CompanyEntity company;

	@ManyToOne
	@JoinColumn(name = "company_id",
			referencedColumnName = "id",
			insertable = false,
			updatable = false)
	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	@Override
	@Column(name = "company_id")
	public Long getCompanyId() {
		return super.getCompanyId();
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

}
