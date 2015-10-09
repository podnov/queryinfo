package com.evanzeimet.queryinfo.jpa.bean.context;

import java.util.Map;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.bean.AbstractQueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.field.DefaultEntityAnnotationsQueryInfoFieldResolver;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfoResolver;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.evanzeimet.queryinfo.jpa.result.DefaultEntityQueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.result.QueryInfoResultConverter;
import com.evanzeimet.queryinfo.jpa.selection.DefaultEntityQueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;

public abstract class AbstractEntityQueryInfoBeanContext<RootEntity>
		extends AbstractQueryInfoBeanContext<RootEntity, RootEntity, RootEntity> {

	private Map<String, QueryInfoFieldInfo> fieldInfos;
	private QueryInfoResultConverter<RootEntity, RootEntity> resultConveter;
	private QueryInfoSelectionSetter<RootEntity> selectionSetter;

	public AbstractEntityQueryInfoBeanContext(QueryInfoBeanContextRegistry beanContextRegistry) {
		super(beanContextRegistry);
		resultConveter = new DefaultEntityQueryInfoResultConverter<>();
		selectionSetter = new DefaultEntityQueryInfoSelectionSetter<>(beanContextRegistry);
	}

	@Override
	public Class<RootEntity> getCriteriaQueryResultClass() {
		return getRootEntityClass();
	}

	@Override
	public Map<String, QueryInfoFieldInfo> getFieldInfos() throws QueryInfoException {
		if (fieldInfos == null) {
			Class<RootEntity> rootEntityClass = getRootEntityClass();
			QueryInfoPathFactory<RootEntity> pathFactory = getPathFactory();

			QueryInfoFieldInfoResolver<RootEntity> fieldResolver = new DefaultEntityAnnotationsQueryInfoFieldResolver<>(rootEntityClass);
			fieldInfos = fieldResolver.resolve(pathFactory);
		}

		return fieldInfos;
	}

	@Override
	public QueryInfoResultConverter<RootEntity, RootEntity> getResultConverter() {
		return resultConveter;
	}

	@Override
	public QueryInfoSelectionSetter<RootEntity> getSelectionSetter() {
		return selectionSetter;
	}
}
