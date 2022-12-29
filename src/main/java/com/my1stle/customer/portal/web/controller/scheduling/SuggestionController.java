package com.my1stle.customer.portal.web.controller.scheduling;

import com.my1stle.customer.portal.service.scheduling.SuggestionRequestHandler;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SuggestionController {

    private SuggestionRequestHandler suggestionRequestHandler;

    @Autowired
    public SuggestionController(SuggestionRequestHandler ISuggestionRequestHandler) {
        this.suggestionRequestHandler = ISuggestionRequestHandler;
    }

    @PostMapping(value = "/truck-roll/suggestion")
    public List<Suggestion> provideSuggestion(@RequestBody SuggestionRequest request) {
        return suggestionRequestHandler.provideSuggestion(request);
    }

}