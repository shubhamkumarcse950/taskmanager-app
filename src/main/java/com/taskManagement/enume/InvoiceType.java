package com.taskManagement.enume;

public enum InvoiceType {
	PROFORMA("PI"), TAX("TI");

	private final String prefix;

	InvoiceType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}