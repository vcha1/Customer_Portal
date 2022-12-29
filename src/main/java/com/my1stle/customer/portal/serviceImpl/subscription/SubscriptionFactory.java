package com.my1stle.customer.portal.serviceImpl.subscription;

import com.adobe.sign.model.agreements.AgreementInfo;
import com.adobe.sign.model.agreements.SigningUrl;
import com.adobe.sign.model.agreements.SigningUrlSetInfo;
import com.my1stle.customer.portal.persistence.model.SubscriptionEntity;
import com.my1stle.customer.portal.persistence.model.SubscriptionTermsOfServiceEntity;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.adobe.sign.client.facade.AdobeSignFacadeClient;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.model.SubscriptionTermsOfService;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;
import com.my1stle.customer.portal.serviceImpl.model.OrderProxy;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
class SubscriptionFactory {

    private ProductService productService;
    private InstallationService installationService;

    private OdooInstallationData odooInstallationData;
    private AdobeSignFacadeClient adobeSignFacadeClient;


    @Autowired
    SubscriptionFactory(ProductService productService, InstallationService installationService, AdobeSignFacadeClient adobeSignFacadeClient) {
        this.productService = productService;
        this.installationService = installationService;
        this.adobeSignFacadeClient = adobeSignFacadeClient;
    }


    /*
    @Autowired
    SubscriptionFactory(ProductService productService, OdooInstallationData odooInstallationData, AdobeSignFacadeClient adobeSignFacadeClient) {
        this.productService = productService;
        this.odooInstallationData = odooInstallationData;
        this.adobeSignFacadeClient = adobeSignFacadeClient;
    }*/

    Subscription create(SubscriptionEntity subscriptionEntity) {
        return new SubscriptionProxy(subscriptionEntity);
    }

    private class SubscriptionProxy implements Subscription {

        private Long id;
        private Boolean isActivated;
        private User user;
        private BigDecimal price;
        private Product product;
        private PaymentSchedule paymentSchedule;
        private Installation installation;
        private ZonedDateTime startingDate;
        private ZonedDateTime expirationDate;
        private List<Order> orders;
        private SubscriptionTermsOfService subscriptionTermsOfService;

        // Lazy loaders
        Supplier<Product> productSupplier;
        Supplier<OdooInstallationData> installationSupplier;
        Supplier<SubscriptionTermsOfService> subscriptionTermsOfServiceSupplier;
        Supplier<List<Order>> ordersSupplier;

