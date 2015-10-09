package com.evanzeimet.queryinfo.it.companies;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.bean.context.AbstractEntityQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.bean.context.QueryInfoBeanContextRegistry;

@Stateless
public class CompanyQueryInfoBeanContext extends AbstractEntityQueryInfoBeanContext<CompanyEntity> {

	@Inject
	public CompanyQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		super(beanContextRegistry);
	}

	@Override
	public Class<CompanyEntity> getRootEntityClass() {
		return CompanyEntity.class;
	}

}
