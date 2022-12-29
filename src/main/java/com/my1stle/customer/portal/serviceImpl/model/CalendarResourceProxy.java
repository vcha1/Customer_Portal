package com.my1stle.customer.portal.serviceImpl.model;

import com.my1stle.customer.portal.service.model.CalendarResource;
import com.dev1stle.scheduling.system.v1.model.Calendar;


public class CalendarResourceProxy implements CalendarResource {

    private Long id ;

    public CalendarResourceProxy(Calendar calendar) {
        this.id = calendar.getId() ;
    }
    public CalendarResourceProxy(Long id)
    {
        this.id = id ;
    }

    @Override
    public Long getId() {
        return id;
    }
}
