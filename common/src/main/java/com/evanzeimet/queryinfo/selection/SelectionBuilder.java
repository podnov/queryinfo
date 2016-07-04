package com.evanzeimet.queryinfo.selection;

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


public class SelectionBuilder {

	private Selection builderReferenceInstance = createDefaultInstance();

	public SelectionBuilder() {

	}

	public SelectionBuilder aggregateFunction(AggregateFunction aggregateFunction) {
		builderReferenceInstance.setAggregateFunction(aggregateFunction);
		return this;
	}

	public SelectionBuilder attributePath(String attributePath) {
		builderReferenceInstance.setAttributePath(attributePath);
		return this;
	}

	public Selection build() {
		DefaultSelection result = createDefaultInstance();

		result.setAggregateFunction(builderReferenceInstance.getAggregateFunction());
		result.setAttributePath(builderReferenceInstance.getAttributePath());

		return result;
	}

	public SelectionBuilder builderReferenceInstance(Selection builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public static SelectionBuilder create() {
		return new SelectionBuilder();
	}

	protected DefaultSelection createDefaultInstance() {
		return new DefaultSelection();
	}

}
