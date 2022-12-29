package com.my1stle.customer.portal.service.time.zone;

import java.time.ZoneId;

public interface TimeZoneService {


    /**
     * @param singleLineAddress ; address formatted as a single single
     * @return zone id based on the single line address;
     */
    ZoneId getBySingleLineAddress(String singleLineAddress);

}
