package com.my1stle.customer.portal.serviceImpl.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import com.dev1stle.scheduling.system.v1.model.suggestion.SuggestionCode;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollDetails;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollResult;
import com.my1stle.customer.portal.application.event.OnScheduledEventDetail;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.TruckRollRequestFactory;
import com.my1stle.customer.portal.service.datetimeselection.DateTimeSelectionService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.notification.NotificationService;
import com.my1stle.customer.portal.service.scheduling.AppointmentAlreadyBookedException;
import com.my1stle.customer.portal.service.scheduling.NoAvailabilityException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleService;
import com.my1stle.customer.portal.service.scheduling.SuggestionRequestHandler;
import com.my1stle.customer.portal.service.time.zone.TimeZoneService;
import com.my1stle.customer.portal.service.util.ActiveAppointmentPredicate;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DefaultSelfScheduleService implements SelfScheduleService {

    private final TruckRollClient truckRollClient;
    private final TruckRollRequestFactory truckRollRequestFactory;
    private final SuggestionRequestHandler suggestionRequestHandler;
    private final DateTimeSelectionService dateTimeSelection;
    private final TimeZoneService timeZoneService;
    private final InstallationService installationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static Logger LOGGER = LoggerFactory.getLogger(DefaultSelfScheduleService.class);

    private static final Integer DEFAULT_DURATION = 480;
    private static final int RANGE_IN_MONTHS = 6; // 6 Months
    static final LocalTime ALLOWED_TIME_SLOT = LocalTime.of(8, 0); // i.e 8 AM
    static final String CANCELLED_CONTRACT = "Cancelled Contract";
    private static final long TIME_SLOT_SIZE = 30; // 30 mins

    @Autowired
    public DefaultSelfScheduleService(
            TruckRollClient truckRollClient,
            TruckRollRequestFactory truckRollRequestFactory,
            @Qualifier("multipleDaySuggestionRequestHandler") SuggestionRequestHandler suggestionRequestHandler,
            DateTimeSelectionService dateTimeSelection,
            TimeZoneService timeZoneService,
            @Qualifier("publicInstallationService") InstallationService installationService,
            ApplicationEventPublisher applicationEventPublisher) {

        this.truckRollClient = truckRollClient;
        this.truckRollRequestFactory = truckRollRequestFactory;
        this.suggestionRequestHandler = suggestionRequestHandler;
        this.dateTimeSelection = dateTimeSelection;
        this.timeZoneService = timeZoneService;
        this.installationService = installationService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * @param customerSelfSchedulingRequestDecodedJwt decodedJWT
     * @return an instance of {@link PublicDateTimeSelectionDto}
     * @throws SelfScheduleException             when decodedJWT is expired
     * @throws AppointmentAlreadyBookedException when there is already an appointment booked
     * @throws NoAvailabilityException           when there no available appointment times
     * @implSpec must return non-null {@link PublicDateTimeSelectionDto}
     */
    @Override
    public PublicDateTimeSelectionDto generateSelfScheduleRequestDto(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt) throws SelfScheduleException, AppointmentAlreadyBookedException, NoAvailabilityException {
        return generateSelfScheduleRequestDto(customerSelfSchedulingRequestDecodedJwt, false, null);
    }

    /**
     * @param customerSelfSchedulingRequestDecodedJwt      decodedJWT
     * @param preselectEarliestPossibleAppointmentTimeSlot indicates whether the date and time should be pre-populated
     * @param preferredLocalDate                           the local date in which would the earliest possible appointment time should be
     * @return an instance of {@link PublicDateTimeSelectionDto} with {@link PublicDateTimeSelectionDto#getStartDateTime()} and {@link PublicDateTimeSelectionDto#getDuration()} not return null
     * @throws SelfScheduleException             when decodedJWT is expired
     * @throws AppointmentAlreadyBookedException when there is already an appointment booked
     * @throws NoAvailabilityException           when there no available appointment times
     * @implSpec must return non-null {@link PublicDateTimeSelectionDto}
     * @implNote if preferredLocalDate is null or is before today will use today as the preferred local date
     */
    @Override
    public PublicDateTimeSelectionDto generateSelfScheduleRequestDto(CustomerSelfSchedulingRequestDecodedJwt customerSelfSchedulingRequestDecodedJwt, boolean preselectEarliestPossibleAppointmentTimeSlot, LocalDate preferredLocalDate) throws SelfScheduleException, AppointmentAlreadyBookedException, NoAvailabilityException {

        Set<Long> skillsRequiredForInstallation = Collections.singleton(Scheduling.INSTALLATION_SKILL_ID);
        Set<Long> skillsRequiredForBatteryInstallation = Collections.singleton(Scheduling.BATTERY_SKILL_ID);
        boolean isBatteryInstall = customerSelfSchedulingRequestDecodedJwt.isBatteryInstall();

        Installation installation = installationService.getInstallationById(customerSelfSchedulingRequestDecodedJwt.getInstallationId());

        if (StringUtils.equalsIgnoreCase(CANCELLED_CONTRACT, installation.getInstallStatus())) {
            throw new SelfScheduleException(String.format("Installation is in %s !", installation.getInstallStatus()));
        }

        List<Calendar> personnel = this.dateTimeSelection.getAvailableCalendars(installation.getId(), isBatteryInstall ? skillsRequiredForBatteryInstallation : skillsRequiredForInstallation);

        if (personnel.isEmpty()) {
            throw new NoAvailabilityException(
                    String.format("No available personnel for installation [id : %s] [name : %s] [operational area : %s] [skills required : %s]",
                            installation.getId(),
                            installation.getName(),
                            installation.getOperationalArea(),
                            isBatteryInstall ? skillsRequiredForBatteryInstallation : skillsRequiredForInstallation)
            );
        }

        if (null != installation.getScheduledStartDate()) {
            throw new AppointmentAlreadyBookedException(String.format("Installation [id : %s ] [name : %s] already has a scheduled start date of %s",
                    installation.getId(),
                    installation.getName(),
                    DateTimeFormatter.ISO_LOCAL_DATE.format(installation.getScheduledStartDate()))
            );
        }

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenExpirationDateTime = customerSelfSchedulingRequestDecodedJwt.getExpirationDateTime();
        ZonedDateTime tokenIssuedDate = customerSelfSchedulingRequestDecodedJwt.getIssueDateTime();

        boolean isOldToken = (tokenIssuedDate == null || tokenExpirationDateTime == null);

        Long duration = isOldToken ? DEFAULT_DURATION : customerSelfSchedulingRequestDecodedJwt.getDuration();

        // Check if token is expired
        if (tokenExpirationDateTime != null && now.isAfter(tokenExpirationDateTime)) {
            throw new SelfScheduleException(String.format("Token is expired for [id : %s ] [name : %s] [expiration : %s]",
                    installation.getId(),
                    installation.getName(),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(tokenExpirationDateTime)
            ));
        }

        Predicate<EventDetail> activeAppointmentPredicate = new ActiveAppointmentPredicate(now, isBatteryInstall ? skillsRequiredForBatteryInstallation : skillsRequiredForInstallation);

        List<EventDetail> activeInstallation = this.truckRollClient.events()
                .getByCustomerAccountId(installation.getAccountId())
                .stream()
                .filter(activeAppointmentPredicate)
                .collect(Collectors.toList());

        if (!activeInstallation.isEmpty()) {
            throw new AppointmentAlreadyBookedException(String.format("Installation [id : %s ] [name : %s] already has a scheduled installation(s) [event ids : %s]",
                    installation.getId(),
                    installation.getName(),
                    activeInstallation.stream().map(EventDetail::getId).collect(Collectors.toSet())));
        }

        Appointment appointment = this.dateTimeSelection.createAppointment(
                installation.getId(),
                duration.intValue()
        );

        if (appointment.getDuration() == null) {
            throw new BadRequestException("Duration was expected!");
        }

        PublicDateTimeSelectionDto dto = new PublicDateTimeSelectionDto();

        dto.setName(installation.getName());
        dto.setDuration(Math.toIntExact(appointment.getDuration()));
        dto.setInstallationId(installation.getId());
        dto.setSkillIds(isBatteryInstall ? skillsRequiredForBatteryInstallation : skillsRequiredForInstallation);

        if (preselectEarliestPossibleAppointmentTimeSlot) {
            Suggestion suggestion = getEarliestAvailableAppointmentTime(personnel, preferredLocalDate, appointment);
            dto.setStartDateTime(suggestion.getStart());
            dto.setCalendarId(Long.parseLong(suggestion.getResource()));
        }

        dto.setToken(customerSelfSchedulingRequestDecodedJwt.getToken());

        return dto;
    }

    /**
     * @param publicDateTimeSelectionDto representing the date time selection
     * @return eventDetail
     * @throws SelfScheduleException when an validation or error occurs while commiting the event
     * @implNote sends a notification via {@link NotificationService<EventDetail>}
     */
    @Override
    public EventDetail schedule(PublicDateTimeSelectionDto publicDateTimeSelectionDto) throws SelfScheduleException {

        List<NewTruckRollDetails> details = truckRollRequestFactory.from(publicDateTimeSelectionDto);

        boolean invalidRequest = details.stream().anyMatch(new InvalidTruckRollStartDateTime());
        if (invalidRequest) {
            throw new SelfScheduleException("Appointment cannot be book at that time!");
        }

        NewTruckRollResult newTruckRollResult = this.truckRollClient.events().create(details);

        if (newTruckRollResult.getSuccessful()) {

            List<EventDetail> eventDetails = this.truckRollClient.events()
                    .get(newTruckRollResult.getIds());

            if (!eventDetails.stream().map(EventDetail::getId).collect(Collectors.toSet()).containsAll(newTruckRollResult.getIds())) {
                throw new BadRequestException("All Truck Rolls Not Found!");
            }

            PublicEventDetailDto eventDetail = new PublicEventDetailDto(eventDetails);
            applicationEventPublisher.publishEvent(new OnScheduledEventDetail(this, eventDetail));

            return eventDetail;

        } else {
            LOGGER.error(newTruckRollResult.getErrorMessage());
            throw new SelfScheduleException(newTruckRollResult.getErrorMessage());
        }
    }

    private Suggestion getEarliestAvailableAppointmentTime(List<Calendar> calendars, LocalDate localDate, Appointment appointment) throws NoAvailabilityException {

        ZoneId zone = getTimeZone(appointment);

        LocalDate today = LocalDate.now(zone);

        if (null == localDate || localDate.isBefore(today)) {
            localDate = today;
        }

        ZonedDateTime start = localDate.atStartOfDay(zone);
        ZonedDateTime end = localDate.plusMonths(RANGE_IN_MONTHS).atTime(LocalTime.MAX).atZone(zone);

        SuggestionRequest request = new SuggestionRequest();
        request.setTimezone(zone);
        request.setStart(start);
        request.setEnd(end);
        request.setResources(calendars);
        request.setAppointment(appointment);
        request.setBlockDuration(TIME_SLOT_SIZE);
        request.setTolerance(0L);

        return this.suggestionRequestHandler.provideSuggestion(request)
                .stream()
                .filter(suggestion -> suggestion.getStart().toLocalTime().equals(ALLOWED_TIME_SLOT) && SuggestionCode.SERVICEABLE.getCode().equals(suggestion.getSuggestionCode()))
                .min(Comparator.comparing(Suggestion::getStart))
                .orElseThrow(() -> new NoAvailabilityException("Unable to find an available time slot!"));


    }

    private ZoneId getTimeZone(Appointment appointment) {

        return this.timeZoneService.getBySingleLineAddress(appointment.getAddress());

    }

    private static final class InvalidTruckRollStartDateTime implements Predicate<NewTruckRollDetails> {

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param newTruckRollDetails the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        @Override
        public boolean test(final NewTruckRollDetails newTruckRollDetails) {

            Set<Long> skillIds = newTruckRollDetails.getSkillIds();

            if (skillIds.contains(Scheduling.BATTERY_SKILL_ID) || skillIds.contains(Scheduling.INSTALLATION_SKILL_ID)) {

                ZonedDateTime startZdt = newTruckRollDetails.getStartDateTime();
                ZoneId zone = startZdt.getZone();

                LocalDate startLocalDate = startZdt.toLocalDate();

                LocalDate today = LocalDate.now(zone);
                LocalDate limit = LocalDate.now(zone).plusMonths(RANGE_IN_MONTHS);

                return !startZdt.toLocalTime().equals(ALLOWED_TIME_SLOT) || startLocalDate.isAfter(limit) || startLocalDate.isBefore(today) || startLocalDate.isEqual(today);
            }

            return false;

        }

    }

}