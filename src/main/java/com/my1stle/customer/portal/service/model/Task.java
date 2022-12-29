package com.my1stle.customer.portal.service.model;

import java.time.ZonedDateTime;

public interface Task {

    String getId();

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