package com.my1stle.customer.portal.web.controller.monitoring;

import com.my1stle.customer.portal.service.solaredge.SiteMonitoring;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

class DashboardView {


    private EnergyGraphDataView weekEnergyByDayView;
    private EnergyGraphDataView monthEnergyByDayView;
    private EnergyGraphDataView yearEnergyByMonthView;

    private SiteMonitoring monitoring;

    DashboardView(SiteMonitoring monitoring, LocalDate weekDate, YearMonth month, Integer year) {

        this.monitoring = monitoring;

        TreeMap<LocalDate, Double> weekEnergyByDay = monitoring.getWeekEnergyByDay(weekDate);
        TreeMap<LocalDate, Double> monthEnergyByDay = monitoring.getMonthEnergyByDay(month);
        TreeMap<YearMonth, Double> yearEnergyByMonth = monitoring.getYearEnergyByMonth(year);

        weekEnergyByDayView = new EnergyGraphDataView();
        weekEnergyByDayView.setTitle("Energy By Day in Week");
        weekEnergyByDayView.setxAxisLabel("Energy");
        weekEnergyByDayView.setyAxisLabel("KWh");
        weekEnergyByDayView.setValues(weekEnergyByDay, DateTimeFormatter.ofPattern("d MMM"), 1.0 / 1000.0, ChronoUnit.WEEKS, 1);

        monthEnergyByDayView = new EnergyGraphDataView();
        monthEnergyByDayView.setTitle("Energy By Day In Month");
        monthEnergyByDayView.setxAxisLabel("Energy");
        monthEnergyByDayView.setyAxisLabel("KWh");
        monthEnergyByDayView.setValues(monthEnergyByDay, DateTimeFormatter.ofPattern("d"), 1.0 / 1000.0, ChronoUnit.MONTHS, 1);

        yearEnergyByMonthView = new EnergyGraphDataView();
        yearEnergyByMonthView.setTitle("Energy By Day In Month");
        yearEnergyByMonthView.setxAxisLabel("Energy");
        yearEnergyByMonthView.setyAxisLabel("MWh");
        yearEnergyByMonthView.setValues(yearEnergyByMonth, DateTimeFormatter.ofPattern("MMM yyyy"), 1.0 / 1000000.0, ChronoUnit.YEARS, 1);

    }


    public EnergyGraphDataView getWeekEnergyByDayViewData() {
        return weekEnergyByDayView;
    }

    public EnergyGraphDataView getMonthEnergyByDayViewData() {
        return monthEnergyByDayView;
    }

    public EnergyGraphDataView getYearEnergyByMonthViewData() {
        return yearEnergyByMonthView;
    }

    public Double getDayEnergy() {
        return this.monitoring.getLastDayEnergy();
    }

    public Double getMonthEnergy() {
        return this.monitoring.getLastMonthEnergy();
    }

    public Double getYearEnergy() {
        return this.monitoring.getLastYearEnergy();
    }

    public Double getLifeEnergy() {
        return this.monitoring.getLifeTimeEnergy();
    }

    public Double getPower() {
        return this.monitoring.getPower();
    }

    public LocalDateTime getLastUpdated() {
        return this.monitoring.getDataLastUpdatedTime();
    }

}
