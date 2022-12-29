package com.my1stle.customer.portal.service.scheduling;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;

import java.time.LocalDate;

public interface SelfScheduleService {

    /**
     * @param customerSelfSchedulingRequestDecodedJwt decodedJWT
     * @return an instance of {@link PublicDateTimeSelectionDto}
     * @throws SelfScheduleException             when decodedJWT is expired
     * @throws AppointmentAlreadyBookedException when there is already an appointment booked
     * @throws NoAvailabilityException           when there no available appointment times
     * @implSpec must return non-null {@link PublicDateTimeSelectionDto}
     */
    PublicDateTimeSelectionDto generateSelfScheduleRequestDto(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt) throws SelfScheduleException, AppointmentAlreadyBookedException, NoAvailabilityException;

    /**
     * @param customerSelfSchedulingRequestDecodedJwt      decodedJWT
     * @param preselectEarliestPossibleAppointmentTimeSlot indicates whether the date and time should be pre-populated
     * @param preferredLocalDate                           the local date in which would the earliest possible appointment time should be
     * @return an instance of {@link PublicDateTimeSelectionDto} with {@link PublicDateTimeSelectionDto#getStartDateTime()} and {@link PublicDateTimeSelectionDto#getDuration()} not return null
     * @throws SelfScheduleException             when decodedJWT is expired
     * @throws AppointmentAlreadyBookedException when there is already an appointment booked
     * @throws NoAvailabilityException           when there no available appointment times
     * @implSpec must return non-null {@link PublicDateTimeSelectionDto}
     */
    PublicDateTimeSelectionDto generateSelfScheduleRequestDto(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt, boolean preselectEarliestPossibleAppointmentTimeSlot, LocalDate preferredLocalDate) throws SelfScheduleException, AppointmentAlreadyBookedException, NoAvailabilityException;

    /**
     * @param publicDateTimeSelectionDto representing the date time selection
     * @return eventDetail
     * @throws SelfScheduleException when an validation or error occurs while commiting the event
     */
    EventDetail schedule(PublicDateTimeSelectionDto publicDateTimeSelectionDto) throws SelfScheduleException;

}