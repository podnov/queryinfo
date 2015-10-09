package com.evanzeimet.queryinfo.jpa.result;

import java.util.List;

public interface QueryInfoResultConverter<CriteriaQueryResultType, QueryInfoResultType> {

	List<QueryInfoResultType> convert(List<CriteriaQueryResultType> criteriaQueryResults);

}
