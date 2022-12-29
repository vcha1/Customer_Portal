package com.my1stle.customer.portal.service.solaredge;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

class DynamoDbSiteMonitoring implements SiteMonitoring {
    private Double power;
    private Double lastDayEnergy;
    private Double lastMonthEnergy;
    private Double lastYearEnergy;
    private Double lifeTimeEnergy;
    private LocalDateTime dataLastUpdatedTime;

    private Map<LocalDate, Double> energyByDay;
    private Map<YearMonth, Double> energyByMonth;
    private Map<Integer, Double> energyByYear;


    public DynamoDbSiteMonitoring(Item overviewItem, List<Item> energyItems) {

        this.power = overviewItem.getDouble("currentPower");
        this.lastDayEnergy = overviewItem.getDouble("lastDayDataEnergy");
        this.lastMonthEnergy = overviewItem.getDouble("lastMonthDataEnergy");
        this.lastYearEnergy = overviewItem.getDouble("lastYearDataEnergy");
        this.lifeTimeEnergy = overviewItem.getDouble("lifeTimeDataEnergy");

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = overviewItem.getString("overviewLastUpdatedTime");
        this.dataLastUpdatedTime = LocalDateTime.parse(dateString, format);


        this.energyByDay = new HashMap<>();
        this.energyByMonth = new HashMap<>();
        this.energyByYear = new HashMap<>();


        for (Item d : energyItems) {

            DynamoDbEnergyData data = new DynamoDbEnergyData(d);

            LocalDate date = data.getDate();
            YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
            Integer year = date.getYear();


            Double energy = data.getEnergy();

            if (!this.energyByDay.containsKey(date))
                this.energyByDay.put(date, energy);
            else
                this.energyByDay.put(date, this.energyByDay.get(date) + energy);

            if (!this.energyByMonth.containsKey(yearMonth))
                this.energyByMonth.put(yearMonth, energy);
            else
                this.energyByMonth.put(yearMonth, this.energyByMonth.get(yearMonth) + energy);

            if (!this.energyByYear.containsKey(year))
                this.energyByYear.put(year, energy);
            else
                this.energyByYear.put(year, this.energyByYear.get(year) + energy);

        }
    }


    @Override
    public Double getPower() {
        return power;
    }

    @Override
    public Double getLastDayEnergy() {
        return lastDayEnergy;
    }

    @Override
    public Double getLastMonthEnergy() {
        return lastMonthEnergy;
    }

    @Override
    public Double getLastYearEnergy() {
        return lastYearEnergy;
    }

    @Override
    public LocalDateTime getDataLastUpdatedTime() {
        return dataLastUpdatedTime;
    }

    public Double getLifeTimeEnergy() {
        return lifeTimeEnergy;
    }


    @Override
    public TreeMap<LocalDate, Double> getWeekEnergyByDay(LocalDate date) {

        final TreeMap<LocalDate, Double> weekData = new TreeMap<>();
        final Integer weekLength = 7;

        TemporalField fieldISO = WeekFields.of(Locale.US).dayOfWeek();
        LocalDate currentDay = date.with(fieldISO, 1);
        for (Integer i = 0; i < weekLength; i++) {
            Double energy = this.energyByDay.get(currentDay);
            if (null != energy)
                weekData.put(currentDay, energy);
            else
                weekData.put(currentDay, 0.0);

            currentDay = currentDay.plusDays(1);
        }

        return weekData;

    }

    @Override
    public TreeMap<YearMonth, Double> getYearEnergyByMonth(Integer year) {

        TreeMap<YearMonth, Double> data = new TreeMap<>();

        YearMonth currentMonth = YearMonth.of(year, 1);
        while (currentMonth.getYear() == year) {
            Double energy = this.energyByMonth.get(currentMonth);

            if (null != energy)
                data.put(currentMonth, energy);
            else
                data.put(currentMonth, 0.0);
            currentMonth = currentMonth.plusMonths(1);
        }

        return data;
    }

    @Override
    public Double getYearEnergyTotal(Integer year) {
        return this.energyByYear.get(year);
    }

    @Override
    public TreeMap<LocalDate, Double> getMonthEnergyByDay(YearMonth month) {

        TreeMap<LocalDate, Double> data = new TreeMap<>();

        for (int i = 1; i <= month.lengthOfMonth(); i++) {

            LocalDate date = LocalDate.of(month.getYear(), month.getMonthValue(), i);
            Double energy = this.energyByDay.get(date);

            if (null != energy)
                data.put(date, energy);
            else
                data.put(date, 0.0);
        }

        return data;

    }
}
