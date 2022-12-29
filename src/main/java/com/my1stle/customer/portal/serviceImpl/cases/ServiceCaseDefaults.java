package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.serviceapi.ServiceApi;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCategory;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiGroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

enum ServiceCaseDefaults {

    ;
    static final Set<Long> SERVICE_GROUP_ISSUE_TYPE = Collections.unmodifiableSet(
            new HashSet<>(
                    Arrays.asList(
                            ServiceApiCategory.HIGH_UTILITY_BILL.getId(),
                            ServiceApiCategory.NOT_SURE.getId()
                    )
            )

    );
    static final Set<Long> DIVISION_GROUP_ISSUE_TYPE = Collections.unmodifiableSet(
            new HashSet<>(
                    Arrays.asList(
                            ServiceApiCategory.ELECTRICAL_ISSUE.getId(),
                            ServiceApiCategory.INVERTER_ISSUE.getId(),
                            ServiceApiCategory.METER_ISSUE.getId(),
                            ServiceApiCategory.PANEL_ISSUE.getId(),
                            ServiceApiCategory.SYSTEM_CHECK.getId(),
                            ServiceApiCategory.PEST_CONTROL.getId(),
                            ServiceApiCategory.ROOF_LEAK.getId()
                    )
            )

    );

    private static Map<String, Long> DIVISION_GROUPS_BY_ROOTSTOCK_DIVISION_MASTER_ID = null;

    static Long getGroupIdByRootstockDivisionMasterId(String divisionMasterId) {
        if (DIVISION_GROUPS_BY_ROOTSTOCK_DIVISION_MASTER_ID == null) {
            DIVISION_GROUPS_BY_ROOTSTOCK_DIVISION_MASTER_ID = initializeDivisionGroupsByRootDivisionMasterIdMapping();
        }
        return DIVISION_GROUPS_BY_ROOTSTOCK_DIVISION_MASTER_ID.get(divisionMasterId);
    }

    private static Map<String, Long> initializeDivisionGroupsByRootDivisionMasterIdMapping() {

        Map<String, Long> mapping = new HashMap<>();

        mapping.put("a7D70000000fyIWEAY", ServiceApiGroup.BAKERSFIELD.getId());
        mapping.put("a7D70000000CaR7EAK", ServiceApiGroup.CALIFORNIA.getId());
        mapping.put("a7D0g00000097XLEAY", ServiceApiGroup.COMMERCIAL_EAST.getId());
        mapping.put("a7D0g000000H3gSEAS", ServiceApiGroup.COMMERCIAL_WEST.getId());
        mapping.put("a7D0g000000H30PEAS", ServiceApiGroup.CONNECTICUT.getId());
        mapping.put("a7D70000000CaS3EAK", ServiceApiGroup.CONSERVATION_CA.getId());
        mapping.put("a7D70000000CaS8EAK", ServiceApiGroup.CONSERVATION_NJ.getId());
        mapping.put("a7D70000000GrmNEAS", ServiceApiGroup.FREEPORT.getId());
        mapping.put("a7D70000000CaSmEAK", ServiceApiGroup.FRESNO.getId());
        mapping.put("a7D0g000000PW4WEAW", ServiceApiGroup.LAS_VEGAS.getId());
        mapping.put("a7D0g000000Y1YMEA0", ServiceApiGroup.MARYLAND.getId());
        mapping.put("a7D70000000CaRHEA0", ServiceApiGroup.MASSACHUSETTS.getId());
        mapping.put("a7D70000000CaRMEA0", ServiceApiGroup.NEW_JERSEY.getId());
        mapping.put("a7D0g000000Y1UHEA0", ServiceApiGroup.SO_CAL.getId());
        mapping.put("a7D70000000boWCEAY", ServiceApiGroup.SOUTH_CAROLINA.getId());
        mapping.put("a7D70000000bp7WEAQ", ServiceApiGroup.UTAH.getId());
        mapping.put("a7D0g00000093qTEAQ", ServiceApiGroup.VERMONT.getId());
        mapping.put("a7D0g000000H2yEEAS", ServiceApiGroup.RHODE_ISLAND.getId());

        return Collections.unmodifiableMap(mapping);

    }
}





