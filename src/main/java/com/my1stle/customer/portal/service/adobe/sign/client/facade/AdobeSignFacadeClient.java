package com.my1stle.customer.portal.service.adobe.sign.client.facade;

import com.adobe.sign.api.AgreementsApi;
import com.adobe.sign.api.LibraryDocumentsApi;
import com.my1stle.customer.portal.service.adobe.sign.api.Agreements;
import com.my1stle.customer.portal.service.adobe.sign.api.LibraryDocuments;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;

/**
 * This is a facade to adobe signs sdk
 *
 * @see <a href="https://www.adobe.com/devnet-docs/adobesign/reference/sdk/">Adobe Sign SDK</a>
 */
public class AdobeSignFacadeClient {

    private MultivaluedMap<String, String> headers = new MultivaluedMapImpl();

    private Agreements agreements = null;
    private LibraryDocuments libraryDocuments = null;

    public AdobeSignFacadeClient(String token) {
        this.headers.add("Access-Token", token);
    }

    public AdobeSignFacadeClient(String token, String apiUser) {
        this(token);
        this.headers.add("x-api-user", String.format("email:%s", apiUser));
    }

    public Agreements agreements() {
        if (null == agreements) {
            agreements = new DefaultAgreements(new AgreementsApi(), this.headers);
        }
        return agreements;
    }

    public LibraryDocuments libraryDocuments() {

        if (null == libraryDocuments) {
            libraryDocuments = new DefaultLibraryDocuments(new LibraryDocumentsApi(), this.headers);
        }

        return libraryDocuments;

    }

}