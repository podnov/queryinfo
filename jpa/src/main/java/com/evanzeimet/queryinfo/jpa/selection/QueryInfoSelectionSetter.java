package com.evanzeimet.queryinfo.jpa.selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public interface QueryInfoSelectionSetter<RootEntity> {

	void setSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException;

}
