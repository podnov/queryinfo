package com.evanzeimet.queryinfo.it.companies;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.bean.AbstractEntityQueryInfoBean;

@Stateless
public class CompanyQueryInfoBean extends AbstractEntityQueryInfoBean<CompanyEntity> {

	@Inject
	public CompanyQueryInfoBean(CompanyQueryInfoBeanContext beanContext) {
		super(beanContext);
	}

}
