package com.my1stle.customer.portal.service.adobe.sign.client;

import com.adobe.sign.api.AgreementsApi;
import com.adobe.sign.model.agreements.DocumentUrl;
import com.adobe.sign.model.agreements.SigningUrlResponse;
import com.adobe.sign.model.agreements.UserAgreement;
import com.adobe.sign.model.agreements.UserAgreements;
import com.adobe.sign.utils.ApiException;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

@Service
public class AgreementsClient {
	private final String accessToken;
	private final AgreementsApi agreementsApi;
	private final String apiUser;

	@Autowired
	public AgreementsClient(
			@Value("${adobe.sign.api.token}") String accessToken,
			@Value("${adobe.sign.api.client.user}") String apiUser,
			//@Value("email:WestCoastSales@1stlightenergy.com") String apiUser,
			AgreementsApi agreementsApi
	) {
		this.accessToken = accessToken;
		this.agreementsApi = agreementsApi;
		this.apiUser = apiUser;
	}

	/**
	 * Gets an url to view the combined agreement document with the standard
	 * @param agreementId the e-sign id for the agreement
	 * @return the url to the completed document
	 * @throws RuntimeException when generating the link fails
	 */
	public String getAgreementUrl(String agreementId) {
		try {
			DocumentUrl url = agreementsApi.getCombinedDocumentUrl(
					generateStandardHeaders(),
					agreementId,
					null,
					null,
					false,
					false
			);

			return url.getUrl();
		}
		catch (ApiException e) {
			throw new RuntimeException("Failed to get URL for contract", e);
		}
	}

	private MultivaluedMap<String, String> generateStandardHeaders() {
		MultivaluedMap<String, String> standardHeaders = new MultivaluedMapImpl();
		standardHeaders.add("Access-Token", this.accessToken);
		standardHeaders.add("x-api-user", this.apiUser);
		return standardHeaders;
	}


	private MultivaluedMap<String, String> generateMultipleStandardHeaders(String echoSignEmail) {
		MultivaluedMap<String, String> standardHeaders = new MultivaluedMapImpl();
		standardHeaders.add("Access-Token", this.accessToken);
		standardHeaders.add("x-api-user", echoSignEmail);
		return standardHeaders;
	}

	public List<UserAgreement> getAgreement(String email, String echoSignEmail) {
		try {

			UserAgreements agreements = agreementsApi.getAgreements( generateMultipleStandardHeaders(echoSignEmail),
					email,
					null,
					null,
					null);

			return agreements.getUserAgreementList();
		}
		catch (ApiException e) {
			throw new RuntimeException("Failed to get URL for contract", e);
		}
	}


	public String getSigningUrls(String agreementId) {
		try {
			SigningUrlResponse signingUrl = agreementsApi.getSigningUrl(
					generateStandardHeaders(),
					agreementId
			);

			return signingUrl.getSigningUrlSetInfos().get(0).getSigningUrls().get(0).getEsignUrl();
		} catch (ApiException e) {
			throw new RuntimeException("Failed to get URL for contract", e);
		}
	}
}
