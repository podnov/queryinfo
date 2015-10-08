package com.evanzeimet.queryinfo.jpa.result;

import java.util.List;

public interface QueryInfoOriginalResultTransformer<InitialTupleResultType, FinalTupleResultType> {

	List<FinalTupleResultType> transform(List<InitialTupleResultType> initialResults);
}
