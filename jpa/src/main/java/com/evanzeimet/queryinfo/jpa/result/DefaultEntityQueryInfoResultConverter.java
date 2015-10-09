package com.evanzeimet.queryinfo.jpa.result;

import java.util.List;

public class DefaultEntityQueryInfoResultConverter<RootEntity>
		implements QueryInfoResultConverter<RootEntity, RootEntity> {

	@Override
	public List<RootEntity> convert(List<RootEntity> criteriaQueryResults) {
		return criteriaQueryResults;
	}

}
