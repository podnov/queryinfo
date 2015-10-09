package com.evanzeimet.queryinfo.jpa.selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoResultType;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;

public interface QueryInfoSelectionSetter<RootEntity> {

	void setSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfoResultType resultType,
			QueryInfo queryInfo) throws QueryInfoException;

}
