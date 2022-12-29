package com.my1stle.customer.portal.web.dto.scheduling;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;

import java.time.ZonedDateTime;

/**
 * Represents which data will be available to the client
 */
public class AnonymizeEventDetailDto {

    private Long id;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    private AnonymizeEventDetailDto() {

    }

    public static AnonymizeEventDetailDto from(EventDetail eventDetail) {

        AnonymizeEventDetailDto dto = new AnonymizeEventDetailDto();

        dto.id = eventDetail.getId();
        dto.startDateTime = eventDetail.getStartDateTime();
        dto.endDateTime = eventDetail.getEndDateTime();

        return dto;

    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

}
