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

public enum SortDirection {
	ASC,
	DESC;

	public String getText() {
		return name().toLowerCase();
	}

	public static SortDirection fromSort(Sort sort) {
		String direction = sort.getDirection();
		return fromText(direction);
	}

	public static SortDirection fromText(String text) {
		SortDirection result = null;

		for (SortDirection sortDirection : SortDirection.values()) {
			if (sortDirection.getText().equals(text)) {
				result = sortDirection;
				break;
			}
		}

		return result;
	}
}
