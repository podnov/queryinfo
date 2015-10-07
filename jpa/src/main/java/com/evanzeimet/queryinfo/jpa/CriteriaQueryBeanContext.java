package com.evanzeimet.queryinfo.jpa;

import com.evanzeimet.queryinfo.jpa.from.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.predicate.PredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoOriginalResultTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public interface CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType> {

	QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory();

	QueryInfoOrderFactory<RootEntity> getOrderFactory();

	Class<InitialResultType> getInitialResultTypeClass();

	QueryInfoOriginalResultTransformer<InitialResultType, FinalResultType> getOriginalResultTransformer();

	PredicateFactory<RootEntity> getPredicateFactory();

	Class<RootEntity> getRootEntityClass();

	QueryInfoSelectionSetter<RootEntity> getSelectionSetter();

	Boolean getUseDistinctSelections();

}
