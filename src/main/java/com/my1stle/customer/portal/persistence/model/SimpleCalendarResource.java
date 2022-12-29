package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.CalendarResource;

class SimpleCalendarResource implements CalendarResource
{
    private Long id ;

    public SimpleCalendarResource(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
