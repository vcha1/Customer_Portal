package com.my1stle.customer.portal.service.installation.detail;

import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;
import com.my1stle.customer.portal.persistence.repository.InstallationSalesforceRepository;
import com.my1stle.customer.portal.persistence.repository.installation.detail.WorkOrderComponentRetriever;
import com.my1stle.customer.portal.service.adobe.sign.client.AgreementsClient;
import com.my1stle.customer.portal.service.attachment.AttachmentData;
import com.my1stle.customer.portal.service.design.DesignRetrieverService;
import com.my1stle.customer.portal.service.installation.detail.model.ContractDetail;
import com.my1stle.customer.portal.service.installation.detail.model.InstallationDetail;
import com.my1stle.customer.portal.service.session.SessionUserProvider;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my1stle.customer.portal.service.dropbox.DropboxImageService;

@Service
public class InstallationDetailRetriever {
	private static final String AGREEMENT_SIGNED_STATUS = "SIGNED";

	private final AgreementsClient agreementsClient;
	private final DesignRetrieverService designRetrieverService;
	private final InstallationSalesforceRepository installationSalesforceRepository;
	private final SessionUserProvider sessionUserProvider;
	private final SystemTurnOnPackageRetriever systemTurnOnPackageRetriever;
	private final WorkOrderComponentRetriever workOrderItemRetriever;

	private final DropboxImageService dropboxImageServiceRetreiver;

	@Autowired
	public InstallationDetailRetriever(
			AgreementsClient agreementsClient,
			DesignRetrieverService designRetrieverService,
			InstallationSalesforceRepository installationSalesforceRepository,
			SessionUserProvider sessionUserProvider,
			SystemTurnOnPackageRetriever systemTurnOnPackageRetriever,
			WorkOrderComponentRetriever workOrderItemRetriever,

			DropboxImageService dropboxImageServiceRetreiver
	) {
		this.agreementsClient = agreementsClient;
		this.designRetrieverService = designRetrieverService;
		this.installationSalesforceRepository = installationSalesforceRepository;
		this.sessionUserProvider = sessionUserProvider;
		this.systemTurnOnPackageRetriever = systemTurnOnPackageRetriever;
		this.workOrderItemRetriever = workOrderItemRetriever;

		this.dropboxImageServiceRetreiver = dropboxImageServiceRetreiver;
	}

	/**
	 * Retrieves the installation details for an installation if it is accessible by the current
	 * session's user, otherwise throwing an error.
	 * @param installationId the id of the installation
	 * @return the base installation information along with additional details
	 * @throws ResourceNotFoundException when the installation is either inaccessible or does not
	 *                                   exist.
	 * @throws RuntimeException when multiple qualifying installations are found (which indicates a
	 *                          data integrity problem in the database), or an unexpected error
	 *                          prevents retrieval of the complete information.
	 */
	public InstallationDetail retrieveAccessibleById(String installationId) throws ResourceNotFoundException {
		User sessionUser = sessionUserProvider.getUser();
		String userEmail = sessionUser.getEmail();

		InstallationSalesforceObject info = this.installationSalesforceRepository.selectByIdAndCustomerEmail(installationId, userEmail);
		if(info == null)
			throw new ResourceNotFoundException("Failed to find the correct installation");

		boolean hasStopPackage = systemTurnOnPackageRetriever.getExistenceByInstallationId(info.getId(), info.getName());

		List<ContractDetail> contractDetails = getContractDetailsForInstallation(info);

		String dropboxImagePath = dropboxImageServiceRetreiver.getDropboxImage(info);

		return new InstallationDetail(info, hasStopPackage, contractDetails, dropboxImagePath);
	}

