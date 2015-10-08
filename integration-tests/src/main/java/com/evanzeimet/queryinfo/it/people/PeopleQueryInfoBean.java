package com.evanzeimet.queryinfo.it.people;

import com.evanzeimet.queryinfo.jpa.AbstractQueryInfoBean;
import com.evanzeimet.queryinfo.jpa.beancontext.CriteriaQueryBeanContext;


public class PeopleQueryInfoBean
		extends AbstractQueryInfoBean<PersonEntity, PersonEntity, PersonEntity> {

	public PeopleQueryInfoBean(CriteriaQueryBeanContext<PersonEntity, PersonEntity, PersonEntity> context) {
		super(context);
	}

}
