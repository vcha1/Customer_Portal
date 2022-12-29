package com.my1stle.customer.portal.service.subscription;

import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.model.SubscriptionTermsOfService;

import java.util.Optional;

public interface SubscriptionTermsOfServiceService {

    SubscriptionTermsOfService create(Subscription subscription);

    Optional<SubscriptionTermsOfService> getBySubscriptionId(Long subscriptionId);

}
