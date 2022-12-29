package com.my1stle.customer.portal.web.controller.scheduling;

import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.scheduling.CustomerSelfSchedulingRole;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleService;
import com.my1stle.customer.portal.service.scheduling.SuggestionRequestHandler;
import com.my1stle.customer.portal.service.serviceapi.*;
import com.my1stle.customer.portal.service.util.IsPSAEventDetail;
import com.my1stle.customer.portal.service.util.NameValuePair;
import com.my1stle.customer.portal.serviceImpl.serviceapi.*;
import com.my1stle.customer.portal.web.controller.PublicController;
import com.my1stle.customer.portal.web.dto.scheduling.AnonymizeEventDetailDto;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import kong.unirest.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PublicSchedulingController extends PublicController {

    private TruckRollClient truckRollClient;
    private SuggestionRequestHandler suggestionRequestHandler;
    private SelfScheduleService selfScheduleService;
    private Converter<String, CustomerSelfSchedulingRequestDecodedJwt> tokenConverter;
    private OperationsApi operationsApi;

    private Map<CustomerSelfSchedulingRole, PublicSchedulingControllerDateTimeSelectionHandler> handlers = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicSchedulingController.class);
    private static final LocalTime ALLOWED_TIME_SLOT = LocalTime.of(8, 0); // i.e 8 AM

    @Autowired
    public PublicSchedulingController(
            TruckRollClient truckRollClient,
            @Qualifier("defaultSuggestionRequestHandler") SuggestionRequestHandler suggestionRequestHandler,
            SelfScheduleService selfScheduleService,
            Converter<String, CustomerSelfSchedulingRequestDecodedJwt> tokenConverter,
            @Qualifier("publicSchedulingControllerCustomerDateTimeSelectionHandler") PublicSchedulingControllerDateTimeSelectionHandler customerHandler,
            @Qualifier("publicSchedulingControllerCalendarDateTimeSelectionHandler") PublicSchedulingControllerDateTimeSelectionHandler calendarHandler,
            OperationsApi operationsApi) {

        this.truckRollClient = truckRollClient;
        this.suggestionRequestHandler = suggestionRequestHandler;
        this.selfScheduleService = selfScheduleService;
        this.tokenConverter = tokenConverter;
        this.operationsApi = operationsApi;

        this.handlers = Collections.unmodifiableMap(initHandlers(
                new NameValuePair<>(CustomerSelfSchedulingRole.CUSTOMER, customerHandler),
                new NameValuePair<>(CustomerSelfSchedulingRole.SALES_REP, calendarHandler),
                new NameValuePair<>(CustomerSelfSchedulingRole.DEALER_ADMIN, calendarHandler)
        ));

    }

    @GetMapping(value = "/schedule")
    public String dateTimeSelection(
            @RequestParam(name = "token", required = true) CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model, HttpServletRequest httpServletRequest) {

        CustomerSelfSchedulingRole role = customerSelfSchedulingRequestDecodedJwt.getRole();
        Optional<ScheduleRequestDto> scheduleRequestDtoOptional = this.operationsApi.scheduleRequest().get(customerSelfSchedulingRequestDecodedJwt.getToken());
        if(!scheduleRequestDtoOptional.isPresent()){
            return "scheduling/public-appointment-invalid";
        }

        if (null == role) {
            LOGGER.warn("Role is undefined! token={}", customerSelfSchedulingRequestDecodedJwt);
            LOGGER.warn("Defaulting to Customer Role");
            role = CustomerSelfSchedulingRole.CUSTOMER;
        }

        PublicSchedulingControllerDateTimeSelectionHandler handler = handlers.get(role);

        if (null == handler) {
            LOGGER.error("Undefined handler for role of {}", role);
            return "scheduling/public-date-time-selection-unavailable";
        }

        return handler.dateTimeSelection(customerSelfSchedulingRequestDecodedJwt, date, model, httpServletRequest);

    }

    @PostMapping(value = "/schedule")
    public String schedule(@ModelAttribute @Valid PublicDateTimeSelectionDto request, Model model, RedirectAttributes redirectAttributes) {

        EventDetail eventDetail = null;

        try {
            eventDetail = this.selfScheduleService.schedule(request);
        } catch (SelfScheduleException e) {
            LOGGER.error(e.getMessage(), e);
            return "scheduling/public-date-time-selection-unavailable";
        }

        CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt = tokenConverter.convert(request.getToken());
        if (customerSelfSchedulingRequestDecodedJwt != null && psaScheduledForDirectSales(customerSelfSchedulingRequestDecodedJwt, eventDetail)) {
            return redirectToDirectSales(customerSelfSchedulingRequestDecodedJwt, eventDetail);
        }

        model.addAttribute("appointment_address", eventDetail.getAddress());
        model.addAttribute("appointment_duration", eventDetail.getDuration());
        model.addAttribute("appointment_date_time", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(eventDetail.getStartDateTime()));
        model.addAttribute("reason", eventDetail.getReason());

        return "scheduling/public-date-time-booked-successfully";

    }


    /**
     * @param calendarIds list of calendar ids
     * @param start       zoned date time
     * @param end         zoned date time
     * @return response entity with anonymize event details
     */
    @GetMapping(value = "/event")
    public ResponseEntity<List<AnonymizeEventDetailDto>> getTruckRolls(@RequestParam List<Long> calendarIds,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam ZonedDateTime start,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam ZonedDateTime end) {

        List<AnonymizeEventDetailDto> dtos = this.truckRollClient.events()
                .get(calendarIds, start, end)
                .stream()
                .map(AnonymizeEventDetailDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);

    }

    /**
     * @param request
     * @return suggestions
     */
    @PostMapping(value = "/suggestion")
    public ResponseEntity<List<Suggestion>> provideSuggestion(@RequestBody SuggestionRequest request) {
        LocalDate localDate = request.getStart().withZoneSameInstant(request.getTimezone()).toLocalDate();
        ZonedDateTime now = ZonedDateTime.now(request.getTimezone());
        List<Suggestion> suggestions = this.suggestionRequestHandler.provideSuggestion(request)
                .stream()
                .filter(suggestion -> suggestion.getStart().isAfter(now))
                .collect(Collectors.toList());
        return ResponseEntity.ok(suggestions);
    }

    @SafeVarargs
    private static Map<CustomerSelfSchedulingRole, PublicSchedulingControllerDateTimeSelectionHandler> initHandlers(
            NameValuePair<CustomerSelfSchedulingRole, PublicSchedulingControllerDateTimeSelectionHandler>... pairs) {

        Map<CustomerSelfSchedulingRole, PublicSchedulingControllerDateTimeSelectionHandler> handlers = new HashMap<>();

        for (NameValuePair<CustomerSelfSchedulingRole, PublicSchedulingControllerDateTimeSelectionHandler> pair : pairs) {
            handlers.put(pair.getKey(), pair.getValue());
        }

        return handlers;

    }

    private boolean psaScheduledForDirectSales(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt, EventDetail eventDetail) {
        String directSalesCustomerId = customerSelfSchedulingRequestDecodedJwt == null ? null : customerSelfSchedulingRequestDecodedJwt.getDirectSalesCustomerId();
        String issuer = customerSelfSchedulingRequestDecodedJwt == null ? null : customerSelfSchedulingRequestDecodedJwt.getIssuer();
        boolean psaScheduled = IsPSAEventDetail.getInstance().test(eventDetail);
        return StringUtils.equalsIgnoreCase("DirectSalesApplication", issuer) && !StringUtils.isBlank(directSalesCustomerId) && psaScheduled;
    }

    private String redirectToDirectSales(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt, EventDetail eventDetail) {
        try {
            return String.format("redirect:%s",
                    new URIBuilder()
                            .setScheme("https")
                            .setHost("rooftoprevolution.com")
                            .setPath(String.format("/content/content/scheduled/%s", customerSelfSchedulingRequestDecodedJwt.getDirectSalesCustomerId()))
                            .setParameter("appointmentTime", String.valueOf(eventDetail.getStartDateTime().toEpochSecond()))
                            .build().toURL().toString()
            );
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}