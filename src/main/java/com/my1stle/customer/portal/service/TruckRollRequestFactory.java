package com.my1stle.customer.portal.service;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollDetails;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;

import java.util.List;

public interface TruckRollRequestFactory {

    List<NewTruckRollDetails> from(ServiceRequest serviceRequest);

    List<NewTruckRollDetails> from(PublicDateTimeSelectionDto dateTimeSelectionDto);

}