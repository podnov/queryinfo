package com.evanzeimet.queryinfo.jpa.jpacontext;

/*
 * #%L
 * queryinfo-jpa
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

import javax.persistence.criteria.From;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class QueryInfoJoinKey {

	private From<?, ?> joinParent;
	private String parentAttributeName;

	public QueryInfoJoinKey() {

	}

	public From<?, ?> getJoinParent() {
		return joinParent;
	}

	public void setJoinParent(From<?, ?> from) {
		this.joinParent = from;
	}

	public String getParentAttributeName() {
		return parentAttributeName;
	}

	public void setParentAttributeName(String parentAttributeName) {
		this.parentAttributeName = parentAttributeName;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result;

		if (obj == null) {
			result = false;
		} else if (obj == this) {
			result = true;
		} else if (obj.getClass() != getClass()) {
			result = false;
		} else {
			QueryInfoJoinKey rhs = (QueryInfoJoinKey) obj;
			result = new EqualsBuilder()
				.append(parentAttributeName, rhs.parentAttributeName)
				.append(joinParent, rhs.joinParent)
				.isEquals();
		}

		return result;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 39)
				.append(parentAttributeName)
				.append(joinParent)
				.toHashCode();
	}
}
