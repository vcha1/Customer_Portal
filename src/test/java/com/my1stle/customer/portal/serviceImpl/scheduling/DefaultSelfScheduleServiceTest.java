package com.my1stle.customer.portal.serviceImpl.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.suggestion.Suggestion;
import com.dev1stle.scheduling.system.v1.model.suggestion.SuggestionCode;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollDetails;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollResult;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.SkillDetail;
import com.my1stle.customer.portal.application.event.OnScheduledEventDetail;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.TruckRollRequestFactory;
import com.my1stle.customer.portal.service.datetimeselection.DateTimeSelectionService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.scheduling.AppointmentAlreadyBookedException;
import com.my1stle.customer.portal.service.scheduling.NoAvailabilityException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleException;
import com.my1stle.customer.portal.service.scheduling.SelfScheduleService;
import com.my1stle.customer.portal.service.scheduling.SuggestionRequestHandler;
import com.my1stle.customer.portal.service.time.zone.TimeZoneService;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.my1stle.customer.portal.web.dto.suggestion.SuggestionRequest;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link DefaultSelfScheduleService}
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultSelfScheduleServiceTest {

    private SelfScheduleService selfScheduleService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TruckRollClient mockTruckRollClient;

    @Mock
    private TruckRollRequestFactory mockTruckRollRequestFactory;

    @Mock
    private SuggestionRequestHandler mockSuggestionRequestHandler;

    @Mock
    private DateTimeSelectionService mockDateTimeSelectionService;

    @Mock
    private TimeZoneService mockTimeZoneService;

    @Mock
    private InstallationService mockInstallationService;

    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    @Before
    public void setUp() throws Exception {

        this.selfScheduleService = new DefaultSelfScheduleService(
                mockTruckRollClient,
                mockTruckRollRequestFactory,
                mockSuggestionRequestHandler,
                mockDateTimeSelectionService,
                mockTimeZoneService,
                mockInstallationService,
                mockApplicationEventPublisher);

    }

    @Test
    public void shouldThrowSelfScheduleExceptionWhenInstallationIsInContractCancelled() throws AppointmentAlreadyBookedException, NoAvailabilityException {

        // Given
        String installationId = "some installation id";
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);

        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        when(token.getInstallationId()).thenReturn(installationId);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn(DefaultSelfScheduleService.CANCELLED_CONTRACT);

        // When
        try {
            PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token);
        } catch (SelfScheduleException e) {

            // Then
            verify(mockInstallationService).getInstallationById(eq(installationId));

            return;

        }

        fail(String.format("Expected %s to be thrown!", SelfScheduleException.class.getSimpleName()));

    }

    @Test
    public void shouldThrowNoAvailabilityExceptionWhenNoAvailablePersonnel() throws AppointmentAlreadyBookedException, SelfScheduleException {

        String installationId = "some installation id";
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);

        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        when(token.getInstallationId()).thenReturn(installationId);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn("Scheduling Installation");
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(mockDateTimeSelectionService.getAvailableCalendars(eq(installationId), anySet())).thenReturn(Collections.emptyList());

        // When
        try {
            PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token);
        } catch (NoAvailabilityException e) {

            // Then
            verify(mockInstallationService).getInstallationById(eq(installationId));
            verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
            return;

        }

        fail(String.format("Expected %s to be thrown!", SelfScheduleException.class.getSimpleName()));

    }

    @Test
    public void shouldThrowAppointmentAlreadyBookedExceptionWhenInstallationHasAScheduleStartDate() throws SelfScheduleException, NoAvailabilityException {

        // Given
        String installationId = "some installation id";
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);

        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        when(token.getInstallationId()).thenReturn(installationId);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn("Scheduling Installation");
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(mockDateTimeSelectionService.getAvailableCalendars(eq(installationId), anySet())).thenReturn(Collections.singletonList(mock(Calendar.class)));
        when(stubbedInstallation.getScheduledStartDate()).thenReturn(LocalDate.now());

        // When
        try {
            PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token);
        } catch (AppointmentAlreadyBookedException e) {

            // Then
            verify(mockInstallationService).getInstallationById(eq(installationId));
            verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
            return;

        }

        fail(String.format("Expected %s to be thrown!", AppointmentAlreadyBookedException.class.getSimpleName()));

    }

    @Test
    public void shouldThrowSelfScheduleExceptionIfTokenIsExpired() throws NoAvailabilityException, AppointmentAlreadyBookedException {

        // Given
        String installationId = "some installation id";
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);

        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        when(token.getInstallationId()).thenReturn(installationId);
        when(token.getExpirationDateTime()).thenReturn(ZonedDateTime.now().minusYears(1));
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn("Scheduling Installation");
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(mockDateTimeSelectionService.getAvailableCalendars(eq(installationId), anySet())).thenReturn(Collections.singletonList(mock(Calendar.class)));
        when(stubbedInstallation.getScheduledStartDate()).thenReturn(null);

        // When
        try {
            PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token);
        } catch (SelfScheduleException e) {

            // Then
            verify(mockInstallationService).getInstallationById(eq(installationId));
            verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
            return;

        }

        fail(String.format("Expected %s to be thrown!", AppointmentAlreadyBookedException.class.getSimpleName()));

    }

    @Test
    public void shouldThrowAppointmentAlreadyBookedExceptionWhenActiveInstallationAppointment() throws SelfScheduleException, NoAvailabilityException {

        // Given
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);
        String installationId = "some installation id";
        String accountId = "some account id";

        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        EventDetail stubbedEventDetail = mock(EventDetail.class);
        SkillDetail stubbedSkillDetail = mock(SkillDetail.class);
        when(token.getInstallationId()).thenReturn(installationId);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn("Scheduling Installation");
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(stubbedInstallation.getAccountId()).thenReturn(accountId);
        when(mockDateTimeSelectionService.getAvailableCalendars(eq(installationId), anySet())).thenReturn(Collections.singletonList(mock(Calendar.class)));
        when(stubbedInstallation.getScheduledStartDate()).thenReturn(null);
        when(mockTruckRollClient.events().getByCustomerAccountId(eq(accountId))).thenReturn(Collections.singletonList(stubbedEventDetail));
        when(stubbedEventDetail.isCancelled()).thenReturn(false);
        when(stubbedEventDetail.getEndDateTime()).thenReturn(ZonedDateTime.now().plusYears(1));
        when(stubbedEventDetail.getSkills()).thenReturn(Collections.singleton(stubbedSkillDetail));
        when(stubbedSkillDetail.getId()).thenReturn(Scheduling.INSTALLATION_SKILL_ID);

        // When
        try {
            PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token, true, today);
        } catch (AppointmentAlreadyBookedException e) {

            // Then
            verify(mockInstallationService).getInstallationById(eq(installationId));
            verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
            verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
            verify(mockTruckRollClient.events()).getByCustomerAccountId(eq(accountId));
            return;

        }

        fail(String.format("Expected %s to be thrown!", AppointmentAlreadyBookedException.class.getSimpleName()));

    }

    @Test
    public void shouldGeneratePublicDateTimeSelectionDto() throws SelfScheduleException, AppointmentAlreadyBookedException, NoAvailabilityException {

        // Given
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);
        String installationId = "some installation id";
        String accountId = "some account id";
        String installationName = "Foo Bar - 001869";
        String operationalArea = "California - Manteca";
        String address = "1869 Moffat Blvd Manteca CA 95336";
        long duration = 480L;

        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        Suggestion stubbedSuggestion = mock(Suggestion.class);
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expires = now.plusYears(1);

        when(token.getInstallationId()).thenReturn(installationId);
        when(token.getDuration()).thenReturn(duration);
        when(token.getIssueDateTime()).thenReturn(now);
        when(token.getExpirationDateTime()).thenReturn(expires);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn("Scheduling Installation");
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(stubbedInstallation.getAccountId()).thenReturn(accountId);
        when(stubbedInstallation.getName()).thenReturn(installationName);
        when(mockDateTimeSelectionService.getAvailableCalendars(eq(installationId), anySet())).thenReturn(Collections.singletonList(mock(Calendar.class)));
        when(stubbedInstallation.getScheduledStartDate()).thenReturn(null);
        when(mockTruckRollClient.events().getByCustomerAccountId(eq(accountId))).thenReturn(Collections.emptyList());
        //when(mockTokenService.getClaimAs(eq(token), eq("duration"), eq(Integer.class))).thenReturn(480);
        when(mockDateTimeSelectionService.createAppointment(eq(installationId), anyInt()))
                .thenReturn(new Appointment(duration, address));
        when(mockSuggestionRequestHandler.provideSuggestion(any(SuggestionRequest.class)))
                .thenReturn(Collections.singletonList(stubbedSuggestion));
        when(stubbedSuggestion.getStart()).thenReturn(LocalDate.now().atTime(DefaultSelfScheduleService.ALLOWED_TIME_SLOT).atZone(zone));
        when(stubbedSuggestion.getSuggestionCode()).thenReturn(SuggestionCode.SERVICEABLE.getCode());
        when(stubbedSuggestion.getResource()).thenReturn("1869");
        when(mockTimeZoneService.getBySingleLineAddress(anyString())).thenReturn(zone);

        // When
        PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token, true, today);

        // Then
        verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
        verify(mockTruckRollClient.events()).getByCustomerAccountId(eq(accountId));
        verify(mockSuggestionRequestHandler, times(1)).provideSuggestion(any(SuggestionRequest.class));
        verify(mockTimeZoneService, times(1)).getBySingleLineAddress(eq(address));

        assertEquals(DefaultSelfScheduleService.ALLOWED_TIME_SLOT, publicDateTimeSelectionDto.getStartDateTime().toLocalTime());
        assertEquals(installationId, publicDateTimeSelectionDto.getInstallationId());
        assertEquals(Integer.valueOf(480), publicDateTimeSelectionDto.getDuration());
        assertEquals(installationName, publicDateTimeSelectionDto.getName());
        assertNull(publicDateTimeSelectionDto.getNotes());
        assertEquals(Long.valueOf(1869), publicDateTimeSelectionDto.getCalendarId());

    }


    @Test
    public void shouldSchedule() throws SelfScheduleException {

        // Given
        PublicDateTimeSelectionDto dto = new PublicDateTimeSelectionDto();
        Long eventId = 1869L;

        // Stubbing
        EventDetail stubbedEventDetail = mock(EventDetail.class);
        when(stubbedEventDetail.getId()).thenReturn(eventId);
        NewTruckRollResult stubbedTruckRollResult = mock(NewTruckRollResult.class);
        when(mockTruckRollClient.events().create(anyListOf(NewTruckRollDetails.class))).thenReturn(stubbedTruckRollResult);
        when(stubbedTruckRollResult.getSuccessful()).thenReturn(true);
        when(stubbedTruckRollResult.getIds()).thenReturn(Collections.singleton(eventId));
        when(mockTruckRollClient.events().get(eq(Collections.singleton(eventId)))).thenReturn(Collections.singletonList(stubbedEventDetail));

        // When
        EventDetail eventDetail = this.selfScheduleService.schedule(dto);

        // Then
        verify(mockTruckRollRequestFactory, times(1)).from(any(PublicDateTimeSelectionDto.class));
        verify(mockTruckRollClient.events()).create(anyListOf(NewTruckRollDetails.class));
        verify(mockTruckRollClient.events()).get(eq(Collections.singleton(eventId)));
        verify(mockApplicationEventPublisher).publishEvent(any(OnScheduledEventDetail.class));


    }

    @Test
    public void shouldThrowSelfScheduleExceptionWhenDtoIsInvalid() {

        // Given
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        PublicDateTimeSelectionDto dto = new PublicDateTimeSelectionDto();
        dto.setSkillIds(Collections.singleton(Scheduling.INSTALLATION_SKILL_ID));
        dto.setStartDateTime(LocalDate.now().atTime(12, 0).atZone(ZoneId.systemDefault()));

        // Stubbing
        NewTruckRollDetails stubbedDetails = mock(NewTruckRollDetails.class);
        when(stubbedDetails.getSkillIds()).thenReturn(dto.getSkillIds());
        when(stubbedDetails.getStartDateTime()).thenReturn(dto.getStartDateTime());
        when(mockTruckRollRequestFactory.from(any(PublicDateTimeSelectionDto.class)))
                .thenReturn(Collections.singletonList(stubbedDetails));
        when(stubbedDetails.getStartDateTime()).thenReturn(ZonedDateTime.now(zone).plusYears(20));

        // When
        try {
            EventDetail eventDetail = this.selfScheduleService.schedule(dto);
        } catch (SelfScheduleException e) {

            // Then
            verify(mockTruckRollRequestFactory, times(1)).from(any(PublicDateTimeSelectionDto.class));
            verify(mockTruckRollClient.events(), times(0)).create(anyListOf(NewTruckRollDetails.class));
            verify(mockTruckRollClient.events(), times(0)).get(anyLong());
            verify(mockApplicationEventPublisher, times(0)).publishEvent(any(OnScheduledEventDetail.class));

            assertEquals("Appointment cannot be book at that time!", e.getMessage());

            return;
        }

        fail(String.format("Expected %s to be thrown!", SelfScheduleException.class.getSimpleName()));

    }

    @Test
    public void shouldThrowSelfScheduleExceptionWhenUnableToCreateTruckRoll() {

        // Given
        PublicDateTimeSelectionDto dto = new PublicDateTimeSelectionDto();
        Long eventId = 1869L;

        // Stubbing
        NewTruckRollResult stubbedTruckRollResult = mock(NewTruckRollResult.class);
        when(mockTruckRollClient.events().create(anyListOf(NewTruckRollDetails.class))).thenReturn(stubbedTruckRollResult);
        when(stubbedTruckRollResult.getSuccessful()).thenReturn(false);
        when(stubbedTruckRollResult.getErrorMessage()).thenReturn("Stubbed Error Message. Please ignore...");

        // When
        try {
            EventDetail eventDetail = this.selfScheduleService.schedule(dto);
        } catch (SelfScheduleException e) {

            // Then
            verify(mockTruckRollRequestFactory, times(1)).from(any(PublicDateTimeSelectionDto.class));
            verify(mockTruckRollClient.events()).create(anyListOf(NewTruckRollDetails.class));
            verify(mockTruckRollClient.events(), times(0)).get(eq(Collections.singleton(eventId)));
            verify(mockApplicationEventPublisher, times(0)).publishEvent(any(OnScheduledEventDetail.class));

            return;

        }

        fail(String.format("Expected %s to be thrown!", SelfScheduleException.class.getSimpleName()));

    }

    @Test
    public void shouldThrowBadRequestExceptionWhenTheDeterminedDurationIsNull() throws SelfScheduleException, AppointmentAlreadyBookedException, NoAvailabilityException {

        // Given
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        CustomerSelfSchedulingRequestDecodedJwt token = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        LocalDate today = LocalDate.now(zone);
        String installationId = "some installation id";
        String accountId = "some account id";
        String installationName = "Foo Bar - 001869";
        String operationalArea = "California - Manteca";
        String address = "1869 Moffat Blvd Manteca CA 95336";


        // Stubbing
        Installation stubbedInstallation = mock(Installation.class);
        Suggestion stubbedSuggestion = mock(Suggestion.class);

        when(token.getInstallationId()).thenReturn(installationId);
        when(token.getIssueDateTime()).thenReturn(null);
        when(token.getExpirationDateTime()).thenReturn(null);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubbedInstallation);
        when(stubbedInstallation.getInstallStatus()).thenReturn("Scheduling Installation");
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(stubbedInstallation.getAccountId()).thenReturn(accountId);
        when(mockDateTimeSelectionService.getAvailableCalendars(eq(installationId), anySet())).thenReturn(Collections.singletonList(mock(Calendar.class)));
        when(stubbedInstallation.getScheduledStartDate()).thenReturn(null);
        when(mockTruckRollClient.events().getByCustomerAccountId(eq(accountId))).thenReturn(Collections.emptyList());
        when(mockDateTimeSelectionService.createAppointment(eq(installationId), anyInt()))
                .thenReturn(new Appointment(null, address));

        // When
        try {
            PublicDateTimeSelectionDto publicDateTimeSelectionDto = this.selfScheduleService.generateSelfScheduleRequestDto(token, true, today);
        } catch (BadRequestException e) {

            // Then
            verify(mockDateTimeSelectionService, times(1)).getAvailableCalendars(eq(installationId), anySet());
            verify(mockTruckRollClient.events()).getByCustomerAccountId(eq(accountId));
            verify(mockSuggestionRequestHandler, times(0)).provideSuggestion(any(SuggestionRequest.class));
            verify(mockTimeZoneService, times(0)).getBySingleLineAddress(eq(address));

            return;

        }

        fail(String.format("%s expected to be thrown!", BadRequestException.class.getSimpleName()));

    }
    /**/


}