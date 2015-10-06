package com.evanzeimet.queryinfo.jpa.field;

import java.util.List;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

public interface QueryInfoFieldInfoResolver<T> {

	List<QueryInfoFieldInfo> resolve(QueryInfoPathFactory<T> pathFactory) throws QueryInfoException;

}
