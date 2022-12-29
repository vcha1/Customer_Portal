package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollDetails;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollResult;
import com.my1stle.customer.portal.persistence.model.ServiceRequestEntity;
import com.my1stle.customer.portal.persistence.repository.ServiceRequestRepository;
import com.my1stle.customer.portal.service.TruckRollRequestFactory;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestScheduler;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class TruckRollServiceRequestScheduler implements ServiceRequestScheduler {

    private TruckRollClient truckRollClient;
    private TruckRollRequestFactory truckRollRequestFactory;
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public TruckRollServiceRequestScheduler(
            TruckRollClient truckRollClient,
            TruckRollRequestFactory truckRollRequestFactory,
            ServiceRequestRepository serviceRequestRepository) {

        this.truckRollClient = truckRollClient;
        this.truckRollRequestFactory = truckRollRequestFactory;
        this.serviceRequestRepository = serviceRequestRepository;

    }

    /**
     * Schedules a service job based on what the user has requested
     *
     * @param serviceRequest serviceRequest representing what the user has request
     * @implNote this implementation schedules a truck roll based on provided service request
     */
    @Override
    public void scheduleServiceRequest(ServiceRequest serviceRequest) {

        List<NewTruckRollDetails> details = this.truckRollRequestFactory.from(serviceRequest);

        NewTruckRollResult newTruckRollResult = this.truckRollClient
                .events()
                .create(details);

        if (newTruckRollResult.getSuccessful()) {
            saveEventId(serviceRequest, newTruckRollResult);
        } else {
            // TODO : figure out a better way to handle
            throw new InternalServerErrorException(newTruckRollResult.getErrorMessage());
        }

    }

    private void saveEventId(ServiceRequest serviceRequest, NewTruckRollResult newTruckRollResult) {

        Optional<ServiceRequestEntity> serviceRequestEntityOptional = this.serviceRequestRepository.findById(serviceRequest.getId());

        if (!serviceRequestEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("Service Request Not Found!");
        }

        ServiceRequestEntity serviceRequestEntity = serviceRequestEntityOptional.get();

        serviceRequestEntity.setEventId(newTruckRollResult.getId());

        this.serviceRequestRepository.save(serviceRequestEntity);

    }

}


