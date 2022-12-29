package com.my1stle.customer.portal.service.model;

import java.net.URL;

public interface SubscriptionTermsOfService {

    Long getId();

    Boolean getHasAgreedToTermsOfService();

    Subscription getSubscription();

    String getAgreementId();

    URL getSigningURL();

    URL getCompletedDocumentURL();

}