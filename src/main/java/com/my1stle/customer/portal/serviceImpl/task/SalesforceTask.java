package com.my1stle.customer.portal.serviceImpl.task;

import com.my1stle.customer.portal.service.model.Task;
import com.dev1stle.repository.salesforce.model.SObjectApiName;
import com.dev1stle.repository.salesforce.model.SObjectField;
import com.dev1stle.repository.salesforce.model.SalesforceObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.ZonedDateTime;

/**
 * Regarding email and phone being read only
 *
 * @see <a href="https://developer.salesforce.com/forums/?id=906F0000000AlTOIA0">Email & Phone fields on Task Object</a>
 */
@SObjectApiName(value = "Task")
public final class SalesforceTask extends SalesforceObject implements Task {

    @SObjectField(api_name = "OwnerId")
    private String ownerId;

    @SObjectField(api_name = "Priority")
    private String priority;

    @SObjectField(api_name = "Status")
    private String status;

    @SObjectField(api_name = "Subject")
    private String subject;

    @SObjectField(api_name = "Description")
    private String description;

    @SObjectField(api_name = "Phone", read_only = true)
    private String phone;

    @SObjectField(api_name = "Email", read_only = true)
    private String email;

    @SObjectField(api_name = "WhatId")
    private String relatedTo;

    @SObjectField(api_name = "IsReminderSet")
    private Boolean isReminderOn;

    @SObjectField(api_name = "ReminderDateTime")
    private ZonedDateTime reminderDateTime;

    private SalesforceTask() {

    }

    /**
     * @param taskSpecification
     * @return
     * @implNote Does not set email and phone
     * @see <a href="https://developer.salesforce.com/forums/?id=906F0000000AlTOIA0">Email & Phone fields on Task Object</a>
     */
    static SalesforceTask from(SalesforceTaskSpecification taskSpecification) {

        SalesforceTask task = new SalesforceTask();
        task.ownerId = taskSpecification.getOwner();
        task.priority = taskSpecification.getPriority();
        task.status = taskSpecification.getStatus();
        task.subject = taskSpecification.getSubject();
        task.description = taskSpecification.getDescription();
        //task.email = taskSpecification.getEmail();
        //task.phone = taskSpecification.getPhone();
        task.relatedTo = taskSpecification.getRelatedTo();
        task.isReminderOn = taskSpecification.getIsReminderOn();
        task.reminderDateTime = taskSpecification.getReminderDateTime();

        return task;

    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String getOwner() {
        return getOwnerId();
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
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof SalesforceTask)) return false;

        SalesforceTask that = (SalesforceTask) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
                .append(getId())
                .toHashCode();

    }

}