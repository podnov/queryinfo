package com.evanzeimet.queryinfo.jpa.bean;

import javax.persistence.Tuple;

import com.evanzeimet.queryinfo.jpa.bean.context.AbstractTupleQueryInfoBeanContext;

public abstract class AbstractTupleQueryInfoBean<RootEntity, QueryInfoResult>
		extends AbstractQueryInfoBean<RootEntity, Tuple, QueryInfoResult> {

	public AbstractTupleQueryInfoBean(AbstractTupleQueryInfoBeanContext<RootEntity, QueryInfoResult> context) {
		super(context);
	}

}
