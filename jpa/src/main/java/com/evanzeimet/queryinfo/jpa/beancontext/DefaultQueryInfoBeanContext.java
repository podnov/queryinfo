package com.evanzeimet.queryinfo.jpa.beancontext;

import javax.inject.Inject;

import com.evanzeimet.queryinfo.jpa.field.EntityAnnotationsQueryInfoFieldResolver;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfoResolver;
import com.evanzeimet.queryinfo.jpa.jpacontext.DefaultQueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContextFactory;
import com.evanzeimet.queryinfo.jpa.order.QueryInfoOrderFactory;
import com.evanzeimet.queryinfo.jpa.path.DefaultQueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.predicate.QueryInfoPredicateFactory;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoTupleTransformer;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public class DefaultQueryInfoBeanContext<RootEntity, FinalTupleResultType>
		implements QueryInfoBeanContext<RootEntity, FinalTupleResultType> {

	private QueryInfoFieldInfoResolver<RootEntity> fieldInfoResolver;
	private QueryInfoJPAContextFactory<RootEntity> jpaContextFactory = new DefaultQueryInfoJPAContextFactory<>();
	// TODO this won't work with the beancontext built this way, beancontext producer that inspects
	// the context injection hosts for parameter type? tie in the queryinfofield resolver w/ this?
	@Inject
	private QueryInfoOrderFactory<RootEntity> orderFactory;
	@Inject
	private DefaultQueryInfoPathFactory<RootEntity> pathFactory;

	public DefaultQueryInfoBeanContext(Class<RootEntity> rootEntityClass) {
		fieldInfoResolver = new EntityAnnotationsQueryInfoFieldResolver<RootEntity>(rootEntityClass);
		// TODO fieldInfoResolver = new FieldEnumResolver(FieldEnum.class);
	}


	public QueryInfoFieldInfoResolver<RootEntity> getFieldInfoResolver() {
		return fieldInfoResolver;
	}

	@Override
	public QueryInfoJPAContextFactory<RootEntity> getJpaContextFactory() {
		return jpaContextFactory;
	}

	@Override
	public QueryInfoOrderFactory<RootEntity> getOrderFactory() {
		return orderFactory;
	}

	@Override
	public QueryInfoTupleTransformer<FinalTupleResultType> getTupleTransformer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryInfoPathFactory<RootEntity> getPathFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryInfoPredicateFactory<RootEntity> getPredicateFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<RootEntity> getRootEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryInfoSelectionSetter<RootEntity> getSelectionSetter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getUseDistinctSelections() {
		// TODO Auto-generated method stub
		return null;
	}
}
