package com.evanzeimet.queryinfo.jpa.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoResultType;
import com.evanzeimet.queryinfo.jpa.beancontext.QueryInfoBeanContext;
import com.evanzeimet.queryinfo.jpa.beancontext.QueryInfoBeanContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldInfo;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldPurpose;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoFieldUtils;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;

@LocalBean
public class DefaultQueryInfoSelectionSetter<RootEntity>
		implements QueryInfoSelectionSetter<RootEntity> {

	@Inject
	protected QueryInfoBeanContextRegistry beanContextRegistry;

	protected QueryInfoFieldUtils fieldUtils = new QueryInfoFieldUtils();

	public DefaultQueryInfoSelectionSetter() {

	}

	protected void setFlatSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		boolean hasRequestedAllFields = fieldUtils.hasRequestedAllFields(queryInfo);

		if (hasRequestedAllFields) {
			setRequestAllFlatSelection(jpaContext);
		} else {
			setRequestedSelection(jpaContext, queryInfo);
		}
	}

	protected void setExplicitHierarchicalSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		// TODO validate explicit hierarchical collections first? if you requested joined field,
		// what's it do?
		setRequestedSelection(jpaContext, queryInfo);
	}

	protected void setFieldNameSelections(QueryInfoJPAContext<RootEntity> jpaContext,
			List<String> requestedFields) throws QueryInfoException {
		QueryInfoBeanContext<RootEntity, ?> queryInfoBeanContext = beanContextRegistry.getContextForRoot(jpaContext);
		QueryInfoPathFactory<RootEntity> pathFactory = queryInfoBeanContext.getPathFactory();

		int selectionCount = requestedFields.size();

		Root<RootEntity> root = jpaContext.getRoot();
		List<Selection<?>> selections = new ArrayList<>(selectionCount);

		for (String requestedField : requestedFields) {
			Expression<?> path = pathFactory.getPathForField(jpaContext,
					root,
					requestedField,
					QueryInfoFieldPurpose.SELECT);
			selections.add(path);
		}

		CriteriaQuery<Object> criteriaQuery = jpaContext.getCriteriaQuery();
		criteriaQuery.multiselect(selections);
	}

	protected void setHierarchicalSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		boolean hasRequestedAllFields = fieldUtils.hasRequestedAllFields(queryInfo);

		if (hasRequestedAllFields) {
			setRequestAllEntitySelection(jpaContext);
		} else {
			setExplicitHierarchicalSelection(jpaContext, queryInfo);
		}
	}

	protected void setRequestAllEntitySelection(QueryInfoJPAContext<RootEntity> jpaContext) {
		CriteriaQuery<RootEntity> criteriaQuery = jpaContext.getCriteriaQuery();
		Root<RootEntity> root = jpaContext.getRoot();
		criteriaQuery.select(root);
	}

	protected void setRequestAllFlatSelection(QueryInfoJPAContext<RootEntity> jpaContext)
			throws QueryInfoException {
		QueryInfoBeanContext<RootEntity, ?> queryInfoBeanContext = beanContextRegistry.getContextForRoot(jpaContext);

		Map<String, QueryInfoFieldInfo> fieldInfos = queryInfoBeanContext.getFieldInfos();
		Iterator<QueryInfoFieldInfo> iterator = fieldInfos.values().iterator();

		int selectionCount = fieldInfos.size();
		List<String> fieldNames = new ArrayList<>(selectionCount);

		while (iterator.hasNext()) {
			QueryInfoFieldInfo fieldInfo = iterator.next();
			String fieldName = fieldInfo.getFieldName();
			fieldNames.add(fieldName);
		}

		setFieldNameSelections(jpaContext, fieldNames);
	}

	protected void setRequestedSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		List<String> fieldNames = fieldUtils.coalesceRequestedFields(queryInfo);
		setFieldNameSelections(jpaContext, fieldNames);
	}

	@Override
	public void setSelection(QueryInfoJPAContext<RootEntity> jpaContext,
			QueryInfoResultType resultType,
			QueryInfo queryInfo) throws QueryInfoException {
		switch (resultType) {
			case FLAT:
				setFlatSelection(jpaContext, queryInfo);
				break;

			case HIERARCHICAL:
				setHierarchicalSelection(jpaContext, queryInfo);
				break;
		}
	}

}
