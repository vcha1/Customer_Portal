package com.my1stle.customer.portal.service.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * recipients determined from @see <a href="https://jira.1stlightenergy.com/browse/HD-11518">HD-11518</a>
 */
public enum DivisionEmail {

    ;
    public static final String BAKERSFIELD = "bakersfieldsf@1stlightenergy.com";
    public static final String MANTECA = "mantecasf@1stlightenergy.com";
    public static final String SOCAL = "socaldivsf@1stlightenergy.com";
    public static final String CONNECTICUT = "ctdivsf@1stle.com";
    public static final String MASSACHUESETTS = "MAdivSF@1stlightenergy.com";
    public static final String NEW_JERSEY = "NJdivSF@1stlightenergy.com";
    public static final String UTAH = "ut-operations-sf@1stlightenergy.com";

    private static final Map<String, String> divisionEmailRecipient;

    static {

        Map<String, String> map = new HashMap<>();

        map.put(OperationalArea.CALIFORNIA_BAKERSFIELD, BAKERSFIELD);
        map.put(OperationalArea.CALIFORNIA_DUBLIN, MANTECA);
        map.put(OperationalArea.CALIFORNIA_FRESNO, BAKERSFIELD);
        map.put(OperationalArea.CALIFORNIA_LOS_ANGELES, SOCAL);
        map.put(OperationalArea.CAILFORNIA_MANTECA, MANTECA);
        map.put(OperationalArea.CALIFORNIA_SACRAMENTO, MANTECA);
        map.put(OperationalArea.CALIFORNIA_SAN_DIEGO, SOCAL);
        map.put(OperationalArea.CALIFORNIA_YUBA_CITY, MANTECA);
        map.put(OperationalArea.COLORADO, MANTECA);
        map.put(OperationalArea.CONNECTICUT, CONNECTICUT);
        map.put(OperationalArea.MASSACHUSETTS, MASSACHUESETTS);
        map.put(OperationalArea.MINNESOTA, MANTECA);
        map.put(OperationalArea.NEVADA, MANTECA);
        map.put(OperationalArea.NEW_HAMPSHIRE, MASSACHUESETTS);
        map.put(OperationalArea.NEW_JERSEY, NEW_JERSEY);
        map.put(OperationalArea.NEW_YORK, NEW_JERSEY);
        map.put(OperationalArea.NEW_YORK_LONG_ISLAND, NEW_JERSEY);
        map.put(OperationalArea.NORTH_CAROLINA, UTAH);
        map.put(OperationalArea.SOUTH_CAROLINA, UTAH);
        map.put(OperationalArea.SUB_CA_BAKERSFIELD, BAKERSFIELD);
        map.put(OperationalArea.SUB_CA_FRESNO, BAKERSFIELD);
        map.put(OperationalArea.SUB_CA_MANTECA, MANTECA);
        map.put(OperationalArea.SUB_CA_SOCAL, SOCAL);
        map.put(OperationalArea.SUB_MA_HOLLISTON, MASSACHUESETTS);
        map.put(OperationalArea.SUB_NJ, NEW_JERSEY);
        map.put(OperationalArea.SUB_NY_BROOKLYN, NEW_JERSEY);
        map.put(OperationalArea.SUB_NY_LONG_ISLAND, NEW_JERSEY);
        map.put(OperationalArea.SUB_NY_OTHER, NEW_JERSEY);
        map.put(OperationalArea.SUB_NY_QUEENS, NEW_JERSEY);
        map.put(OperationalArea.SUB_NY_STATEN_ISLAND, NEW_JERSEY);
        map.put(OperationalArea.SUB_PA, NEW_JERSEY);
        map.put(OperationalArea.SUB_RI, MASSACHUESETTS);
        map.put(OperationalArea.SUB_SC, UTAH);
        map.put(OperationalArea.SUB_UT, UTAH);
        map.put(OperationalArea.SUB_UTAH, UTAH);
        map.put(OperationalArea.UTAH, UTAH);

        divisionEmailRecipient = Collections.unmodifiableMap(map);
    }

    /**
     * @param operationalArea
     * @return 1st Light Energy Division's Email Address
     */
    public static String getDivisionEmail(String operationalArea) {
        return divisionEmailRecipient.get(operationalArea);
    }

}
