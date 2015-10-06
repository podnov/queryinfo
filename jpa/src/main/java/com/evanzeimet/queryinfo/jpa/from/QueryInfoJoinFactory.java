package com.evanzeimet.queryinfo.jpa.from;

import java.util.List;

import javax.persistence.criteria.Root;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;

public interface QueryInfoJoinFactory<RootEntity> {

	QueryInfoFromContext createJoins(Root<RootEntity> root,
			List<QueryInfoFieldInfo> fields,
			QueryInfo queryInfo);

}
