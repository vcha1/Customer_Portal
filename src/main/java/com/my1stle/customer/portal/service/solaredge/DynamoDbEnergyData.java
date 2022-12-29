package com.my1stle.customer.portal.service.solaredge;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class DynamoDbEnergyData {
    private LocalDate date;
    private Double energy;

    public DynamoDbEnergyData(Item d) {

        String unit = d.getString("timeUnit");
        if (!unit.equals("DAY"))
            throw new RuntimeException("Unexpected unit!");

        String dateString = d.getString("date");
        this.date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        this.energy = d.getDouble("value");

    }

    public LocalDate getDate() {
        return date;
    }

    public Double getEnergy() {
        return energy;
    }
}
