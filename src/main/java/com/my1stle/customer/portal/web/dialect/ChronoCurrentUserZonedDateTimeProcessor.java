package com.my1stle.customer.portal.web.dialect;

import org.apache.commons.lang3.StringUtils;
import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class process the temporal accessor as a ZonedDateTime
 * and formats it with respect to the current user times zone
 * and the extending classes specified format pattern
 */
abstract class ChronoCurrentUserZonedDateTimeProcessor extends ChronoTemporalProcessor<ZonedDateTime> {

    ChronoCurrentUserZonedDateTimeProcessor(String dialectPrefix, String attributeName) {
        super(dialectPrefix, attributeName);
    }

    /**
     * @return pattern that conforms to {@link DateTimeFormatter#ofPattern(String)}
     * @implSpec pattern must conforms to {@link DateTimeFormatter#ofPattern(String)}
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns">Patterns</a>
     */
    protected abstract String pattern();

    /**
     * @param temporal
     * @return string representation of temporal with extending class
     * @implNote returns "N/A" if temporal is null
     */
    @Override
    protected final String format(ZonedDateTime temporal) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern());

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ZonedDateTime zdt = ZonedDateTime.from(temporal).withZoneSameInstant(
                StringUtils.isBlank(currentUser.getTimeZone()) ? ZoneOffset.UTC : ZoneId.of(currentUser.getTimeZone())
        );

        return dtf.format(zdt);

    }

}
