package com.evanzeimet.queryinfo.sort;

/*
 * #%L
 * queryinfo-common
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
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


public class SortBuilder {

	private Sort builderReferenceInstance = createDefaultInstance();

	public SortBuilder() {

	}

	public SortBuilder attributePath(String attributePath) {
		builderReferenceInstance.setAttributePath(attributePath);
		return this;
	}

	public Sort build() {
		DefaultSort result = createDefaultInstance();

		result.setAttributePath(builderReferenceInstance.getAttributePath());
		result.setDirection(builderReferenceInstance.getDirection());

		return result;
	}

	public SortBuilder builderReferenceInstance(Sort builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public static SortBuilder create() {
		return new SortBuilder();
	}

	protected DefaultSort createDefaultInstance() {
		return new DefaultSort();
	}

	public SortBuilder direction(SortDirection direction) {
		String rawDirection;

		if (direction == null) {
			rawDirection = null;
		} else {
			rawDirection = direction.getText();
		}

		return direction(rawDirection);
	}

	public SortBuilder direction(String direction) {
		builderReferenceInstance.setDirection(direction);
		return this;
	}
}
