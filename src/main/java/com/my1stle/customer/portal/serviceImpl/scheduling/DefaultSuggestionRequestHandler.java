package com.my1stle.customer.portal.serviceImpl.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.routing.NewEvent;
import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import com.dev1stle.scheduling.system.v1.service.assignee.AssigneeService;
import com.dev1stle.scheduling.system.v1.service.suggestion.provider.ISuggestionProvider;
import com.my1stle.customer.portal.service.scheduling.SuggestionRequestHandler;
import com.my1stle.customer.portal.service.util.SuggestionRequestUtil;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class DefaultSuggestionRequestHandler implements SuggestionRequestHandler {

    private final AssigneeService assigneeService;
    private final ISuggestionProvider suggestionProvider;

    @Autowired
    public DefaultSuggestionRequestHandler(AssigneeService assigneeService, ISuggestionProvider suggestionProvider) {
        this.assigneeService = assigneeService;
        this.suggestionProvider = suggestionProvider;
    }

    /**
     * @param request
     * @return
     * @implNote note on temp work around. This is to prevent the using instances of {@link Calendar} via de-serializations.
     * refetches entities.
     */
    @Override
    public List<Suggestion> provideSuggestion(SuggestionRequest request) {

        List<LocalDate> dates = SuggestionRequestUtil.getValidDatesToCheck(request);

        // Temp Work around. See @implNote
        List<Long> ids = request.getResources().stream().map(Calendar::getId).collect(Collectors.toList());
        List<Calendar> calendars = this.assigneeService.findById(ids);

        NewEvent appointment = new NewEvent();
        appointment.location = request.getAppointment().getAddress();
        appointment.duration = request.getAppointment().getDuration().intValue();

        com.dev1stle.scheduling.system.v1.suggestion.request.SuggestionRequest suggestionsRequest = new com.dev1stle.scheduling.system.v1.suggestion.request.SuggestionRequest(
                calendars,
                dates,
                appointment,
                request.getBlockDuration().intValue(),
                request.getTolerance().intValue()
        );

        List<Suggestion> suggestionList = this.suggestionProvider.getSuggestion(suggestionsRequest);
        suggestionList.forEach(suggestion -> suggestion.setStart(suggestion.getStart().withZoneSameInstant(request.getTimezone())));
        return suggestionList;

    }


}