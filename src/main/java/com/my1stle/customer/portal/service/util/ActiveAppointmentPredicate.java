package com.my1stle.customer.portal.service.util;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.SkillDetail;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * tests if the event is an active appointment based on the given date time and
 * the required skills
 */
public class ActiveAppointmentPredicate implements Predicate<EventDetail> {

    private ZonedDateTime now;
    private Set<Long> requiredSkills;

    public ActiveAppointmentPredicate(ZonedDateTime now, Set<Long> requiredSkills) {
        this.now = now;
        this.requiredSkills = requiredSkills;
    }

    @Override
    public boolean test(final EventDetail eventDetail) {

        if (eventDetail.isCancelled()) {
            return false;
        }

        // Check if in the future
        Boolean isInTheFuture = false;
        if (eventDetail.getEndDateTime() != null && eventDetail.getEndDateTime().isAfter(now)) {
            isInTheFuture = true;
        }

        // Check if it has the required skills
        Boolean hasRequiredSkill = false;
        Set<Long> eventSkills = eventDetail.getSkills().stream().map(SkillDetail::getId).collect(Collectors.toSet());
        for (Long skillId : requiredSkills) {
            if (eventSkills.contains(skillId)) {
                hasRequiredSkill = true;
                break;
            }
        }

        return isInTheFuture && hasRequiredSkill;
    }

}
