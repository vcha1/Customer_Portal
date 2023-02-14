package com.my1stle.customer.portal.service.serviceapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ServiceApiCategory {

    NOT_SURE(1, null, "Other"),
    ELECTRICAL_ISSUE(2, null, "Electrical Issue"),
    INVERTER_ISSUE(3, null, "Inverter Issue"),
    METER_ISSUE(4, null, "Meter Issue"),
    PANEL_ISSUE(5, null, "Panel Issue"),
    SYSTEM_CHECK(6, null,  "System Check"),
    PEST_CONTROL(7, null, "Pest Control"),
    TAX_QUESTIONS(8, 15L, "Tax Questions"),
    STATUS_OF_CONSTRUCTION(9, 15L, "Status of construction"),
    REQUEST_COPIES_OF_CONTRACT_DOCUMENTS(10, 15L, "Request copies of contract documents"),
    REQUEST_CONTACT_FROM_SALES_REPRESENTATIVE(11, 15L, "Request contact from Sales Representative"),
    STATUS_OF_REIMBURSEMENT(12, 15L, "Status of Reimbursement"),
    CANCELLATION(13, 15L, "Cancellation"),
    OTHER(14, 15L, "Other"),
    PREINSTALLATION_ISSUE(15, 15L, "Pre-installation Issue"),
    ROOF_LEAK(16, null, "Roof Leak"),
    HIGH_UTILITY_BILL(17, null, "High Utility Bill"),
    INTERNAL(18, null, "Internal");

    private long id;
    private Long parentId;
    private String label;

    private static final Set<Long> systemOperationalCategoryIds = Collections.unmodifiableSet(
            new HashSet<>(
                    Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 16L, 17L)
            )
    );

    private static final Set<Long> preInstallationCategoryIds = Collections.unmodifiableSet(
            new HashSet<>(
                    Arrays.asList(8L, 9L, 10L, 11L, 12L, 13L, 14L)
            )
    );

    ServiceApiCategory(long id, Long parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getLabel() {
        return label;
    }

    public static ServiceApiCategory valueOf(long id) {
        return Stream.of(ServiceApiCategory.values())
                .filter(value -> value.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<ServiceApiCategory> getSystemOperationalCategories() {

        return Stream.of(ServiceApiCategory.values())
                .filter(category -> systemOperationalCategoryIds.contains(category.id))
                .collect(Collectors.toList());

    }

    public static List<ServiceApiCategory> getPreInstallationCategories() {
        return Stream.of(ServiceApiCategory.values())
                .filter(category -> preInstallationCategoryIds.contains(category.id))
                .collect(Collectors.toList());

    }

}