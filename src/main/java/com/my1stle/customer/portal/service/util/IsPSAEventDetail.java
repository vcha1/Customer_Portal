package com.my1stle.customer.portal.service.util;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.SkillDetail;
import com.my1stle.customer.portal.serviceImpl.scheduling.Scheduling;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class IsPSAEventDetail implements Predicate<EventDetail> {

    private static IsPSAEventDetail instance;

    private static final Set<Long> skillIds = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(Scheduling.FULL_PSA_SKILL_ID, Scheduling.EXTERIOR_PSA_SKILL_ID))
    );

    private IsPSAEventDetail() {

    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param eventDetail the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override
    public boolean test(EventDetail eventDetail) {
        return eventDetail.getSkills()
                .stream()
                .map(SkillDetail::getId)
                .anyMatch(skillIds::contains);
    }

    public static IsPSAEventDetail getInstance() {
        if (null == instance) {
            instance = new IsPSAEventDetail();
        }
        return instance;
    }
}
