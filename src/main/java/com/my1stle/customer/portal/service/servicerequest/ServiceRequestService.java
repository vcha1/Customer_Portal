package com.my1stle.customer.portal.service.servicerequest;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.web.dto.scheduling.DateTimeSelectionDto;

public interface ServiceRequestService {

    Order generateOrder(ServiceRequest serviceRequest);

    //ServiceRequest create(Product product, Installation installation, int quantity);

    ServiceRequest create(Product product, OdooInstallationData odooInstallationData, int quantity);

    ServiceRequest create(DateTimeSelectionDto dateTimeSelectionDto);

    ServiceRequest getById(Long id);

    void setPaymentDetail(ServiceRequest serviceRequest, PaymentDetail paymentDetail);

}
