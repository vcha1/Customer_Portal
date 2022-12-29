package com.my1stle.customer.portal.serviceImpl.task;

import com.my1stle.customer.portal.service.task.TaskSpecification;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZonedDateTime;

 public class SalesforceTaskSpecification implements TaskSpecification {

    // By Default are required by salesforce
    private String owner;
    private String subject;
    private String status;
    private String relatedTo;

    // Optional
    private String priority;
    private String description;
    private String phone;
    private String email;
    private Boolean isReminderOn;
    private ZonedDateTime reminderDateTime;

    private SalesforceTaskSpecification() {
        throw new AssertionError();
    }


    private SalesforceTaskSpecification(Builder builder) {

        this.owner = builder.owner;
        this.subject = builder.subject;
        this.status = builder.status;
        this.relatedTo = builder.relatedTo;

        this.priority = builder.priority;
        this.description = builder.description;
        this.phone = builder.phone;
        this.email = builder.email;
        this.isReminderOn = builder.isReminderOn;
        this.reminderDateTime = builder.reminderDateTime;

    }

    public static class Builder {

        private String owner;
        private String subject;
        private String status;
        private String relatedTo;

        private String priority;
        private String description;
        private String phone;
        private String email;
        private Boolean isReminderOn;
        private ZonedDateTime reminderDateTime;

        public Builder(String owner, String subject, String status, String relatedTo) {
            this.owner = owner;
            this.subject = subject;
            this.status = status;
            this.relatedTo = relatedTo;
        }

        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder reminder(Boolean isReminderOn) {
            this.isReminderOn = isReminderOn;
            return this;
        }

        public Builder reminderDateTime(ZonedDateTime reminderDateTime) {
            this.reminderDateTime = reminderDateTime;
            return this;
        }

        public SalesforceTaskSpecification build() {
            return new SalesforceTaskSpecification(this);
        }

    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getRelatedTo() {
        return relatedTo;
    }

    @Override
    public Boolean getIsReminderOn() {
        return isReminderOn;
    }

    @Override
    public ZonedDateTime getReminderDateTime() {
        return reminderDateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("owner", owner)
                .append("subject", subject)
                .append("status", status)
                .append("relatedTo", relatedTo)
                .append("priority", priority)
                .append("description", description)
                .append("phone", phone)
                .append("email", email)
                .toString();
    }
}
