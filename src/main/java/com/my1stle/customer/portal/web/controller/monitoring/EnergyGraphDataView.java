package com.my1stle.customer.portal.web.controller.monitoring;

import com.google.gson.Gson;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;

class EnergyGraphDataView {
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private Map<String, Double> dataValues;

    private Temporal currentTemporal;
    private Temporal previousTemporal;
    private Temporal nextTemporal;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public <T extends Temporal> void setValues(TreeMap<T, Double> values, DateTimeFormatter keyFormatter, Double dataMultiple, ChronoUnit chronoUnit, Integer chronoUnitAmount) {

        this.dataValues = new LinkedHashMap<>();

        for (T temporal : values.keySet()) {
            this.dataValues.put(keyFormatter.format(temporal), values.get(temporal) * dataMultiple);
        }

        this.currentTemporal = values.keySet().iterator().next();
        this.nextTemporal = this.currentTemporal.plus(chronoUnitAmount, chronoUnit);
        this.previousTemporal = this.currentTemporal.minus(chronoUnitAmount, chronoUnit);

    }


    public String getDataTableJson() {
        List<List<Object>> data = new ArrayList<>();

        List<Object> labels = new ArrayList<>();
        labels.add(this.xAxisLabel);
        labels.add(this.yAxisLabel);
        data.add(labels);

        for (String k : this.dataValues.keySet()) {
            List<Object> pair = new ArrayList<>();
            pair.add(k);
            pair.add(this.dataValues.get(k));
            data.add(pair);
        }

        return new Gson().toJson(data);
    }

    public String getOptionsJson() {

        Map<String, Object> options = new HashMap<>();
        options.put("title", this.title);

        return new Gson().toJson(options);
    }

    public Temporal getCurrentTemporal() {
        return currentTemporal;
    }

    public Temporal getPreviousTemporal() {
        return previousTemporal;
    }

    public Temporal getNextTemporal() {
        return nextTemporal;
    }
}
