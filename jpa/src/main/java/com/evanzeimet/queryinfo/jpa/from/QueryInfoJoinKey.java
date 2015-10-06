package com.evanzeimet.queryinfo.jpa.from;

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
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		QueryInfoJoinKey rhs = (QueryInfoJoinKey) obj;
		return new EqualsBuilder()
				.append(parentAttributeName, rhs.parentAttributeName)
				.append(joinParent, rhs.joinParent)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 39)
				.append(parentAttributeName)
				.append(joinParent)
				.toHashCode();
	}
}
