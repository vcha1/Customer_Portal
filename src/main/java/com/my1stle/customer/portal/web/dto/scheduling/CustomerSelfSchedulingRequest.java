package com.my1stle.customer.portal.web.dto.scheduling;

import com.my1stle.customer.portal.service.scheduling.CustomerSelfSchedulingRole;

import java.util.List;

public interface CustomerSelfSchedulingRequest {

    boolean isBatteryInstall();

    Long getDuration();

    CustomerSelfSchedulingRole getRole();

    String getDirectSalesCustomerId();

    String getInstallationId();

    String getOpportunityId();

    List<Long> getSkillIds();

    String getClientId();

}