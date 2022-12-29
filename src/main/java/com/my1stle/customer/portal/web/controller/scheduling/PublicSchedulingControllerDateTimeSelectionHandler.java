package com.my1stle.customer.portal.web.controller.scheduling;

import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

interface PublicSchedulingControllerDateTimeSelectionHandler {

    /**
     * @param token
     * @param date
     * @param model
     * @param httpServletRequest
     * @return template path
     */
    String dateTimeSelection(CustomerSelfSchedulingRequestDecodedJwt token, LocalDate date, Model model, HttpServletRequest httpServletRequest);

}