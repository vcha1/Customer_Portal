package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.my1stle.customer.portal.service.util.OperationalArea;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a utility class that returns the appropriate record type id
 * based on the given operational area. Hard coded values are based on
 * 1st Light Energy's Salesforce Production org
 *
 * @see <a href="https://1stlight.my.salesforce.com/ui/setup/rectype/RecordTypes?type=Case&setupid=CaseRecords&retURL=%2Fui%2Fsetup%2FSetup%3Fsetupid%3DCase">Case Record Types</a>
 */
public enum SalesforceServiceCaseRecordType {

    ;
    private static Map<String, String> recordTypeMapping = null;

    static final String CONSERVATION_CASE = "01270000000MJVTAA4";
    static final String EASTERN_SERVICE_AREA = "01270000000MJrUAAW";
    static final String WESTERN_SERVICE_AREA = "01270000000MJrZAAW";
    static final String COMMERICAL_SERVICE = "01270000000MJreAAG";
    static final String CLOSER_OPEN_ITEM = "01270000000MxVUAA0";
    static final String CUSTOMER_COMPLAINT_OPEN_CASE = "01270000000Mxn4AAC";

    static {

        Map<String, String> map = new HashMap<>();

        map.put(OperationalArea.CALIFORNIA_BAKERSFIELD, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CALIFORNIA_DUBLIN, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CALIFORNIA_FRESNO, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CALIFORNIA_LOS_ANGELES, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CAILFORNIA_MANTECA, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CALIFORNIA_SACRAMENTO, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CALIFORNIA_SAN_DIEGO, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CALIFORNIA_YUBA_CITY, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.COLORADO, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.CONNECTICUT, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.MASSACHUSETTS, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.MINNESOTA, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.NEVADA, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.NEW_HAMPSHIRE, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.NEW_JERSEY, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.NEW_YORK, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.NEW_YORK_LONG_ISLAND, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.NORTH_CAROLINA, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SOUTH_CAROLINA, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_CA_BAKERSFIELD, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_CA_FRESNO, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_CA_MANTECA, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_CA_SOCAL, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_MA_HOLLISTON, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_NJ, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_NY_BROOKLYN, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_NY_LONG_ISLAND, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_NY_OTHER, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_NY_QUEENS, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_NY_STATEN_ISLAND, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_PA, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_RI, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_SC, EASTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_UT, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.SUB_UTAH, WESTERN_SERVICE_AREA);
        map.put(OperationalArea.UTAH, WESTERN_SERVICE_AREA);

        recordTypeMapping = Collections.unmodifiableMap(map);

    }


    /**
     * @param operationalArea specified operational area
     * @return nullable record type id.
     */
    public static String getByOperationalArea(String operationalArea) {
        return recordTypeMapping.get(operationalArea);
    }

}
