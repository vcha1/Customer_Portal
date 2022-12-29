package com.my1stle.customer.portal.serviceImpl;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.NewTruckRollDetails;
import com.my1stle.customer.portal.service.TruckRollRequestFactory;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import com.my1stle.customer.portal.web.dto.scheduling.PublicDateTimeSelectionDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit Test for {@link DefaultTruckRollRequestFactory}
 * TODO test {@link DefaultTruckRollRequestFactory#from(ServiceRequest)}
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultTruckRollRequestFactoryTest {

    private TruckRollRequestFactory truckRollRequestFactory;

    @Mock
    private Converter<String, CustomerSelfSchedulingRequestDecodedJwt> mockCustomerSelfSchedulingRequestDecodedJwtConverter;

    @Before
    public void setUp() throws Exception {
        this.truckRollRequestFactory = new DefaultTruckRollRequestFactory(mockCustomerSelfSchedulingRequestDecodedJwtConverter)
                .setMinutesInAWorkDay(480);
    }

    @Test
    public void shouldGenerateListOfNewTruckRollDetailsFromPublicDateTimeSelectionDto1() {

        // Given
        ZonedDateTime startDateTime = ZonedDateTime.of(LocalDateTime.of(2019, 10, 1, 8, 0), ZoneId.of("America/Los_Angeles"));
        PublicDateTimeSelectionDto dto = new PublicDateTimeSelectionDto();
        dto.setName("Foo Bar - 001869");
        dto.setInstallationId("some installation id");
        dto.setCalendarId(1869L);
        dto.setStartDateTime(startDateTime);
        dto.setDuration(840);
        dto.setSkillIds(Collections.singleton(7L));
        dto.setNotes("Lorum Ipsum");
        dto.setToken("some token");

        // Stubbing
        CustomerSelfSchedulingRequestDecodedJwt stubbedDecodedJWT = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        when(mockCustomerSelfSchedulingRequestDecodedJwtConverter.convert(eq("some token"))).thenReturn(stubbedDecodedJWT);

        // When
        List<NewTruckRollDetails> details = this.truckRollRequestFactory.from(dto);

        // Then
        assertEquals(2, details.size());
        assertTrue(details.stream().allMatch(detail -> StringUtils.equalsIgnoreCase("Foo Bar - 001869", detail.getName())));
        //assertTrue(details.stream().allMatch(detail -> Objects.equals(480, detail.getDuration())));
        assertFalse(details.stream().allMatch(NewTruckRollDetails::getNeedsConfirmation));
        assertTrue(details.stream().allMatch(detail -> detail.getSkillIds().contains(7L)));
        assertTrue(details.stream().allMatch(NewTruckRollDetails::getFixedAppointment));
        assertTrue(details.stream().allMatch(detail -> StringUtils.equalsIgnoreCase(DefaultTruckRollRequestFactory.CUSTOMER_PORTAL_ACCOUNT, detail.getRequestedBy())));
        assertTrue(details.stream().allMatch(detail -> detail.getResourceIds().contains(1869L)));
        assertTrue(details.stream().allMatch(detail -> StringUtils.equalsIgnoreCase("Lorum Ipsum", detail.getAppointmentNotes())));

        for (int i = 0; i < details.size(); i++) {

            NewTruckRollDetails truckRollDetails = details.get(i);
            ZonedDateTime startZdt = truckRollDetails.getStartDateTime();
            LocalDate expectedLocalDate = startDateTime.toLocalDate().plusDays(i);

            assertEquals(expectedLocalDate, startZdt.toLocalDate());

            if (i == details.size() - 1) {
                assertEquals(Integer.valueOf(360), truckRollDetails.getDuration());
            } else {
                assertEquals(Integer.valueOf(480), truckRollDetails.getDuration());
            }

        }

    }

    @Test
    public void shouldGenerateListOfNewTruckRollDetailsFromPublicDateTimeSelectionDto2() {

        // Given
        ZonedDateTime startDateTime = ZonedDateTime.of(LocalDateTime.of(2019, 10, 1, 8, 0), ZoneId.of("America/Los_Angeles"));
        PublicDateTimeSelectionDto dto = new PublicDateTimeSelectionDto();
        dto.setName("Foo Bar - 001869");
        dto.setInstallationId("some installation id");
        dto.setCalendarId(1869L);
        dto.setStartDateTime(startDateTime);
        dto.setDuration(240);
        dto.setSkillIds(Collections.singleton(7L));
        dto.setNotes("Lorum Ipsum");
        dto.setToken("some token");

        // Stubbing
        CustomerSelfSchedulingRequestDecodedJwt stubbedDecodedJWT = mock(CustomerSelfSchedulingRequestDecodedJwt.class);
        when(mockCustomerSelfSchedulingRequestDecodedJwtConverter.convert(eq("some token"))).thenReturn(stubbedDecodedJWT);

        // When
        List<NewTruckRollDetails> details = this.truckRollRequestFactory.from(dto);

        // Then
        assertEquals(1, details.size());
        assertTrue(details.stream().allMatch(detail -> StringUtils.equalsIgnoreCase("Foo Bar - 001869", detail.getName())));
        //assertTrue(details.stream().allMatch(detail -> Objects.equals(480, detail.getDuration())));
        assertFalse(details.stream().allMatch(NewTruckRollDetails::getNeedsConfirmation));
        assertTrue(details.stream().allMatch(detail -> detail.getSkillIds().contains(7L)));
        assertTrue(details.stream().allMatch(NewTruckRollDetails::getFixedAppointment));
        assertTrue(details.stream().allMatch(detail -> StringUtils.equalsIgnoreCase(DefaultTruckRollRequestFactory.CUSTOMER_PORTAL_ACCOUNT, detail.getRequestedBy())));
        assertTrue(details.stream().allMatch(detail -> detail.getResourceIds().contains(1869L)));
        assertTrue(details.stream().allMatch(detail -> StringUtils.equalsIgnoreCase("Lorum Ipsum", detail.getAppointmentNotes())));

        for (int i = 0; i < details.size(); i++) {

            NewTruckRollDetails truckRollDetails = details.get(i);
            ZonedDateTime startZdt = truckRollDetails.getStartDateTime();
            LocalDate expectedLocalDate = startDateTime.toLocalDate().plusDays(i);

            assertEquals(expectedLocalDate, startZdt.toLocalDate());

            if (i == details.size() - 1) {
                assertEquals(Integer.valueOf(240), truckRollDetails.getDuration());
            } else {
                assertEquals(Integer.valueOf(480), truckRollDetails.getDuration());
            }

        }

    }

}