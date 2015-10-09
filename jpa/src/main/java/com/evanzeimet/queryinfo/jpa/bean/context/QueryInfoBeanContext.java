package com.evanzeimet.queryinfo.jpa.bean.context;

import java.util.Map;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public interface QueryInfoBeanContext<RootEntity, CriteriaQueryResult, QueryInfoResult> {

	Class<CriteriaQueryResult> getCriteriaQueryResultClass();

	Map<String /* fieldName */, QueryInfoFieldInfo> getFieldInfos() throws QueryInfoException;

	QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory();

	QueryInfoOrderFactory<RootEntity> getOrderFactory();

	QueryInfoPathFactory<RootEntity> getPathFactory();

	QueryInfoPredicateFactory<RootEntity> getPredicateFactory();

	QueryInfoResultConverter<CriteriaQueryResult, QueryInfoResult> getResultConverter();

	Class<RootEntity> getRootEntityClass();

	QueryInfoSelectionSetter<RootEntity> getSelectionSetter();

	Boolean getUseDistinctSelections();

}
