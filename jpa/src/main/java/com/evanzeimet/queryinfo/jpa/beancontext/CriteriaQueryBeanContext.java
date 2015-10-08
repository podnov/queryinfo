package com.evanzeimet.queryinfo.jpa.beancontext;

import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoOriginalResultTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public interface CriteriaQueryBeanContext<RootEntity, InitialTupleResultType, FinalTupleResultType> {

	QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory();

	QueryInfoOrderFactory<RootEntity> getOrderFactory();

	Class<InitialTupleResultType> getInitialTupleResultTypeClass();

	QueryInfoOriginalResultTransformer<InitialTupleResultType, FinalTupleResultType> getTupleResultTransformer();

	QueryInfoPathFactory<RootEntity> getPathFactory();

	QueryInfoPredicateFactory<RootEntity> getPredicateFactory();

	Class<RootEntity> getRootEntityClass();

	QueryInfoSelectionSetter<RootEntity> getSelectionSetter();

	Boolean getUseDistinctSelections();

}
