package com.my1stle.customer.portal.service.model;

import java.util.List;

public interface OnboardingQuestion {

    String getQuestion();

    List<String> getOptions();

    Integer getCorrectOption();

    String getCorrectionResponse();

    String getIncorrectResponse();

}