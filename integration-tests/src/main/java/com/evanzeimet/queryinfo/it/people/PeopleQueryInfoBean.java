package com.evanzeimet.queryinfo.it.people;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.bean.AbstractEntityQueryInfoBean;

@Stateless
public class PeopleQueryInfoBean extends AbstractEntityQueryInfoBean<PersonEntity> {

	@Inject
	public PeopleQueryInfoBean(PeopleQueryInfoBeanContext context) {
		super(context);
	}

}
