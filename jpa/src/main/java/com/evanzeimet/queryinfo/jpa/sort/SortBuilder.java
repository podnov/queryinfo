package com.evanzeimet.queryinfo.jpa.sort;

/*
 * #%L
 * queryinfo-jpa
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

import javax.persistence.metamodel.SingularAttribute;

import com.evanzeimet.queryinfo.sort.Sort;
import com.evanzeimet.queryinfo.sort.SortDirection;

public class SortBuilder extends com.evanzeimet.queryinfo.sort.SortBuilder {

	public SortBuilder() {

	}

	@Override
	public SortBuilder builderReferenceInstance(Sort builderReferenceInstance) {
		super.builderReferenceInstance(builderReferenceInstance);
		return this;
	}

	public static SortBuilder create() {
		return new SortBuilder();
	}

	@Override
	public SortBuilder direction(SortDirection direction) {
		super.direction(direction);
		return this;
	}

	@Override
	public SortBuilder direction(String direction) {
		super.direction(direction);
		return this;
	}

	public SortBuilder fieldName(SingularAttribute<?, ?> attribute) {
		String name = attribute.getName();
		return fieldName(name);
	}

	@Override
	public SortBuilder fieldName(String fieldName) {
		super.fieldName(fieldName);
		return this;
	}
}
