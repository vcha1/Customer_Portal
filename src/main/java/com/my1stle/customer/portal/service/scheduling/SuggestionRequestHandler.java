package com.my1stle.customer.portal.service.scheduling;

import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;

import java.util.List;

public interface SuggestionRequestHandler {
    List<Suggestion> provideSuggestion(SuggestionRequest request);
}
