package com.my1stle.customer.portal.application.event;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import org.springframework.context.ApplicationEvent;

public class OnScheduledEventDetail extends ApplicationEvent {

    private final EventDetail eventDetail;

    public OnScheduledEventDetail(Object source, EventDetail eventDetail) {
        super(source);
        this.eventDetail = eventDetail;
    }

    public EventDetail getEventDetail() {
        return eventDetail;
    }

}
