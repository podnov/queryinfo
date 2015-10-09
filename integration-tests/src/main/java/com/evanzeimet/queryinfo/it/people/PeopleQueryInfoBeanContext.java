package com.evanzeimet.queryinfo.it.people;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.bean.context.AbstractEntityQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;

@Stateless
public class PeopleQueryInfoBeanContext extends AbstractEntityQueryInfoBeanContext<PersonEntity> {

	@Inject
	public PeopleQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		super(beanContextRegistry);
	}

	@Override
	public Class<PersonEntity> getRootEntityClass() {
		return PersonEntity.class;
	}
}
