package com.my1stle.customer.portal.service.serviceapi;

import java.util.stream.Stream;

public enum ServiceApiGroup {

    BAKERSFIELD(3, "Bakersfield"),
    CALIFORNIA(4, "California"),
    COMMERCIAL_EAST(5, "Commercial - East"),
    COMMERCIAL_WEST(6, "Commercial - West"),
    CONNECTICUT(7, "Connecticut"),
    CONSERVATION_CA(8, "Conservation CA"),
    CONSERVATION_NJ(9, "Conservatoin NJ"),
    FREEPORT(10, "Freeport"),
    FRESNO(11, "Fresno"),
    LAS_VEGAS(12, "Las Vegas"),
    MARYLAND(13, "Maryland"),
    MASSACHUSETTS(14, "Massachusetts"),
    NEW_JERSEY(15, "New Jersey"),
    SO_CAL(16, "SoCal"),
    SOUTH_CAROLINA(17, "South Carolina"),
    UTAH(18, "Utah"),
    VERMONT(19, "Vermont"),
    RHODE_ISLAND(20, "Rhode Island"),
    SERVICE(21, "Service");

    private long id;
    private String label;

    private ServiceApiGroup(long id, String label) {
        this.id = id;
        this.label = label;
    }

    public static ServiceApiGroup valueOf(Long id) {
        if (null == id) {
            return null;
        }
        return Stream.of(ServiceApiGroup.values())
                .filter(value -> value.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
