package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

class CurrentUserOwnsExistingServiceCase implements Predicate<ExistingServiceCaseDto> {

    private final InstallationService installationService;

    CurrentUserOwnsExistingServiceCase(InstallationService installationService) {
        this.installationService = installationService;
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param existingServiceCaseDto the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override
    public boolean test(ExistingServiceCaseDto existingServiceCaseDto) {
        return this.installationService.getInstallations()
                .stream()
                .map(Installation::getId)
                .anyMatch(installationId -> StringUtils.equalsIgnoreCase(existingServiceCaseDto.getExternalId(), installationId));
    }
}