	/**
	 * Retrieves the System Turn On Package blob (if it exists) for an installation (if it is
	 * accessible by the user).
	 * @param installationId the id of the installation
	 * @return the byte array (basic binary blob) of the STOP Package
	 * @throws ResourceNotFoundException when the installation is not accessible, or the STOP
	 *      Package does not exist.
	 * @throws RuntimeException when an unexpected error prevents the file from being found or
	 *      accessed.
	 */
	public byte[] retrieveAccessibleSystemTurnOnPackageByInstallationId(String installationId) {
		User sessionUser = sessionUserProvider.getUser();
		String userEmail = sessionUser.getEmail();

		InstallationSalesforceObject info = this.installationSalesforceRepository.selectByIdAndCustomerEmail(installationId, userEmail);
		if(info == null)
			throw new ResourceNotFoundException("Failed to find valid installation (id: " + installationId + ")");

		try {
			return systemTurnOnPackageRetriever.getDataByInstallationId(info.getId(), info.getName());
		}
		catch (RuntimeException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Retrieves the Installation's design image if it exists and is accessible by the user.
	 * @param installationId the id of the accessible installation
	 * @return the data and metadata for the accessible installation's main design image
	 */
	public AttachmentData retrieveAccessibleInstallationDesignImageData(String installationId) {
		User sessionUser = sessionUserProvider.getUser();
		String userEmail = sessionUser.getEmail();

		InstallationSalesforceObject info = this.installationSalesforceRepository.selectByIdAndCustomerEmail(installationId, userEmail);
		if(info == null)
			throw new ResourceNotFoundException("Failed to find valid installation (id: " + installationId + ")");

		try {
			return designRetrieverService.getMainDesignForPredesign(info.getSoldProposalPredesignId());
		}
		catch (RuntimeException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Generates the contract details necessary for the installation.  It automatically detects if
	 * the contract was split or not and what contracts were actually sent.
	 * @param install the full installation, mainly requiring contract information
	 * @return the contract details for the installation
	 * @throws RuntimeException when the Adobe Sign links fail to be generated for a contract
	 */
	private List<ContractDetail> getContractDetailsForInstallation(InstallationSalesforceObject install) {
		//get agreement id, utility id, and financing id, also checks to make sure that it is signed from salesforce
		Map<String, String> contractNamesToEsignIdMap = generateContractLabelToEsignIdMap(install);

		List<ContractDetail> contractDetails = new ArrayList<>();

		for(Map.Entry<String, String> labelToEsignIdEntry : contractNamesToEsignIdMap.entrySet()) {
			String agreementLabel = labelToEsignIdEntry.getKey();
			String agreementId = labelToEsignIdEntry.getValue();

			String url = agreementsClient.getAgreementUrl(agreementId);

			if(url != null)
				contractDetails.add(new ContractDetail(agreementLabel, url));
		}

		return contractDetails;
	}


	private Map<String, String> generateContractLabelToEsignIdMap(InstallationSalesforceObject install) {
		Map<String, String> contractLabelToEsignIdMap = new HashMap<>();

		String firstLightId = install.getFirstLightAgreementId();
		boolean firstLightSigned = isSigned(install.getFirstLightAgreementStatus());

		String utilityId = install.getUtilityAgreementId();
		boolean utilitySigned = isSigned(install.getUtilityAgreementStatus());

		String financingId = install.getFinancingAgreementId();
		boolean financingSigned = isSigned(install.getFinancingAgreementStatus());

		boolean hasSignedFirstLight = isNotNullOrEmpty(firstLightId) && firstLightSigned;
		boolean hasSignedUtility = isNotNullOrEmpty(utilityId) && utilitySigned;
		boolean hasSignedFinancing = isNotNullOrEmpty(financingId) && financingSigned;

		if(hasSignedFirstLight && hasSignedUtility && hasSignedFinancing && firstLightId.equals(utilityId) && firstLightId.equals(financingId)) {
			contractLabelToEsignIdMap.put("contract", firstLightId);
			return contractLabelToEsignIdMap;
		}

		if(hasSignedFirstLight) {
			contractLabelToEsignIdMap.put("1st Light", firstLightId);
		}
		if(hasSignedUtility) {
			contractLabelToEsignIdMap.put("utility", utilityId);
		}
		if(hasSignedFinancing) {
			contractLabelToEsignIdMap.put("financing", financingId);
		}

		return contractLabelToEsignIdMap;
	}

	private static boolean isNotNullOrEmpty(String source) {
		return source != null && !source.isEmpty();
	}

	private static boolean isSigned(String source) {
		return AGREEMENT_SIGNED_STATUS.equals(source);
	}
}
