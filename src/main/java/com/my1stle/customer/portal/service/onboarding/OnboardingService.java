package com.my1stle.customer.portal.service.onboarding;

import com.my1stle.customer.portal.service.model.OnboardingQuestion;
import org.baeldung.persistence.model.User;

import java.time.ZonedDateTime;
import java.util.List;

public interface OnboardingService {


    /**
     * Checks whether the user is required to complete the onboarding process
     * @param user
     * @return
     */
    Boolean isRequiredToCompleteOnboarding(User user);

    /**
     * Check if user has completed the on boarding process
     * @param user
     * @return Boolean of with the user has completed the onboarding process
     */
    Boolean hasCompletedOnboarding(User user);

    /**
     * Timestamp when the user has watch the onboarding video
     * @param user
     * @param completedDateTime
     */
    void completeOnboardingVideo(User user, ZonedDateTime completedDateTime);

    /**
     * Timestamp when the user has completed the onboarding form
     * @param user
     * @param completedDateTime
     */
    void completeOnboardingForm(User user, ZonedDateTime completedDateTime);

    /**
     *
     * @return list of onboarding questions
     */
    List<OnboardingQuestion> getOnboardingQuestions();

}
