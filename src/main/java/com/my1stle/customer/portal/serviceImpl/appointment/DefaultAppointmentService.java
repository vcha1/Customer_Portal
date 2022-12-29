package com.my1stle.customer.portal.serviceImpl.appointment;

import com.dev1stle.scheduling.system.v1.exception.EventServiceException;
import com.dev1stle.scheduling.system.v1.model.salesforce.CustomerInformation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.appointment.AppointmentService;
import com.my1stle.customer.portal.service.appointment.AppointmentStatus;
import com.my1stle.customer.portal.service.model.Appointment;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultAppointmentService implements AppointmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAppointmentService.class);

    private TruckRollClient truckRollClient;

    @Autowired
    public DefaultAppointmentService(TruckRollClient truckRollClient) {
        this.truckRollClient = truckRollClient;
    }

    @Override
    public Appointment get(Long eventId) {

        Optional<EventDetail> eventDetailOptional = this.truckRollClient.events().get(eventId);

        if (!eventDetailOptional.isPresent()) {
            LOGGER.warn("Appointment [Id : {}] Not Found!", eventId);
            throw new ResourceNotFoundException("Appointment Not Found!");
        }

        EventDetail eventDetail = eventDetailOptional.get();

        Optional<CustomerInformation> customerInformation = this.truckRollClient.customers().getByCustomerReferenceId(eventDetail.getExternalId());

        if (!customerInformation.isPresent()) {
            LOGGER.warn("Customer Information [Id : {}] Not Found!", eventDetail.getExternalId());
            throw new ResourceNotFoundException("Appointment Not Found!");
        }

        return DefaultAppointment.from(eventDetail, customerInformation.get());
    }

    @Override
    public Appointment confirm(Long eventId, String token) {

        Appointment appointment = getAppointmentForConfirmationOrRejection(eventId);

        try {
            EventDetail eventDetail = this.truckRollClient.events().confirmAppointment(appointment.getEventDetail().getId(), token);
            return this.get(eventDetail.getId());
        } catch (EventServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    public Appointment reject(Long eventId, String token) {

        Appointment appointment = getAppointmentForConfirmationOrRejection(eventId);

        try {
            EventDetail eventDetail = this.truckRollClient.events().rejectAppointment(appointment.getEventDetail().getId(), token);
            return this.get(eventDetail.getId());
        } catch (EventServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }

    }

    private Appointment getAppointmentForConfirmationOrRejection(Long eventId) {

        Appointment appointment = this.get(eventId);

        if (!appointment.getEventDetail().getNeedsConfirmation()) {
            LOGGER.warn("Appointment [Id : {}] does not need to be confirmed!", eventId);
            throw new BadRequestException("Appointment does need confirmation");
        }
        return appointment;

    }

    private static class DefaultAppointment implements Appointment {

        private AppointmentStatus appointmentStatus;
        private EventDetail eventDetail;
        private CustomerInformation customerInformation;

        private DefaultAppointment() {

        }

        static DefaultAppointment from(EventDetail eventDetail, CustomerInformation customerInformation) {

            DefaultAppointment appointment = new DefaultAppointment();

            appointment.eventDetail = eventDetail;
            appointment.customerInformation = customerInformation;

            if (eventDetail.isCancelled()) {
                appointment.appointmentStatus = AppointmentStatus.CANCELLED;
                return appointment;
            }

            if (eventDetail.getNeedsConfirmation()) {

                if (null != eventDetail.getTimeOfConfirmation()) {
                    appointment.appointmentStatus = AppointmentStatus.CONFIRMED;
                } else if (null != eventDetail.getTimeOfRejection()) {
                    appointment.appointmentStatus = AppointmentStatus.REJECTED;
                } else {
                    appointment.appointmentStatus = AppointmentStatus.NO_ANSWER;
                }

            } else {
                appointment.appointmentStatus = AppointmentStatus.CONFIRMATION_NOT_NEEDED;
            }

            return appointment;

        }


        @Override
        public AppointmentStatus getStatus() {
            return appointmentStatus;
        }

        @Override
        public EventDetail getEventDetail() {
            return eventDetail;
        }

        @Override
        public CustomerInformation getCustomerInformation() {
            return customerInformation;
        }

    }

}
