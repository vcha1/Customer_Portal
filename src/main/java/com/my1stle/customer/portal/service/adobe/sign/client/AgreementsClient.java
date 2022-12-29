package com.my1stle.customer.portal.service.adobe.sign.client;

import com.adobe.sign.api.AgreementsApi;
import com.adobe.sign.model.agreements.DocumentUrl;
import com.adobe.sign.utils.ApiException;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MultivaluedMap;

@Service
public class AgreementsClient {
	private final String accessToken;
	private final AgreementsApi agreementsApi;
	private final String apiUser;

	@Autowired
	public AgreementsClient(
			@Value("${adobe.sign.api.token}") String accessToken,
			@Value("${adobe.sign.api.client.user}") String apiUser,
			AgreementsApi agreementsApi
	) {
		this.accessToken = accessToken;
		this.agreementsApi = agreementsApi;
		this.apiUser = apiUser;
	}

	/**
	 * Gets a url to view the combined agreement document with the standard
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
}
