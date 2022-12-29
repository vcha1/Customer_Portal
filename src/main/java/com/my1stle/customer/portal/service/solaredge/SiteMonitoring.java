package com.my1stle.customer.portal.service.solaredge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.TreeMap;

public interface SiteMonitoring {
    Double getPower();

    Double getLastDayEnergy();

    Double getLastMonthEnergy();

    Double getLastYearEnergy();

    LocalDateTime getDataLastUpdatedTime();

    Double getLifeTimeEnergy();

    TreeMap<LocalDate, Double> getWeekEnergyByDay(LocalDate date);

    TreeMap<YearMonth, Double> getYearEnergyByMonth(Integer year);

    Double getYearEnergyTotal(Integer year);

    TreeMap<LocalDate, Double> getMonthEnergyByDay(YearMonth month);


}
