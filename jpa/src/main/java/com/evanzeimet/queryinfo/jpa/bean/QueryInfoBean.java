package com.evanzeimet.queryinfo.jpa.bean;

import java.util.Iterator;

/*
 * #%L
 * queryinfo-jpa
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoJPAAttributePathBuilder;
import com.evanzeimet.queryinfo.pagination.PaginatedResult;

public interface QueryInfoBean<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	Long count(QueryInfo queryInfo) throws QueryInfoException;

	QueryInfoJPAAttributePathBuilder<RootEntity, RootEntity> createJpaAttributePathBuilder();

	List<QueryInfoResult> query(QueryInfo queryInfo) throws QueryInfoException;

	Iterator<QueryInfoResult> queryForIterator(QueryInfo queryInfo) throws QueryInfoException;

	QueryInfoResult queryForOne(QueryInfo queryInfo) throws QueryInfoException;

	PaginatedResult<QueryInfoResult> queryForPaginatedResult(QueryInfo queryInfo)
			throws QueryInfoException;

}
