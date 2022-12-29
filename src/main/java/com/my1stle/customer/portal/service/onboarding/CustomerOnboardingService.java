package com.my1stle.customer.portal.service.onboarding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.my1stle.customer.portal.service.installation.InstallationSelector;
import com.my1stle.customer.portal.service.model.OnboardingQuestion;
import com.my1stle.customer.portal.service.util.LRUCache;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOnboardingService implements OnboardingService {

    private final UserRepository userRepository;
    private final ObjectMapper springObjectMapper;
    private final InstallationSelector installationSelector;

    static final LocalDate CUT_OFF_DATE = LocalDate.of(2020, 2, 19);

    private final Map<String, Boolean> userRequiredToCompleteOnboardingCache = new LRUCache<>(50);

    @Autowired
    public CustomerOnboardingService(
            UserRepository userRepository,
            ObjectMapper springObjectMapper, InstallationSelector installationSelector) {
        this.userRepository = userRepository;
        this.springObjectMapper = springObjectMapper;
        this.installationSelector = installationSelector;
    }

    /**
     * Checks whether the user is required to complete the onboarding process
     *
     * @param user
     * @return {@code true} when user has an installation that its contract signed before the {@link CustomerOnboardingService#CUT_OFF_DATE}
     * @implNote memoizes if user is required to complete onboarding
     */
    @Override
    public Boolean isRequiredToCompleteOnboarding(User user) {

        final String userEmail = user.getEmail();
        Boolean isRequired = userRequiredToCompleteOnboardingCache.get(userEmail);

        if (null == isRequired) {
            isRequired = this.installationSelector.selectByCustomerEmailAndContractApprovedDateIsBefore(userEmail, CUT_OFF_DATE).isEmpty();
            userRequiredToCompleteOnboardingCache.put(userEmail, isRequired);
        }

        return isRequired;

    }

    @Override
    public Boolean hasCompletedOnboarding(User user) {
        return user.getOnboarding().getCompleteOnboardingFormDateTime() != null;
    }

    @Override
    @Transactional
    public void completeOnboardingVideo(User user, ZonedDateTime completedDateTime) {

        user.setCompletedOnboardingVideoDateTime(completedDateTime);
        userRepository.save(user);

    }

    @Override
    @Transactional
    public void completeOnboardingForm(User user, ZonedDateTime completedDateTime) {

        user.setCompleteOnboardingFormDateTime(completedDateTime);
        userRepository.save(user);

    }

    @Override
    public List<OnboardingQuestion> getOnboardingQuestions() {

        try {

            final String onboardingQuestionJsonFilePath = "onboarding/customer_onboarding_questions.json";
            CollectionType typeFactory = springObjectMapper.getTypeFactory().constructCollectionType(List.class, CustomerOnboardingQuestion.class);
            InputStream stream = CustomerOnboardingService.class.getClassLoader().getResourceAsStream(onboardingQuestionJsonFilePath);

            return springObjectMapper.readValue(stream, typeFactory);

        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "q",
            "options",
            "correctIndex",
            "correctResponse",
            "incorrectResponse"
    })
    static class CustomerOnboardingQuestion implements OnboardingQuestion {

        @JsonProperty("q")
        private String q;
        @JsonProperty("options")
        private List<String> options = new ArrayList<String>();
        @JsonProperty("correctIndex")
        private Integer correctIndex;
        @JsonProperty("correctResponse")
        private String correctResponse;
        @JsonProperty("incorrectResponse")
        private String incorrectResponse;

        private CustomerOnboardingQuestion() {

        }

        @Override
        @JsonIgnore
        public String getQuestion() {
            return q;
        }

        @JsonProperty("options")
        public List<String> getOptions() {
            return options;
        }

        @Override
        public Integer getCorrectOption() {
            return correctIndex;
        }

        @Override
        public String getCorrectionResponse() {
            return correctResponse;
        }

        @JsonProperty("correctResponse")
        public String getCorrectResponse() {
            return correctResponse;
        }

        @JsonProperty("incorrectResponse")
        public String getIncorrectResponse() {
            return incorrectResponse;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("q", q).append("options", options).append("correctIndex", correctIndex).append("correctResponse", correctResponse).append("incorrectResponse", incorrectResponse).toString();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(q).append(correctIndex).append(correctResponse).append(incorrectResponse).append(options).toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if ((other instanceof CustomerOnboardingQuestion) == false) {
                return false;
            }
            CustomerOnboardingQuestion rhs = ((CustomerOnboardingQuestion) other);
            return new EqualsBuilder().append(q, rhs.q).append(correctIndex, rhs.correctIndex).append(correctResponse, rhs.correctResponse).append(incorrectResponse, rhs.incorrectResponse).append(options, rhs.options).isEquals();
        }

    }

}
