package com.evanzeimet.queryinfo.jpa.beancontext;

import java.util.Map;

import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoTupleTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public interface QueryInfoBeanContext<RootEntity, FinalTupleResultType> {

	Map<String /* fieldName */, QueryInfoFieldInfo> getFieldInfos();

	QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory();

	QueryInfoOrderFactory<RootEntity> getOrderFactory();


	QueryInfoPathFactory<RootEntity> getPathFactory();

	QueryInfoPredicateFactory<RootEntity> getPredicateFactory();

	Class<RootEntity> getRootEntityClass();

	QueryInfoSelectionSetter<RootEntity> getSelectionSetter();

	QueryInfoTupleTransformer<FinalTupleResultType> getTupleTransformer();

	Boolean getUseDistinctSelections();

}
