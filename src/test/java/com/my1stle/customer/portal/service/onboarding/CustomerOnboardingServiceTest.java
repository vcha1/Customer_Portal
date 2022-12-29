package com.my1stle.customer.portal.service.onboarding;

import com.my1stle.customer.portal.service.installation.InstallationSelector;
import com.my1stle.customer.portal.service.model.OnboardingQuestion;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link CustomerOnboardingService}
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerOnboardingServiceTest {

    private OnboardingService onboardingService;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private InstallationSelector mockInstallationSelector;

    private ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
            .build()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @Before
    public void setUp() throws Exception {

        this.onboardingService = new CustomerOnboardingService(mockUserRepository, objectMapper, mockInstallationSelector);

    }

    @Test
    public void userShouldBeRequiredToCompleteOnboarding() {

        // Given
        User user = new User();
        user.setEmail("foobar@1stle.com");

        // Stubbing
        when(mockInstallationSelector.selectByCustomerEmailAndContractApprovedDateIsBefore(eq(user.getEmail()), eq(CustomerOnboardingService.CUT_OFF_DATE)))
                .thenReturn(Collections.emptyList());

        // When
        Boolean requiredToCompleteOnboarding = this.onboardingService.isRequiredToCompleteOnboarding(user);

        // Then
        verify(mockInstallationSelector, times(1))
                .selectByCustomerEmailAndContractApprovedDateIsBefore(eq(user.getEmail()), eq(CustomerOnboardingService.CUT_OFF_DATE));
        assertTrue(requiredToCompleteOnboarding);

    }

    @Test
    public void shouldHaveCompletedOnboarding() {

        // Given
        User user = new User();
        user.setCompleteOnboardingFormDateTime(ZonedDateTime.now());

        // When
        Boolean hasCompletedOnboarding = this.onboardingService.hasCompletedOnboarding(user);

        // Then
        assertTrue(hasCompletedOnboarding);

    }

    @Test
    public void shouldHaveCompleteOnboardingVideo() {

        // Given
        User user = new User();
        ZonedDateTime now = ZonedDateTime.now();

        // When
        this.onboardingService.completeOnboardingVideo(user, now);

        // Then
        verify(mockUserRepository, times(1)).save(any(User.class));
        assertEquals(now, user.getOnboarding().getCompletedOnboardingVideoDateTime());

    }

    @Test
    public void shouldCompleteOnboardingForm() {

        // Given
        User user = new User();
        ZonedDateTime now = ZonedDateTime.now();

        // When
        this.onboardingService.completeOnboardingForm(user, now);

        // Then
        verify(mockUserRepository, times(1)).save(any(User.class));
        assertEquals(now, user.getOnboarding().getCompleteOnboardingFormDateTime());

    }

    @Test
    public void shouldGetOnboardingQuestions() throws IOException {

        // Given
        List<OnboardingQuestion> expectedQuestions = objectMapper.readValue(ResourceUtils
                        .getFile("classpath:onboarding/customer_onboarding_questions.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, CustomerOnboardingService.CustomerOnboardingQuestion.class));

        // When
        List<OnboardingQuestion> actualQuestions = this.onboardingService.getOnboardingQuestions();

        // Then
        assertEquals(expectedQuestions, actualQuestions);


    }

}