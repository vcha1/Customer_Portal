package com.my1stle.customer.portal.web.controller.monitoring;


import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.solaredge.SiteMonitoring;
import com.my1stle.customer.portal.service.solaredge.SolarEdgeMonitoringClient;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/installation/{installationId}/monitoring")
public class MonitoringDashboardController {


    private InstallationService installationService;

    @Autowired
    public MonitoringDashboardController(InstallationService installationService) {
        this.installationService = installationService;
    }

    @GetMapping("/dashboard")
    public String dashboardAction(Model model,
                                  @PathVariable("installationId") String installationId,
                                  @RequestParam(value = "week", required = false) String weekParam,
                                  @RequestParam(value = "month", required = false) String monthParam,
                                  @RequestParam(value = "year", required = false) Integer yearParam) {


        Installation installation = this.installationService.getInstallationById(installationId);

        if (null == installation)
            throw new ResourceNotFoundException("Installation not found!");

        if (StringUtils.isBlank(installation.getMonitoringExternalId())){
            model.addAttribute("installation", installation);
            return "monitoring/unavailable";
        }

        Integer monitoringExternalId = Integer.valueOf(installation.getMonitoringExternalId());
        SolarEdgeMonitoringClient client = new SolarEdgeMonitoringClient();
        Optional<SiteMonitoring> monitoring = client.getSiteMonitoring(monitoringExternalId);

        if (!monitoring.isPresent()) {
            model.addAttribute("installation", installation);
            return "monitoring/unavailable";
        }

        LocalDate today = LocalDate.now();
        LocalDate week = null == weekParam ? today : LocalDate.parse(weekParam, DateTimeFormatter.ofPattern("MMddyyyy"));
        YearMonth month = null == monthParam ? YearMonth.of(today.getYear(), today.getMonthValue()) : YearMonth.parse(monthParam, DateTimeFormatter.ofPattern("MMyyyy"));
        Integer year = null == yearParam ? today.getYear() : yearParam;


        DashboardView viewData = new DashboardView(monitoring.get(), week, month, year);

        model.addAttribute("monitoringData", viewData);

        return "monitoring/dashboard.html";
    }


}




