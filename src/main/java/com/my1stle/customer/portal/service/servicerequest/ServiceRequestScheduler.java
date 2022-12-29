package com.my1stle.customer.portal.service.servicerequest;

import com.my1stle.customer.portal.service.model.ServiceRequest;

public interface ServiceRequestScheduler {

    /**
     * Schedules a service job based on what the user has requested
     *
     * @param serviceRequest serviceRequest representing what the user has request
     */
    void scheduleServiceRequest(ServiceRequest serviceRequest);

}
