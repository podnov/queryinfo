package com.evanzeimet.queryinfo.jpa.bean.context;

import java.util.Map;

import javax.persistence.Tuple;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.AbstractQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.selection.DefaultTupleQueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public abstract class AbstractTupleQueryInfoBeanContext<RootEntity, QueryInfoResult>
		extends AbstractQueryInfoBeanContext<RootEntity, Tuple, QueryInfoResult> {

	private DefaultTupleQueryInfoSelectionSetter<RootEntity> selectionSetter;

	public AbstractTupleQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		super(beanContextRegistry);
		selectionSetter = new DefaultTupleQueryInfoSelectionSetter<>(beanContextRegistry);
	}

	@Override
	public Class<Tuple> getCriteriaQueryResultClass() {
		return Tuple.class;
	}

	@Override
	public Map<String, QueryInfoFieldInfo> getFieldInfos() throws QueryInfoException {
		// TODO Walk all joins? Enum-based definitions?
		return null;
	}

	@Override
	public QueryInfoSelectionSetter<RootEntity> getSelectionSetter() {
		return selectionSetter;
	}

}
