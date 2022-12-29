package com.my1stle.customer.portal.web.controller.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.Event;
import com.dev1stle.scheduling.system.v1.service.suggestion.provider.ExistingAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class EventController {

    private ExistingAppointmentService service;

    @Autowired
    public EventController(ExistingAppointmentService service) {
        this.service = service;
    }

    @GetMapping(value = "/event")
    public List<Event> getTruckRolls(@RequestParam List<Calendar> resources,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam ZonedDateTime start,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam ZonedDateTime end) {

        return service.findByResourceAndActiveAppointmentsBetween(resources, start, end);

    }

}
