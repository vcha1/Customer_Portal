package com.my1stle.customer.portal.service.order;

import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.ServiceRequest;


public interface OrderService {

    Order generate(ServiceRequest serviceRequest);

}