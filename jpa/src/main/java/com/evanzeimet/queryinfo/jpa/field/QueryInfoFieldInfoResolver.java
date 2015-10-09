package com.evanzeimet.queryinfo.jpa.field;

import java.util.Map;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public interface QueryInfoFieldInfoResolver<T> {

	Map<String /* fieldName */, QueryInfoFieldInfo> resolve(QueryInfoPathFactory<T> pathFactory)
			throws QueryInfoException;

}
