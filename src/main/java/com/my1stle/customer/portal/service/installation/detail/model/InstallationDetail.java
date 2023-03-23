package com.my1stle.customer.portal.service.installation.detail.model;

import com.my1stle.customer.portal.service.installation.configuration.salesforce.utility.DateTimeUtility;
import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.my1stle.customer.portal.service.dropbox.DropboxImageService;

public class InstallationDetail {
	private InstallationSalesforceObject information;
	private boolean systemTurnOnPackagePresent;
	private List<ContractDetail> contractDetails;

	private String dropboxImageService;

	public InstallationDetail(
			InstallationSalesforceObject information,
			boolean systemTurnOnPackagePresent,
			List<ContractDetail> contractDetails,

			String dropboxImageService
	) {
		this.information = information;
		this.systemTurnOnPackagePresent = systemTurnOnPackagePresent;
		this.contractDetails = contractDetails;

		this.dropboxImageService = dropboxImageService;
	}

	public String getDropboxImageService(){
		return this.dropboxImageService;
	}



	public String getId() {
		return this.information.getId();
	}

	public String getName() {
		return this.information.getName();
	}

	public String getCustomerName() {
		return this.information.getCustomerName();
	}

	public String getCustomerEmail() {
		return this.information.getCustomerEmail();
	}

	public String getCustomerPhoneNumber() {
		return this.information.getCustomerPhoneNumber();
	}

	public Double getSystemSize() {
		return this.information.getSystemSize();
	}

	public String getInstallStatus() {
		return this.information.getInstallStatus();
	}

	public String getStreet() {
		return this.information.getAddress();
	}

	public String getCity() {
		return this.information.getCity();
	}

	public String getState() {
		return this.information.getState();
	}

	public String getZipCode() {
		return this.information.getZipCode();
	}

	public String getDesignLink() {
		return this.information.getDesignLink();
	}

	public LocalDate getContractSignedDate() {
		return this.information.getContractSignedDate();
	}

	public LocalDateTime getLocalScheduledPsaDateTime() {
		ZonedDateTime dt = this.information.getPsaScheduledDate();
		return dt == null ? null : DateTimeUtility.interpretAsLocalDateTime(dt);
	}

	public LocalDateTime getLocalCompletedPsaDateTime() {
		ZonedDateTime dt = this.information.getPsaCompleteDate();
		return dt == null ? null : DateTimeUtility.interpretAsLocalDateTime(dt);
	}

	public LocalDate getScheduledStartDate() {
		return this.information.getScheduledStartDate();
	}

	public ZonedDateTime getInstallCompletePendingPto() {
		return this.information.getInstallCompletePendingPto();
	}

	public LocalDate getInstallationDate() {
		return this.information.getInstallationDate();
	}

	public boolean isSystemTurnOnPackagePresent() {
		return systemTurnOnPackagePresent;
	}

	public List<ContractDetail> getContractDetails() {
		return this.contractDetails;
	}

	public String getPaymentTypeName() {
		return this.information.getPaymentTypeName();
	}

	public String getPaymentProviderName() {
		return this.information.getPaymentProviderName();
	}

	public String getPaymentProductName() {
		return this.information.getProductTypeName();
	}

	public String getDirectSalesCloserName() {
		return this.information.getDirectSalesCloserName();
	}

	public String getDirectSalesCloserPhone() {
		return this.information.getDirectSalesCloserPhone();
	}

	public String getDirectSalesCloserEmail() {
		return this.information.getDirectSalesCloserEmail();
	}

	public String getAccountManagerName() {
		return this.information.getAccountManagerName();
	}

	public String getAccountManagerPreformattedContactInfo() {
		return this.information.getAccountManagerPreformattedContactInfo();
	}

	public Double getTotalContractPrice() {
		return this.information.getTotalContractPrice();
	}

	public List<String> getInverterNames() {
		List<String> list = new ArrayList<>();

		return list;
	}

	public String getFormattedInverters() {
		List<String> list = getInverterNames();

		if(list.size() == 0)
			return "";
		else if(list.size() == 1)
			return list.get(0);
		else if(list.size() == 2)
			return list.get(0) + " and " + list.get(1);

		StringBuilder inProgress = new StringBuilder(list.get(0));
		int lastElementIdx = list.size() - 1;
		for(int idx = 1; idx < lastElementIdx; idx++) {
			inProgress.append(", ").append(list.get(idx));
		}
		inProgress.append(", and ").append(list.get(lastElementIdx));

		return inProgress.toString();
	}

	/*public String getMonitorName() {
		return null;//this.information.getMonitorName();
	}*/

	/*public String getPanelName() {
		return null;//this.information.getPanelName();
	}*/

	public Integer getPanelCountFromItems() {
		return this.information.getPanelCountFromItems();
	}

	public String getSoldProposalSrecTypeName() {
		return this.information.getSoldProposalSrecTypeName();
	}

	public Double getSoldProposalSrecUpfrontRebate() {
		return this.information.getSoldProposalSrecUpfrontRebate();
	}

	public Double getSoldProposalSrecSellPrice() {
		return this.information.getSoldProposalSrecSellPrice();
	}

	public Double getSoldProposalSrecSellDeEscalator() {
		return this.information.getSoldProposalSrecSellDeEscalator();
	}

	public Double getSoldProposalFederalTaxCredit() {
		return this.information.getSoldProposalFederalTaxCredit();
	}

	public Double getSoldProposalStateTaxCredit() {
		return this.information.getSoldProposalStateTaxCredit();
	}

	private boolean isNotEmpty(String input) {
		return input != null && !input.isEmpty();
	}
}
