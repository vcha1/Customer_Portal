package com.my1stle.customer.portal.serviceImpl.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.pricing.FeeCalculator;
import com.my1stle.customer.portal.service.pricing.ProductDiscountCalculator;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.service.pricing.SubtotalCalculator;
import com.my1stle.customer.portal.service.product.ProductPricing;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DefaultServiceRequestPricingService implements ServiceRequestPricingService {

    private SubtotalCalculator subtotalCalculator;
    private ProductDiscountCalculator productDiscountCalculator;
    private FeeCalculator feeCalculator;

    @Autowired
    public DefaultServiceRequestPricingService(
            SubtotalCalculator subtotalCalculator,
            ProductDiscountCalculator productDiscountCalculator,
            FeeCalculator feeCalculator) {
        this.subtotalCalculator = subtotalCalculator;
        this.productDiscountCalculator = productDiscountCalculator;
        this.feeCalculator = feeCalculator;
    }

    @Override
    public BigDecimal getTotalPrice(ServiceRequest serviceRequest, PaymentMethod paymentMethod) {

        User user = serviceRequest.getUser();
        //Installation installation = serviceRequest.getInstallation();
        OdooInstallationData odooInstallationData = serviceRequest.getOdooInstallationData();
        Product product = serviceRequest.getProduct();
        int quantity = serviceRequest.getQuantity();

        final BigDecimal subTotal = this.getSubTotalAmount(serviceRequest);

        final BigDecimal discountAmount = this.getDiscountAmount(serviceRequest);

        final BigDecimal subTotalAfterDiscount = subTotal.subtract(discountAmount);

        final BigDecimal totalPrice = subTotalAfterDiscount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subTotalAfterDiscount;

        final BigDecimal convenienceFee = this.getConvenienceFee(serviceRequest, paymentMethod);

        return totalPrice
                .add(convenienceFee)
                .setScale(2, RoundingMode.DOWN);

    }

    @Override
    public BigDecimal getSubTotalAmount(ServiceRequest serviceRequest) {

        return subtotalCalculator.getSubtotal(
                //serviceRequest.getInstallation(),
                serviceRequest.getOdooInstallationData(),
                serviceRequest.getProduct(),
                serviceRequest.getQuantity()
        );

    }

    @Override
    public BigDecimal getRequiredDepositAmount(ServiceRequest serviceRequest) {

        Product product = serviceRequest.getProduct();

        final BigDecimal requiredDepositPercentage = product.getRequiredDepositPercentage();

        if (requiredDepositPercentage.compareTo(BigDecimal.ONE) > 0 || requiredDepositPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Required Deposit Percentage meets or exceeds 100 percent or percentage is negative!");
        }

        final BigDecimal subTotal = this.getSubTotalAmount(serviceRequest);

        final BigDecimal requiredAmount = subTotal.multiply(requiredDepositPercentage);

        return requiredAmount.compareTo(ProductPricing.MAX_REQUIRED_DEPOSIT_AMOUNT) > 0 ? ProductPricing.MAX_REQUIRED_DEPOSIT_AMOUNT : requiredAmount;

    }

    @Override
    public BigDecimal getDiscountAmount(ServiceRequest serviceRequest) {

        return productDiscountCalculator.getTotalDiscountAmount(
                serviceRequest.getUser(),
                //serviceRequest.getInstallation(),
                serviceRequest.getOdooInstallationData(),
                serviceRequest.getProduct(),
                serviceRequest.getQuantity()
        );

    }

    @Override
    public BigDecimal getConvenienceFee(ServiceRequest serviceRequest, PaymentMethod paymentMethod) {
        return feeCalculator.calculate(serviceRequest, paymentMethod);
    }

}