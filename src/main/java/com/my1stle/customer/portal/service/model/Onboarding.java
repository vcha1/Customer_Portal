package com.my1stle.customer.portal.service.model;

import java.time.ZonedDateTime;

public interface Onboarding {

    ZonedDateTime getCompletedOnboardingVideoDateTime();

    ZonedDateTime getCompleteOnboardingFormDateTime();

    ZonedDateTime getProvidedIdentificationDateTime();

}
