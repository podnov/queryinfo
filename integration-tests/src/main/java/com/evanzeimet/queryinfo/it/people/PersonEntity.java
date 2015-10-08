package com.evanzeimet.queryinfo.it.people;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "people")
public class PersonEntity extends DefaultPerson {

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
