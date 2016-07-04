package com.evanzeimet.queryinfo.it.organizationemployee;

/*
 * #%L
 * queryinfo-integration-tests-war
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 - 2016 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import com.evanzeimet.queryinfo.QueryInfo;
import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.QueryInfoUtils;
import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity;
import com.evanzeimet.queryinfo.it.organizations.OrganizationEntity_;
import com.evanzeimet.queryinfo.it.people.PersonEntity_;
import com.evanzeimet.queryinfo.jpa.entity.QueryInfoEntityContextRegistry;
import com.evanzeimet.queryinfo.jpa.field.QueryInfoJPAAttributePathBuilder;
import com.evanzeimet.queryinfo.jpa.jpacontext.QueryInfoJPAContext;
import com.evanzeimet.queryinfo.jpa.selection.DefaultTupleQueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.jpa.selection.QueryInfoSelectionSetter;
import com.evanzeimet.queryinfo.selection.DefaultSelection;
import com.evanzeimet.queryinfo.selection.Selection;

public class OrganizationEmployeeQueryInfoBeanSelectionSetter
		extends DefaultTupleQueryInfoSelectionSetter<OrganizationEntity>
		implements QueryInfoSelectionSetter<OrganizationEntity> {

	protected QueryInfoUtils utils = new QueryInfoUtils();

	@Override
	public void setSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<OrganizationEntity> jpaContext,
			QueryInfo queryInfo) throws QueryInfoException {
		boolean hasRequestedAllFields = utils.hasRequestedAllFields(queryInfo);

		if (hasRequestedAllFields) {
			setRequestedAllFieldsSelection(entityContextRegistry, jpaContext);
		} else {
			super.setSelection(entityContextRegistry, jpaContext, queryInfo);
		}
	}

	protected void setRequestedAllFieldsSelection(QueryInfoEntityContextRegistry entityContextRegistry,
			QueryInfoJPAContext<OrganizationEntity> jpaContext) throws QueryInfoException {
		List<Selection> selections = new ArrayList<>();

		QueryInfoJPAAttributePathBuilder<OrganizationEntity, OrganizationEntity> attributePathBuilder = QueryInfoJPAAttributePathBuilder.create(entityContextRegistry)
				.root(OrganizationEntity.class);

		for (OrganizationEmployeeField field : OrganizationEmployeeField.values()) {
			String attributePath = null;

			switch (field) {
				case EMPLOYEE_FIRST_NAME:
					attributePath = attributePathBuilder.clear()
							.add(OrganizationEntity_.employeeEntities)
							.add(PersonEntity_.firstName)
							.buildString();
					break;

				case EMPLOYEE_LAST_NAME:
					attributePath = attributePathBuilder.clear()
							.add(OrganizationEntity_.employeeEntities)
							.add(PersonEntity_.lastName)
							.buildString();
					break;

				case NAME:
					attributePath = attributePathBuilder.clear()
							.add(OrganizationEntity_.name)
							.buildString();
					break;
			}

			if (attributePath == null) {
				String message = String.format("No field name set for field [%s]", field);
				throw new QueryInfoException(message);
			}

			Selection selection = new DefaultSelection();
			selection.setAttributePath(attributePath);

			selections.add(selection);
		}

		setExplicitSelections(entityContextRegistry, jpaContext, selections);
	}

}
