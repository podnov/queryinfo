package com.evanzeimet.queryinfo.jpa.result;

import java.util.List;

import javax.persistence.Tuple;

public interface QueryInfoTupleTransformer<FinalTupleResultType> {

	List<FinalTupleResultType> transform(List<Tuple> initialResults);
}
