package com.my1stle.customer.portal.serviceImpl.notification.email;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.SkillDetail;
import com.my1stle.customer.portal.persistence.model.SalesforceOpportunity;
import com.my1stle.customer.portal.persistence.repository.SalesforceOpportunityRepository;
import com.my1stle.customer.portal.service.time.zone.TimeZoneService;
import com.my1stle.customer.portal.service.util.DivisionEmail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduledOpportunityEventDetailNotification extends AbstractScheduledEmailEventDetailNotification<SalesforceOpportunity> {

    private final SalesforceOpportunityRepository salesforceOpportunityRepository;

    private static final String SUBJECT_TEMPLATE = "%s Your Scheduled Appointment Date with 1st Light Energy";
    private static final String TEMPLATE_PATH = "email/customer-scheduled-event-detail-notification";

    @Autowired
    ScheduledOpportunityEventDetailNotification(TemplateEngine templateEngine, ResourceLoader resourceLoader, TimeZoneService timeZoneService, SalesforceOpportunityRepository salesforceOpportunityRepository) {
        super(templateEngine, resourceLoader, timeZoneService);
        this.salesforceOpportunityRepository = salesforceOpportunityRepository;
    }

    @Override
    SalesforceOpportunity getContext(EventDetail eventDetail) {

        String opportunityId = eventDetail.getExternalId();

        return this.salesforceOpportunityRepository
                .findById(opportunityId)
                .orElseThrow(() -> new IllegalStateException(String.format("Salesforce Opportunity [id:%s] not found!", opportunityId)));

    }

    @Override
    String subject(EventDetail eventDetail, SalesforceOpportunity context) {
        return String.format(SUBJECT_TEMPLATE, context.getAccountFirstName());
    }

    @Override
    List<String> recipients(EventDetail eventDetail, SalesforceOpportunity context) {
        return Collections.singletonList(context.getEmail());
    }

    @Override
    List<String> bccs(EventDetail eventDetail, SalesforceOpportunity context) {

        String divisionEmail = DivisionEmail.getDivisionEmail(context.getOperationalArea());
        String salesRepEmail = context.getDirectSalesCloserAccountEmail();

        List<String> bccs = new ArrayList<>();

        if (!StringUtils.isBlank(divisionEmail)) {
            bccs.add(divisionEmail);
        }

        if (!StringUtils.isBlank(salesRepEmail)) {
            bccs.add(salesRepEmail);
        }

        bccs.add(Email.SALESFORCE_ACTIVITY_HISTORY_EMAIL);

        return bccs;
    }

    @Override
    String templatePath(EventDetail eventDetail, SalesforceOpportunity context) {
        return TEMPLATE_PATH;
    }

    @Override
    Map<String, Object> additionalContextVariables(EventDetail eventDetail, SalesforceOpportunity context) {

        Map<String, Object> additionalContextVariables = new HashMap<>();
        additionalContextVariables.put("customerName", context.getAccountFirstName());
        additionalContextVariables.put("salesforceId", context.getId());

        String reason = eventDetail.getReason();

        String skillNames = eventDetail.getSkills()
                .stream()
                .map(SkillDetail::getName)
                .collect(Collectors.joining(", "));

        additionalContextVariables.put("reason", StringUtils.isBlank(reason) ? skillNames : reason);

        return additionalContextVariables;

    }

}