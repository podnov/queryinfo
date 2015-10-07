package com.evanzeimet.queryinfo.jpa.result;

import java.util.List;

public interface QueryInfoOriginalResultTransformer<InitialResultType, FinalResultType> {

	List<FinalResultType> transform(List<InitialResultType> initialResults);
}
