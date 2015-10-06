package com.evanzeimet.queryinfo.pagination;

import org.apache.commons.lang3.SerializationUtils;

public class PaginationInfoBuilder {

	private PaginationInfo builderReferenceInstance = new DefaultPaginationInfo();

	public PaginationInfoBuilder() {

	}

	public PaginationInfo build() {
		return SerializationUtils.clone(builderReferenceInstance);
	}

	public PaginationInfoBuilder builderReferenceInstance(PaginationInfo builderReferenceInstance) {
		this.builderReferenceInstance = builderReferenceInstance;
		return this;
	}

	public static PaginationInfoBuilder create() {
		return new PaginationInfoBuilder();
	}

	public static PaginationInfoBuilder createForAll() {
		return create()
				.pageIndex(0L)
				.pageSize(Long.MAX_VALUE);
	}

	public static PaginationInfoBuilder createForOne() {
		return create()
				.pageIndex(0L)
				.pageIndex(1L);
	}

	public PaginationInfoBuilder pageIndex(Long pageIndex) {
		builderReferenceInstance.setPageIndex(pageIndex);
		return this;
	}

	public PaginationInfoBuilder pageSize(Long pageSize) {
		builderReferenceInstance.setPageSize(pageSize);
		return this;
	}
}
