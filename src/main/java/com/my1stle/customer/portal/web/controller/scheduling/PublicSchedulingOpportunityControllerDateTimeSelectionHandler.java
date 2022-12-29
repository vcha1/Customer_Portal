package com.my1stle.customer.portal.web.controller.scheduling;

import com.dev1stle.scheduling.system.v1.model.salesforce.CustomerInformation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.CalendarDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.OperationalAreaDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.SkillDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my1stle.customer.portal.persistence.model.SalesforceOpportunity;
import com.my1stle.customer.portal.persistence.repository.SalesforceOpportunityRepository;
import com.my1stle.customer.portal.service.scheduling.CustomerSelfSchedulingRole;
import com.my1stle.customer.portal.service.util.ActiveAppointmentPredicate;
import com.my1stle.customer.portal.service.util.IsValidSalesforceId;
import com.my1stle.customer.portal.service.util.NameValuePair;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PersonnelDto;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PublicSchedulingOpportunityControllerDateTimeSelectionHandler implements PublicSchedulingControllerDateTimeSelectionHandler {

    private final TruckRollClient truckRollClient;
    private final SalesforceOpportunityRepository salesforceOpportunityRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicSchedulingOpportunityControllerDateTimeSelectionHandler.class);

    @Autowired
    public PublicSchedulingOpportunityControllerDateTimeSelectionHandler(TruckRollClient truckRollClient, SalesforceOpportunityRepository salesforceOpportunityRepository) {
        this.truckRollClient = truckRollClient;
        this.salesforceOpportunityRepository = salesforceOpportunityRepository;
    }

    /**
     * @param customerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt
     * @param date                                    date
     * @param model                                   model
     * @param httpServletRequest                      httpServletRequest
     * @return template path
     * @throws BadRequestException       when customerSelfSchedulingRequestDecodedJwt role is NOT {@link CustomerSelfSchedulingRole#CUSTOMER}
     *                                   when customerSelfSchedulingRequestDecodedJwt does not contain a valid opportunity id and client id
     * @throws ResourceNotFoundException when {@link CustomerInformation} is not found
     */
    @Override
    public String dateTimeSelection(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt, LocalDate date, Model model, HttpServletRequest httpServletRequest) {

        Objects.requireNonNull(customerSelfSchedulingRequestDecodedJwt);
        Objects.requireNonNull(model);
        Objects.requireNonNull(httpServletRequest);

        validate(customerSelfSchedulingRequestDecodedJwt);

        Set<Long> skillIds = new HashSet<>(customerSelfSchedulingRequestDecodedJwt.getSkillIds());
        String opportunityId = customerSelfSchedulingRequestDecodedJwt.getOpportunityId();
        Long duration = customerSelfSchedulingRequestDecodedJwt.getDuration();

        SalesforceOpportunity salesforceOpportunity = this.salesforceOpportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity Not Found!"));

        CustomerInformation customerInformation = getCustomerInformation(opportunityId);
        String operationalArea = customerInformation.getOperationalArea();
        Set<PersonnelDto> personnel = getAvailablePersonnel(skillIds, operationalArea);
        String requestName = generateNameFrom(skillIds, customerInformation);
        List<EventDetail> existingActiveAppointments = getExistingActiveAppointments(skillIds, customerInformation);

        if (personnel.isEmpty()) {
            LOGGER.warn("No Available Personnel! request={}", customerSelfSchedulingRequestDecodedJwt);
            return "scheduling/public-no-available-time-slots";
        }

        if (!existingActiveAppointments.isEmpty() || salesforceOpportunity.getPsaDateTime() != null) {
            return "scheduling/public-appointment-already-booked";
        }

        Appointment appointment = new Appointment(duration, customerInformation.getAddress());
        PublicDateTimeSelectionDto request = new PublicDateTimeSelectionDto();

        request.setDuration(duration.intValue());
        request.setSkillIds(skillIds);
        request.setOpportunityId(opportunityId);
        request.setToken(customerSelfSchedulingRequestDecodedJwt.getToken());
        request.setName(requestName);

        String title = getTitleFrom(httpServletRequest, customerInformation);
        String subTitle = getSubtitleFrom(httpServletRequest);

        addAttributeValueToModel(model,
                new NameValuePair<>("allowed_to_scheduled_on_behalf_of_customer", true),
                new NameValuePair<>("include_questions", false),
                new NameValuePair<>("include_disclaimer", false),
                new NameValuePair<>("availability_title", title),
                new NameValuePair<>("availability_subtitle", subTitle),
                new NameValuePair<>("request", request)
        );

        addAttributeAsJSONToModel(model, new ObjectMapper(),
                new NameValuePair<>("resources", personnel),
                new NameValuePair<>("appointment", appointment)
        );

        return "scheduling/public-date-time-selection-via-calendar";

    }

    private List<EventDetail> getExistingActiveAppointments(Set<Long> skillIds, CustomerInformation customerInformation) {

        Predicate<EventDetail> activeAppointmentPredicate = new ActiveAppointmentPredicate(
                ZonedDateTime.now(), skillIds);

        return this.truckRollClient.events()
                .getByCustomerAccountId(customerInformation.getAccountId())
                .stream()
                .filter(activeAppointmentPredicate)
                .collect(Collectors.toList());


    }

    private static void validate(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt) {

        CustomerSelfSchedulingRole role = customerSelfSchedulingRequestDecodedJwt.getRole();
        List<Long> skillIds = customerSelfSchedulingRequestDecodedJwt.getSkillIds();
        String opportunityId = customerSelfSchedulingRequestDecodedJwt.getOpportunityId();
        Long duration = customerSelfSchedulingRequestDecodedJwt.getDuration();

        if (!CustomerSelfSchedulingRole.CUSTOMER.equals(role)) {
            throw new BadRequestException(String.format("Role of %s is not allowed to view calendar", role));
        }

        if (skillIds == null || skillIds.isEmpty()) {
            throw new BadRequestException("Expected non empty list of skill ids!");
        }

        if (!IsValidSalesforceId.getInstance().test(opportunityId)) {
            throw new BadRequestException("Invalid opportunity id!");
        }

        if (null == duration) {
            throw new BadRequestException("Missing duration!");
        }

    }

    private CustomerInformation getCustomerInformation(String opportunityId) {
        return this.truckRollClient.customers()
                .getByCustomerReferenceId(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Information Not Found!"));
    }

    private Set<PersonnelDto> getAvailablePersonnel(Set<Long> skillIds, String operationalArea) {
        return this.truckRollClient.calendars().getAll()
                .stream()
                .filter(new HasAllSkills(skillIds).and(new ServesOperationalArea(operationalArea)))
                .map(PersonnelDto::from)
                .collect(Collectors.toSet());
    }

    private String generateNameFrom(Set<Long> skillIds, CustomerInformation customerInformation) {
        Set<SkillDetail> skillDetails = this.truckRollClient
                .skills()
                .getAll()
                .stream()
                .filter(skill -> skillIds.contains(skill.getId()))
                .collect(Collectors.toSet());

        String skillNamesJoined = skillDetails.stream().map(SkillDetail::getName).collect(Collectors.joining(","));
        return String.format("%s - %s %s", skillNamesJoined, customerInformation.getCustomerFirstName(), customerInformation.getCustomerLastName());
    }

    private static String getTitleFrom(HttpServletRequest httpServletRequest, CustomerInformation opportunity) {
        String title = httpServletRequest.getParameter("title");
        String defaultTitle = String.format("Scheduling for %s %s", opportunity.getCustomerFirstName(), opportunity.getCustomerLastName());
        return StringUtils.isBlank(title) ? defaultTitle : title;
    }

    private String getSubtitleFrom(HttpServletRequest httpServletRequest) {
        String subTitle = httpServletRequest.getParameter("subtitle");
        String defaultSubtitle = "Please select a date and time that is most convenient for you";
        return StringUtils.isBlank(subTitle) ? defaultSubtitle : subTitle;
    }

    @SafeVarargs
    private static void addAttributeAsJSONToModel(Model model, ObjectMapper objectMapper, NameValuePair<String, Object>... dtos) {
        for (NameValuePair<String, Object> dto : dtos) {
            try {
                model.addAttribute(dto.getKey(), objectMapper.writeValueAsString(dto.getValue()));
            } catch (JsonProcessingException e) {
                throw new InternalServerErrorException(e.getMessage(), e);
            }

        }
    }

    @SafeVarargs
    private static void addAttributeValueToModel(Model model, NameValuePair<String, Object>... dtos) {
        for (NameValuePair<String, Object> dto : dtos) {
            model.addAttribute(dto.getKey(), dto.getValue());
        }
    }

    private static final class HasAllSkills implements Predicate<CalendarDetail> {

        private Set<Long> requiredSkills;

        private HasAllSkills(Set<Long> skillIds) {
            this.requiredSkills = Collections.unmodifiableSet(skillIds);
        }

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param calendarDetail the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        @Override
        public boolean test(CalendarDetail calendarDetail) {

            Set<Long> skillSet = calendarDetail.getSkills()
                    .stream()
                    .map(SkillDetail::getId)
                    .collect(Collectors.toSet());

            return skillSet.containsAll(requiredSkills);
        }
    }

    private static final class ServesOperationalArea implements Predicate<CalendarDetail> {

        private final String operationalArea;

        private ServesOperationalArea(String operationalArea) {
            this.operationalArea = operationalArea;
        }

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param calendarDetail the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        @Override
        public boolean test(CalendarDetail calendarDetail) {
            return calendarDetail.getOperationalAreas()
                    .stream()
                    .map(OperationalAreaDetail::getName)
                    .anyMatch(areaServed -> StringUtils.equalsIgnoreCase(areaServed, this.operationalArea));
        }
    }

}