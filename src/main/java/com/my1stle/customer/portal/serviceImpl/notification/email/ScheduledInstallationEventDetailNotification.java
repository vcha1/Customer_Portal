package com.my1stle.customer.portal.serviceImpl.notification.email;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
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

@Service
class ScheduledInstallationEventDetailNotification extends AbstractScheduledEmailEventDetailNotification<Installation> {

    private final InstallationService publicInstallationService;

    private static final String SUBJECTE_TEMPLATE = "%s Your Scheduled Installation Date with 1st Light Energy";
    private static final String TEMPLATE_PATH = "email/customer-scheduled-installation-notification";

    private final Map<Long, String> durationLabels = new HashMap<>();

    @Autowired
    ScheduledInstallationEventDetailNotification(
            TemplateEngine templateEngine,
            ResourceLoader resourceLoader,
            InstallationService publicInstallationService,
            TimeZoneService timeZoneService) {
        super(templateEngine, resourceLoader, timeZoneService);
        this.publicInstallationService = publicInstallationService;

    }

    @Override
    Installation getContext(EventDetail eventDetail) {
        String installationId = eventDetail.getExternalId();
        Installation installationById = this.publicInstallationService.getInstallationById(installationId);
        if (installationById == null) {
            throw new IllegalStateException(String.format("Installation [id: %s] not found!", installationId));
        }
        return installationById;
    }

    @Override
    String subject(EventDetail eventDetail, Installation context) {
        return String.format(SUBJECTE_TEMPLATE, context.getCustomerName());
    }

    @Override
    List<String> recipients(EventDetail eventDetail, Installation context) {
        return Collections.singletonList(context.getCustomerEmail());
    }

    @Override
    List<String> bccs(EventDetail eventDetail, Installation context) {

        String divisionEmail = DivisionEmail.getDivisionEmail(context.getOperationalArea());
        String salesRepEmail = context.getDirectSalesCloserEmail();

        List<String> bccs = new ArrayList<>();

        bccs.add(Email.SALESFORCE_ACTIVITY_HISTORY_EMAIL);

        if (!StringUtils.isBlank(divisionEmail)) {
            bccs.add(divisionEmail);
        }

        if (!StringUtils.isBlank(salesRepEmail)) {
            bccs.add(salesRepEmail);
        }

        return bccs;

    }

    @Override
    String templatePath(EventDetail eventDetail, Installation context) {
        return TEMPLATE_PATH;
    }

    @Override
    Map<String, Object> additionalContextVariables(EventDetail eventDetail, Installation context) {
        return Collections.singletonMap("installation", context);
    }
}