        SubscriptionProxy(SubscriptionEntity entity) {

            this.id = entity.getId();
            this.user = entity.getOwner();
            this.price = entity.getPrice();
            this.paymentSchedule = entity.getProduct().getPaymentSchedule();
            this.startingDate = entity.getStartingDate();
            this.expirationDate = entity.getExpirationDate();
            this.isActivated = entity.getActive();

            // Lazy Loaders
            //this.installationSupplier = () -> installationService.getInstallationById(entity.getInstallationId());
            this.installationSupplier = () -> new OdooInstallationData(entity.getInstallationId());
            this.productSupplier = () -> productService.getById(entity.getProduct().getId());
            this.subscriptionTermsOfService = new SubscriptionTermsOfServiceProxy(this, entity.getSubscriptionTermsOfServiceEntity());
            this.ordersSupplier = new OrdersSupplier(entity);

        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public User getOwner() {
            return user;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public PaymentSchedule getPaymentFrequency() {
            return paymentSchedule;
        }

        @Override
        public Product getProduct() {

            if (null == product) {
                product = productSupplier.get();
            }

            return product;
        }

        @Override
        public ZonedDateTime getExpirationDate() {
            return expirationDate;
        }
        /*
        @Override
        public Installation getInstallation() {

            if (null == installation) {
                installation = installationSupplier.get();
            }

            return installation;

        }
        */
        @Override
        public OdooInstallationData getOdooInstallationData() {

            if (null == odooInstallationData) {
                odooInstallationData = installationSupplier.get();
            }

            return odooInstallationData;

        }

        @Override
        public ZonedDateTime getStartingDate() {
            return this.startingDate;
        }

        @Override
        public SubscriptionTermsOfService getSubscriptionTermsOfService() {

            if (null == subscriptionTermsOfService) {
                subscriptionTermsOfService = subscriptionTermsOfServiceSupplier.get();
            }

            return subscriptionTermsOfService;
        }

        @Override
        public List<Order> getOrders() {

            if (null == orders) {
                orders = ordersSupplier.get();
            }

            return orders;
        }

        @Override
        public Boolean isActivated() {
            return isActivated;
        }

    }

    private class SubscriptionTermsOfServiceProxy implements SubscriptionTermsOfService {

        private Long id;
        private Subscription subscription;
        private String agreementId;
        private URL signingUrl;
        private URL completedDocumentURL;

        // Lazy Loaders
        private Supplier<URL> signingUrlSupplier;
        private Supplier<URL> completedDocumentUrlSupplier;

        SubscriptionTermsOfServiceProxy(Subscription subscription, SubscriptionTermsOfServiceEntity subscriptionTermsOfServiceEntity) {

            this.id = subscriptionTermsOfServiceEntity.getId();
            this.subscription = subscription;
            this.agreementId = subscriptionTermsOfServiceEntity.getAgreementId();

            // Lazy Loaders
            this.signingUrlSupplier = new ESignURLSupplier(this.subscription.getOwner(), this.agreementId);
            this.completedDocumentUrlSupplier = () -> adobeSignFacadeClient.agreements().getCombinedDocumentURL(this.agreementId);

        }

        @Override
        public Long getId() {
            return this.id;
        }

        @Override
        public Boolean getHasAgreedToTermsOfService() {

            AgreementInfo agreementInfo = adobeSignFacadeClient.agreements().getAgreementInfo(this.agreementId);
            return agreementInfo.getStatus().equals(AgreementInfo.StatusEnum.SIGNED);

        }

        @Override
        public Subscription getSubscription() {
            return this.subscription;
        }

        @Override
        public String getAgreementId() {
            return this.agreementId;
        }

        @Override
        public URL getSigningURL() {

            if (null == signingUrl) {
                signingUrl = signingUrlSupplier.get();
            }

            return signingUrl;
        }

        @Override
        public URL getCompletedDocumentURL() {

            if (null == completedDocumentURL) {
                completedDocumentURL = completedDocumentUrlSupplier.get();
            }

            return completedDocumentURL;

        }

    }

    private class ESignURLSupplier implements Supplier<URL> {

        private User user;
        private String agreementId;

        ESignURLSupplier(User user, String agreementId) {
            this.user = user;
            this.agreementId = agreementId;
        }

        @Override
        public URL get() {

            List<SigningUrlSetInfo> signingUrlSetInfos = adobeSignFacadeClient.agreements().getSigningUrlSetInfos(this.agreementId);

            Optional<SigningUrl> userEsignUrl = signingUrlSetInfos.stream()
                    .map(SigningUrlSetInfo::getSigningUrls)
                    .flatMap(List::stream)
                    .filter(eSignUrl -> StringUtils.equals(user.getEmail(), eSignUrl.getEmail()))
                    .findFirst();

            if (!userEsignUrl.isPresent()) {
                throw new ResourceNotFoundException("E-Sign URL Not Found!");
            } else {
                try {
                    return new URL(userEsignUrl.get().getEsignUrl());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }


    private static class OrdersSupplier implements Supplier<List<Order>> {

        private SubscriptionEntity subscriptionEntity;

        OrdersSupplier(SubscriptionEntity subscriptionEntity) {
            this.subscriptionEntity = subscriptionEntity;
        }

        @Override
        public List<Order> get() {
            return subscriptionEntity.getOrders()
                    .stream()
                    .map(OrderProxy::from)
                    .collect(Collectors.toList());

        }
    }

}
