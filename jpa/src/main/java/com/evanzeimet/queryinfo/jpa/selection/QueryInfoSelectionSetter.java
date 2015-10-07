package com.evanzeimet.queryinfo.jpa.selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.jpa.from.QueryInfoJPAContext;

public interface QueryInfoSelectionSetter<RootEntity> {

	void setSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo);

}
