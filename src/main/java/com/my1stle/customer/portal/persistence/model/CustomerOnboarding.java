package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.Onboarding;
import org.baeldung.persistence.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "onboarding")
public class CustomerOnboarding implements Onboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onboarding_id")
    private Long id;

    @Column(name = "created_date_time", nullable = false)
    private ZonedDateTime createdDateTime;

    @Column(name = "last_modified_date_time", nullable = false)
    private ZonedDateTime lastModifiedDateTime;

    @OneToOne(mappedBy = "onboarding")
    private User user;

    @Column(name = "completed_onboarding_video_date_time")
    private ZonedDateTime completedOnboardingVideoDateTime;

    @Column(name = "completed_onboarding_form_date_time")
    private ZonedDateTime completeOnboardingFormDateTime;

    @Column(name = "provided_identification_date_time")
    private ZonedDateTime providedIdentificationDateTime;

    public CustomerOnboarding() {

    }

    @PrePersist
    public void prePresist() {

        if (null == createdDateTime)
            createdDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        if (null == lastModifiedDateTime)
            lastModifiedDateTime = ZonedDateTime.now(ZoneOffset.UTC);

    }

    @PreUpdate
    public void preUpdate() {

        if (null == lastModifiedDateTime)
            lastModifiedDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        if(null != completedOnboardingVideoDateTime) {
            completedOnboardingVideoDateTime = completedOnboardingVideoDateTime.withZoneSameInstant(ZoneOffset.UTC);
        }

        if(null != completeOnboardingFormDateTime) {
            completeOnboardingFormDateTime = completeOnboardingFormDateTime.withZoneSameInstant(ZoneOffset.UTC);
        }

    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public ZonedDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    @Override
    public ZonedDateTime getCompletedOnboardingVideoDateTime() {
        return completedOnboardingVideoDateTime;
    }

    @Override
    public ZonedDateTime getCompleteOnboardingFormDateTime() {
        return completeOnboardingFormDateTime;
    }

    @Override
    public ZonedDateTime getProvidedIdentificationDateTime() {
        return providedIdentificationDateTime;
    }

    public void setCompletedOnboardingVideoDateTime(ZonedDateTime completedOnboardingVideoDateTime) {
        this.completedOnboardingVideoDateTime = completedOnboardingVideoDateTime;
    }

    public void setCompleteOnboardingFormDateTime(ZonedDateTime completeOnboardingFormDateTime) {
        this.completeOnboardingFormDateTime = completeOnboardingFormDateTime;
    }

    public void setProvidedIdentificationDateTime(ZonedDateTime providedIdentificationDateTime) {
        this.providedIdentificationDateTime = providedIdentificationDateTime;
    }
}
