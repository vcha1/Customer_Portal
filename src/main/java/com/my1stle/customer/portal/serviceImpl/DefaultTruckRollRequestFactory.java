package com.my1stle.customer.portal.serviceImpl;

import com.dev1stle.scheduling.system.v1.model.Skill;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.CreateNewTruckRollDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollDetails;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.TruckRollBasicInformation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.TruckRollOptionalInformation;
import com.my1stle.customer.portal.service.TruckRollRequestFactory;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.scheduling.CustomerSelfSchedulingRole;
import com.my1stle.customer.portal.serviceImpl.scheduling.Scheduling;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class DefaultTruckRollRequestFactory implements TruckRollRequestFactory {

    static final String CUSTOMER_PORTAL_ACCOUNT = "0010g00001gzd96AAA";

    @Value("${truckroll_system.number.of.minutes.in.work.day}")
    private int minutesInAWorkDay;

    private Converter<String, CustomerSelfSchedulingRequestDecodedJwt> customerSelfSchedulingRequestDecodedJwtConverter;

    @Autowired
    public DefaultTruckRollRequestFactory(
            Converter<String, CustomerSelfSchedulingRequestDecodedJwt> customerSelfSchedulingRequestDecodedJwtConverter) {
        this.customerSelfSchedulingRequestDecodedJwtConverter = customerSelfSchedulingRequestDecodedJwtConverter;
    }

    /**
     * @param serviceRequest serviceRequest representing user's requested service
     * @return list of truck roll details
     * @implNote sets the request by to "Customer Portal" account in 1st Light Energy's Salesforce Org.
     * @see <a href="https://1stlight.my.salesforce.com/0010g00001gzd96">Customer Portal Account</a>
     */
    @Override
    public List<NewTruckRollDetails> from(ServiceRequest serviceRequest) {

        Product product = serviceRequest.getProduct();
        //Installation customerInstallation = serviceRequest.getInstallation();
        OdooInstallationData customerInstallation = serviceRequest.getOdooInstallationData();

        String customerReferenceId = customerInstallation.getId().toString();

        String name = String.format("%s for %s", product.getName(), customerInstallation.getName());
        Integer duration = product.getDuration();
        Set<Long> skillIds = product.getSkills().stream().map(Skill::getId).collect(Collectors.toSet());
        Set<Long> resourceIds = Collections.singleton(serviceRequest.getResource().getId());
        ZonedDateTime startDateTime = serviceRequest.getStartTime();

        TruckRollBasicInformation basicInformation = new TruckRollBasicInformation(
                name,
                duration,
                false, // needs confirmation
                skillIds,
                true, // fixed appointment
                CUSTOMER_PORTAL_ACCOUNT // requested by
        );

        TruckRollOptionalInformation optionalInformation = new TruckRollOptionalInformation()
                .withResourceIds(resourceIds)
                .withStartDateTime(startDateTime)
                .withAppointmentNotes(serviceRequest.getCustomerNotes());

        CreateNewTruckRollDetail truckRollDetail = new CreateNewTruckRollDetail(customerReferenceId, basicInformation)
                .withOptionalInformation(optionalInformation);

        return Collections.singletonList(truckRollDetail);

    }

    @Override
    public List<NewTruckRollDetails> from(PublicDateTimeSelectionDto dateTimeSelectionDto) {

        Objects.requireNonNull(dateTimeSelectionDto);
        CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt = this.customerSelfSchedulingRequestDecodedJwtConverter.convert(dateTimeSelectionDto.getToken());
        Objects.requireNonNull(customerSelfSchedulingRequestDecodedJwt);

        String installationId = dateTimeSelectionDto.getInstallationId();
        String opportunityId = dateTimeSelectionDto.getOpportunityId();
        String name = dateTimeSelectionDto.getName();
        Integer duration = dateTimeSelectionDto.getDuration();

        Set<Long> skillIds = dateTimeSelectionDto.getSkillIds();
        Set<Long> resourceIds = Collections.singleton(dateTimeSelectionDto.getCalendarId());
        ZonedDateTime startDateTime = dateTimeSelectionDto.getStartDateTime();
        String notes = dateTimeSelectionDto.getNotes();

        String customerReferenceId = !StringUtils.isBlank(installationId) ? installationId : opportunityId;

        List<NewTruckRollDetails> details = new ArrayList<>();

        int numberOfFullWorkDays = duration / minutesInAWorkDay;

        for (int i = 0; i < numberOfFullWorkDays; i++) {
            details.add(createNewTruckRollDetail(customerReferenceId, name, skillIds, resourceIds, notes, startDateTime, minutesInAWorkDay, i, customerSelfSchedulingRequestDecodedJwt));
        }

        int remainingDuration = duration % minutesInAWorkDay;

        if (remainingDuration > 0) {
            details.add(createNewTruckRollDetail(customerReferenceId, name, skillIds, resourceIds, notes, startDateTime, remainingDuration, numberOfFullWorkDays, customerSelfSchedulingRequestDecodedJwt));
        }

        return details;

    }

    private static CreateNewTruckRollDetail createNewTruckRollDetail(String customerReferenceId, String name, Set<Long> skillIds, Set<Long> resourceIds, String notes, ZonedDateTime startDateTime, int duration, int offset, CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt) {

        CustomerSelfSchedulingRole role = customerSelfSchedulingRequestDecodedJwt.getRole() == null ? CustomerSelfSchedulingRole.CUSTOMER : customerSelfSchedulingRequestDecodedJwt.getRole();

        return new CreateNewTruckRollDetail(
                customerReferenceId,
                new TruckRollBasicInformation(
                        name,
                        duration,
                        false, // needs confirmation
                        skillIds,
                        true, // fixed appointment
                        CustomerSelfSchedulingRole.CUSTOMER.equals(role) ? CUSTOMER_PORTAL_ACCOUNT : customerSelfSchedulingRequestDecodedJwt.getClientId()// requested by
                )

        ).withOptionalInformation(
                new TruckRollOptionalInformation()
                        .withResourceIds(resourceIds)
                        .withStartDateTime(startDateTime.plusDays(offset))
                        .withReason(generateReasonFrom(skillIds))
                        .withAppointmentNotes(notes)
        );
    }

    DefaultTruckRollRequestFactory setMinutesInAWorkDay(int minutesInAWorkDay) {
        this.minutesInAWorkDay = minutesInAWorkDay;
        return this;
    }

    private static String generateReasonFrom(Set<Long> skillIds) {
        List<String> reasons = new ArrayList<>();
        if(skillIds.contains(Scheduling.FULL_PSA_SKILL_ID)) {
            reasons.add("Full PSA");
        }
        if(skillIds.contains(Scheduling.EXTERIOR_PSA_SKILL_ID)) {
            reasons.add("Exterior PSA");
        }
        if(skillIds.contains(Scheduling.INSTALLATION_SKILL_ID) || skillIds.contains(Scheduling.BATTERY_SKILL_ID)) {
            reasons.add("Solar System Installation");
        }
        return String.join(", ", reasons);
    }

}