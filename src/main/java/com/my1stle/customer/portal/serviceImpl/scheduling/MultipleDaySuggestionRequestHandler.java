package com.my1stle.customer.portal.serviceImpl.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import com.dev1stle.scheduling.system.v1.model.suggestion.SuggestionCode;
import com.dev1stle.scheduling.system.v1.service.availability.AvailabilityService;
import com.dev1stle.scheduling.system.v1.service.request.AvailabilityRequest;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.CalendarDetail;
import com.dev1stle.scheduling.system.v1.suggestion.strategy.MergeSuggestionStrategy;
import com.my1stle.customer.portal.service.scheduling.SuggestionRequestHandler;
import com.my1stle.customer.portal.service.util.SuggestionRequestUtil;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MultipleDaySuggestionRequestHandler implements SuggestionRequestHandler {

    private AvailabilityService availabilityService;
    private MergeSuggestionStrategy mergeSuggestionStrategy;

    @Value("${truckroll_system.number.of.minutes.in.work.day}")
    private int minutesInAWorkDay;
    private static final LocalTime EARLIEST_LOCAL_TIME = LocalTime.of(8, 0); // 8 A.M

    @Autowired
    public MultipleDaySuggestionRequestHandler(
            AvailabilityService availabilityService,
            MergeSuggestionStrategy mergeSuggestionStrategy) {

        this.availabilityService = availabilityService;
        this.mergeSuggestionStrategy = mergeSuggestionStrategy;

    }

    @Override
    public List<Suggestion> provideSuggestion(SuggestionRequest request) {

        List<LocalDate> dates = SuggestionRequestUtil.getValidDatesToCheck(request);

        if (dates.isEmpty()) {
            return Collections.emptyList();
        }

        ZoneId timezone = request.getTimezone();

        Set<Calendar> resources = new HashSet<>(request.getResources());

        LocalDate start = dates.stream()
                .min(Comparator.comparing(LocalDate::toEpochDay))
                .get();

        LocalDate end = dates.stream()
                .max(Comparator.comparing(LocalDate::toEpochDay))
                .get();

        long duration = request.getAppointment().getDuration();

        AvailabilityRequest availabilityRequest = new AvailabilityRequest(
                resources, start, end, duration, minutesInAWorkDay
        );

        List<Suggestion> suggestions = new ArrayList<>();

        Map<LocalDate, Set<CalendarDetail>> availability = availabilityService.getAvailability(availabilityRequest);

        for (LocalDate date : availability.keySet()) {

            Set<CalendarDetail> calendarDetails = availability.get(date);

            if (calendarDetails.isEmpty()) {
                continue;
            }

            for (CalendarDetail calendarDetail : calendarDetails) {
                Suggestion suggestion = new Suggestion();
                suggestion.setStart(date.atTime(EARLIEST_LOCAL_TIME).atZone(timezone));
                suggestion.setNote("Available");
                suggestion.setSuggestionCode(SuggestionCode.SERVICEABLE.getCode());
                suggestion.setResource(String.valueOf(calendarDetail.getId()));
                suggestion.setEstimatedDriving(0);
                suggestion.setNumOfExistingEvents(0); // temporary fix until the merge suggestion strategy is fixed
                suggestions.add(suggestion);
            }

        }

        return mergeSuggestionStrategy.merge(suggestions);

    }

}
