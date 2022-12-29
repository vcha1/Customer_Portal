package com.my1stle.customer.portal.web.controller.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.datetimeselection.DateTimeSelectionService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.scheduling.AppointmentAlreadyBookedException;
import com.my1stle.customer.portal.service.scheduling.CustomerSelfSchedulingRole;
import com.my1stle.customer.portal.service.scheduling.NoAvailabilityException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleService;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
class PublicSchedulingControllerCalendarDateTimeSelectionHandler implements PublicSchedulingControllerDateTimeSelectionHandler {

    private InstallationService installationService;
    private SelfScheduleService selfScheduleService;
    private DateTimeSelectionService dateTimeSelectionService;
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicSchedulingControllerCalendarDateTimeSelectionHandler.class);
    private static final Set<CustomerSelfSchedulingRole> ALLOWABLE_ROLES_VIEW_CALENDARS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(CustomerSelfSchedulingRole.SALES_REP, CustomerSelfSchedulingRole.DEALER_ADMIN))
    );

    private static final Set<CustomerSelfSchedulingRole> ALLOWABLE_ROLES_TO_SCHEDULE = Collections.unmodifiableSet(
            Collections.singleton(CustomerSelfSchedulingRole.DEALER_ADMIN)
    );

    @Autowired
    PublicSchedulingControllerCalendarDateTimeSelectionHandler(
            @Qualifier("publicInstallationService") InstallationService installationService,
            SelfScheduleService selfScheduleService,
            DateTimeSelectionService dateTimeSelectionService,
            ObjectMapper objectMapper) {
        this.installationService = installationService;
        this.selfScheduleService = selfScheduleService;
        this.dateTimeSelectionService = dateTimeSelectionService;
        this.objectMapper = objectMapper;

    }

    /**
     * @param customerSelfSchedulingRequestDecodedJwt
     * @param date
     * @param model
     * @param httpServletRequest
     * @return template path
     */
    @Override
    public String dateTimeSelection(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt, LocalDate date, Model model, HttpServletRequest httpServletRequest) {


        PublicDateTimeSelectionDto request = null;

        CustomerSelfSchedulingRole role = customerSelfSchedulingRequestDecodedJwt.getRole();

        if (!ALLOWABLE_ROLES_VIEW_CALENDARS.contains(role)) {
            throw new BadRequestException(String.format("Role of %s is not allowed to view calendar", role));
        }

        Installation installation = this.installationService.getInstallationById(customerSelfSchedulingRequestDecodedJwt.getInstallationId());

        try {

            request = this.selfScheduleService.generateSelfScheduleRequestDto(customerSelfSchedulingRequestDecodedJwt);
            model.addAttribute("request", request);

        } catch (SelfScheduleException e) {
            LOGGER.error(e.getMessage(), e);
            return "scheduling/public-date-time-selection-unavailable";
        } catch (AppointmentAlreadyBookedException e) {
            LOGGER.warn(e.getMessage(), e);
            return "scheduling/public-appointment-already-booked";
        } catch (NoAvailabilityException e) {
            LOGGER.warn(e.getMessage(), e);
            return "scheduling/public-no-available-time-slots";
        }

        List<Calendar> personnel = this.dateTimeSelectionService
                .getAvailableCalendars(request.getInstallationId(), request.getSkillIds());

        if (personnel.isEmpty()) {
            LOGGER.error("No Available Personnel! request={}", customerSelfSchedulingRequestDecodedJwt.toString());
            return "scheduling/public-no-available-time-slots";
        }

        Appointment appointment = this.dateTimeSelectionService
                .createAppointment(request.getInstallationId(), request.getDuration());

        try {
            model.addAttribute("appointment", objectMapper.writeValueAsString(appointment));
            model.addAttribute("resources", objectMapper.writeValueAsString(personnel));
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }

        boolean allowedToScheduleOfBehalfOfCustomer = ALLOWABLE_ROLES_TO_SCHEDULE.contains(role);

        model.addAttribute("allowed_to_scheduled_on_behalf_of_customer", allowedToScheduleOfBehalfOfCustomer);

        model.addAttribute("availability_title",
                allowedToScheduleOfBehalfOfCustomer ? String.format("Scheduling Installation for %s", installation.getCustomerName()) : String.format("Viewing Current Availability for %s", installation.getCustomerName())
        );
        model.addAttribute("availability_subtitle",
                allowedToScheduleOfBehalfOfCustomer ? String.format("Please select a date and time that is most convenient for %s", installation.getCustomerName()) : String.format("The following is our current availability for %s", installation.getCustomerName())
        );

        return "scheduling/public-date-time-selection-via-calendar";

    }

}
