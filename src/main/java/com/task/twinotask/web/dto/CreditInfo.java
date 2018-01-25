package com.task.twinotask.web.dto;

public class CreditInfo {

	private final Long id;
	private final int limit;

	public CreditInfo(Long id, int creditLimit) {
		this.id = id;
		this.limit = creditLimit;
	}

	public int getLimit() {
		return limit;
	}

	public Long getId() {
		return id;
	}

}
