package com.my1stle.customer.portal.serviceImpl;

import com.my1stle.customer.portal.persistence.model.PaymentDetailEntity;
import com.my1stle.customer.portal.persistence.model.PaymentMethodEntity;
import com.my1stle.customer.portal.persistence.repository.PaymentDetailRepository;
import com.my1stle.customer.portal.persistence.repository.PaymentMethodRepository;
import com.my1stle.customer.portal.service.UserProvider;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentDetailService {

    private PaymentDetailRepository paymentDetailRepository;
    private PaymentMethodRepository paymentMethodRepository;
    private UserProvider userProvider;

    @Autowired
    public PaymentDetailService(PaymentDetailRepository paymentDetailRepository, PaymentMethodRepository paymentMethodRepository, UserProvider userProvider) {
        this.paymentDetailRepository = paymentDetailRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.userProvider = userProvider;
    }

    public PaymentDetail save(BigDecimal amount, BigDecimal convenienceFee, PaymentMethod paymentMethod, String externalId) {

        PaymentMethodEntity paymentMethodEntity = this.paymentMethodRepository
                .findById(paymentMethod.getId()).orElseThrow(() -> new ResourceNotFoundException("Payment Method Not Found!"));

        PaymentDetailEntity detail = new PaymentDetailEntity();
        detail.setAmount(amount);
        detail.setConvenienceFee(convenienceFee);
        detail.setTotal(amount.add(convenienceFee));
        detail.setPaymentMethod(paymentMethodEntity);
        detail.setExternalId(externalId);
        detail.setOwner(userProvider.getCurrentUser());

        this.paymentDetailRepository.save(detail);

        return detail;

    }


}
