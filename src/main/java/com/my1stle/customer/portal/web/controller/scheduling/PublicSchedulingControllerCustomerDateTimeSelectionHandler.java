package com.my1stle.customer.portal.web.controller.scheduling;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.scheduling.AppointmentAlreadyBookedException;
import com.my1stle.customer.portal.service.scheduling.NoAvailabilityException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleService;
import com.my1stle.customer.portal.service.slack.SlackChannel;
import com.my1stle.customer.portal.service.slack.SlackMessage;
import com.my1stle.customer.portal.service.slack.SlackService;
import com.my1stle.customer.portal.serviceImpl.PublicInstallationService;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
class PublicSchedulingControllerCustomerDateTimeSelectionHandler implements PublicSchedulingControllerDateTimeSelectionHandler {

    private SelfScheduleService selfScheduleService;
    private InstallationService publicInstallationService;
    private SlackService slackService;

    private final PublicSchedulingControllerDateTimeSelectionHandler publicSchedulingOpportunityControllerDateTimeSelectionHandler;

    private static Logger LOGGER = LoggerFactory.getLogger(PublicSchedulingControllerCustomerDateTimeSelectionHandler.class);

    private static final Integer MAXIMUM_RETRY_COUNT = 20;

    @Autowired
    PublicSchedulingControllerCustomerDateTimeSelectionHandler(
            SelfScheduleService selfScheduleService,
            PublicInstallationService publicInstallationService,
            SlackService slackService,
            @Qualifier("publicSchedulingOpportunityControllerDateTimeSelectionHandler") PublicSchedulingControllerDateTimeSelectionHandler publicSchedulingOpportunityControllerDateTimeSelectionHandler) {

        this.selfScheduleService = selfScheduleService;
        this.publicInstallationService = publicInstallationService;
        this.slackService = slackService;

        this.publicSchedulingOpportunityControllerDateTimeSelectionHandler = publicSchedulingOpportunityControllerDateTimeSelectionHandler;
    }

    /**
     * @param customerSelfSchedulingRequestDecodedJwt
     * @param date
     * @param model
     * @param httpServletRequest
     * @return template path
     */
    @Override
    public String dateTimeSelection(
            CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt,
            LocalDate date,
            Model model,
            HttpServletRequest httpServletRequest) {

        if (!StringUtils.isBlank(customerSelfSchedulingRequestDecodedJwt.getOpportunityId())) {
            return this.publicSchedulingOpportunityControllerDateTimeSelectionHandler
                    .dateTimeSelection(customerSelfSchedulingRequestDecodedJwt, date, model, httpServletRequest);
        }

        PublicDateTimeSelectionDto request = null;

        HttpSession session = httpServletRequest.getSession();

        // restart retry count
        if (session.getAttribute("retry_count") == null || date == null) {
            session.setAttribute("retry_count", 0);
        }

        Integer retryCount = (Integer) session.getAttribute("retry_count");
        session.setAttribute("retry_count", ++retryCount);

        if (retryCount > MAXIMUM_RETRY_COUNT) {
            return "scheduling/public-no-available-time-slots";
        }

        try {

            request = this.selfScheduleService.generateSelfScheduleRequestDto(customerSelfSchedulingRequestDecodedJwt, true, date);
            model.addAttribute("request", request);
            model.addAttribute("token", request.getToken());
            model.addAttribute("next_date", DateTimeFormatter.ISO_LOCAL_DATE.format(request.getStartDateTime().plusDays(1).toLocalDate()));

        } catch (SelfScheduleException e) {
            LOGGER.error(e.getMessage(), e);
            return "scheduling/public-date-time-selection-unavailable";
        } catch (AppointmentAlreadyBookedException e) {
            LOGGER.warn(e.getMessage());
            return "scheduling/public-appointment-already-booked";
        } catch (NoAvailabilityException e) {
            LOGGER.warn(e.getMessage());
            return "scheduling/public-no-available-time-slots";
        }

        Installation installation = this.publicInstallationService.getInstallationById(request.getInstallationId());
        model.addAttribute("installation", installation);

        try {

            slackService.postMessage(
                    new SlackMessage(String.format("<https://1stlight.my.salesforce.com/%s|%s> has begun scheduling their appointment for %s",
                            installation.getId(),
                            installation.getName(),
                            DateTimeFormatter.ISO_LOCAL_DATE.format(request.getStartDateTime())
                    )),

                    SlackChannel.CUSTOMER_SCHEDULING);

        } catch (Exception e) {
            LOGGER.error("Unable to send slack notification! {}", e.getMessage(), e);
        }

        return "scheduling/public-date-time-selection";

    }

}
