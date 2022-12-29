package com.my1stle.customer.portal.serviceImpl.scheduling;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.CalendarDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.OperationalAreaDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.SkillDetail;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

class PublicEventDetailDto implements EventDetail {

    private List<EventDetail> multipleEventDetails;

    /**
     * @param multipleEventDetails non empty
     */
    PublicEventDetailDto(List<EventDetail> multipleEventDetails) {

        if (multipleEventDetails.isEmpty()) {
            throw new IllegalArgumentException("Expected Non Empty List of Event Details");
        }

        this.multipleEventDetails = multipleEventDetails;
        this.multipleEventDetails.sort(Comparator.comparing(EventDetail::getStartDateTime));

    }

    @Override
    public Long getId() {
        return multipleEventDetails.get(0).getId();
    }

    @Override
    public String getExternalId() {
        return multipleEventDetails.get(0).getExternalId();
    }

    @Override
    public String getName() {
        return multipleEventDetails.get(0).getName();
    }

    @Override
    public Boolean isCancelled() {
        return multipleEventDetails.get(0).isCancelled();
    }

    @Override
    public String getAddress() {
        return multipleEventDetails.get(0).getAddress();
    }

    @Override
    public Long getDuration() {
        return multipleEventDetails.stream()
                .map(EventDetail::getDuration)
                .mapToLong(Long::valueOf).sum();
    }

    @Override
    public String getNotes() {
        return multipleEventDetails.get(0).getNotes();
    }

    @Override
    public ZonedDateTime getStartDateTime() {
        return multipleEventDetails.get(0).getStartDateTime();
    }

    @Override
    public ZonedDateTime getEndDateTime() {
        return multipleEventDetails.get(multipleEventDetails.size() - 1).getEndDateTime();
    }

    @Override
    public Set<CalendarDetail> getCalendarDetails() {
        return multipleEventDetails.get(0).getCalendarDetails();
    }

    @Override
    public Set<SkillDetail> getSkills() {
        return multipleEventDetails.get(0).getSkills();
    }

    @Override
    public Set<OperationalAreaDetail> getOperationalAreas() {
        return multipleEventDetails.get(0).getOperationalAreas();
    }

    @Override
    public Boolean getNeedsConfirmation() {
        return multipleEventDetails.get(0).getNeedsConfirmation();
    }

    @Override
    public ZonedDateTime getTimeOfConfirmation() {
        return multipleEventDetails.get(0).getTimeOfConfirmation();
    }

    @Override
    public ZonedDateTime getTimeOfRejection() {
        return multipleEventDetails.get(0).getTimeOfRejection();
    }

    @Override
    public String getReason() {
        return multipleEventDetails.get(0).getReason();
    }

    @Override
    public String getCancellationReason() {
        return multipleEventDetails.get(0).getCancellationReason();
    }

    @Override
    public String getStatus() {
        return multipleEventDetails.get(0).getStatus();
    }

    @Override
    public String getRequestedBy() {
        return multipleEventDetails.get(0).getRequestedBy();
    }

    @Override
    public String getLastModifiedBy() {
        return multipleEventDetails.get(0).getLastModifiedBy();
    }
}
