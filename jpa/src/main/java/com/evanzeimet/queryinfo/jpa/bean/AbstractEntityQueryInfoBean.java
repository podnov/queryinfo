package com.evanzeimet.queryinfo.jpa.bean;

import com.evanzeimet.queryinfo.jpa.bean.context.AbstractEntityQueryInfoBeanContext;

public abstract class AbstractEntityQueryInfoBean<RootEntity>
		extends AbstractQueryInfoBean<RootEntity, RootEntity, RootEntity> {

	public AbstractEntityQueryInfoBean(AbstractEntityQueryInfoBeanContext<RootEntity> context) {
		super(context);
	}

}
