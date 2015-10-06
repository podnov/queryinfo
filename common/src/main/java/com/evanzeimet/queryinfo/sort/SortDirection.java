package com.evanzeimet.queryinfo.sort;

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
