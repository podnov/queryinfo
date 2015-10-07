package com.evanzeimet.queryinfo.jpa.beancontext;

import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoOriginalResultTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public interface CriteriaQueryBeanContext<RootEntity, InitialResultType, FinalResultType> {

	QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory();

	QueryInfoOrderFactory<RootEntity> getOrderFactory();

	Class<InitialResultType> getInitialResultTypeClass();

	QueryInfoOriginalResultTransformer<InitialResultType, FinalResultType> getOriginalResultTransformer();

	QueryInfoPathFactory<RootEntity> getPathFactory();

	QueryInfoPredicateFactory<RootEntity> getPredicateFactory();

	Class<RootEntity> getRootEntityClass();

	QueryInfoSelectionSetter<RootEntity> getSelectionSetter();

	Boolean getUseDistinctSelections();

}
