package com.my1stle.customer.portal.service.task;


import java.time.ZonedDateTime;

public interface TaskSpecification {

    String getOwner();

    String getPriority();

    String getStatus();

    String getSubject();

    String getDescription();

    String getPhone();

    String getEmail();

    String getRelatedTo();

    Boolean getIsReminderOn();

    ZonedDateTime getReminderDateTime();

}