package com.my1stle.customer.portal.service.installation.detail.model;

public class ContractDetail {
	private String label;
	private String link;

	public ContractDetail(String label, String link) {
		this.label = label;
		this.link = link;
	}

	public String getLabel() {
		return label;
	}

	public String getLink() {
		return link;
	}
}
