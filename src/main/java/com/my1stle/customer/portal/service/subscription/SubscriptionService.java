package com.my1stle.customer.portal.service.subscription;

import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.web.dto.subscription.SubscriptionAgreementDto;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    /**
     * @param ownerId
     * @param subscriptionId
     * @return optional containing subscription
     */
    Optional<Subscription> getByOwnerAndSubscriptionId(Long ownerId, Long subscriptionId);

    /**
     * @param ownerId
     * @param productId
     * @param installationId
     * @return
     */
    Optional<Subscription> getByOwnerProductAndInstallationId(Long ownerId, Long productId, String installationId);

    /**
     * @param ownerId
     * @param installationId
     * @return all active subscriptions for the given owner and owner's installation;
     */
    List<Subscription> getOwnersActiveSubscriptions(Long ownerId, String installationId);

    /**
     * @param ownerId
     * @param subscriptionId
     * @param orderId
     * @throws SubscriptionException
     */
    Subscription activateSubscription(Long ownerId, Long subscriptionId, String orderId) throws SubscriptionException;

    /**
     * @param subscription
     * @return total price for the subscription
     */
    BigDecimal getTotalPrice(Subscription subscription);

    /**
     * @param subscriptionAgreementDto
     * @return subscription
     * @throws SubscriptionException
     */
    Subscription subscribe(SubscriptionAgreementDto subscriptionAgreementDto) throws SubscriptionException;


    /**
     * @param ownerId
     * @param subscriptionId
     * @return resource representing the subscription agreement
     */
    Optional<Resource> getSubscriptionAgreementResource(Long ownerId, Long subscriptionId);

}
