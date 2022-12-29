package com.my1stle.customer.portal.service.util;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuggestionRequestUtil {

    private static final Integer CUT_OFF_DAY = 2;

    public static List<LocalDate> getValidDatesToCheck(SuggestionRequest request) {

        validate(request);

        ZoneId zone = request.getTimezone();

        LocalDate start = request.getStart()
                .withZoneSameInstant(zone)
                .toLocalDate();

        LocalDate end = request.getEnd()
                .withZoneSameInstant(zone)
                .toLocalDate();

        LocalDate today = ZonedDateTime.now()
                .withZoneSameInstant(zone)
                .toLocalDate();

        return Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .filter(date -> date.isEqual(today.plusDays(CUT_OFF_DAY)) || date.isAfter(today.plusDays(CUT_OFF_DAY)))
                .collect(Collectors.toList());


    }

    private static void validate(SuggestionRequest request) {

        if (null == request.getResources() || request.getResources().isEmpty()) {
            throw new BadRequestException("Expected at least one resource");
        }

        if (null == request.getStart()) {
            throw new BadRequestException("Start interval was expected");
        }

        if (null == request.getEnd()) {
            throw new BadRequestException("End interval was expected");
        }

        if (request.getEnd().isBefore(request.getEnd())) {
            throw new BadRequestException("Start must before end");
        }

        if (null == request.getAppointment()) {
            throw new BadRequestException("Appointment was expected");
        }

        if (StringUtils.isBlank(request.getAppointment().getAddress())) {
            throw new BadRequestException("Address was expected");
        }

        if (null == request.getAppointment().getDuration()) {
            throw new BadRequestException("Appointment duration was expected");
        }

        if (null == request.getBlockDuration()) {
            throw new BadRequestException("Block Duration was expected");
        }

    }


}
