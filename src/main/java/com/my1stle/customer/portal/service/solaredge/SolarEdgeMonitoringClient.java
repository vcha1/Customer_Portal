package com.my1stle.customer.portal.service.solaredge;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class SolarEdgeMonitoringClient {

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private DynamoDB dynamoDB = new DynamoDB(client);


    public Optional<SiteMonitoring> getSiteMonitoring(Integer siteId) {


        List<Item> siteOverviewItems = this.getOverviewItems(siteId);

        if (siteOverviewItems.size() < 1)
            return Optional.empty();


        if (siteOverviewItems.size() != 1)
            throw new RuntimeException("Unexpected overview: " + siteOverviewItems.size());


        List<Item> energyItems = this.getEnergyItems(siteId);

        SiteMonitoring monitoring = new DynamoDbSiteMonitoring(siteOverviewItems.get(0), energyItems);

        return Optional.of(monitoring);

    }

    private List<Item> getEnergyItems(Integer siteId) {
        Table table = this.dynamoDB.getTable("SOLAR_EDGE_ENERGY_ALL");

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("site_id = :v_id")
                .withValueMap(new ValueMap()
                        .withInt(":v_id", siteId)
                );

        ItemCollection<QueryOutcome> result = table.query(spec);

        List<Item> items = new ArrayList<>();
        result.iterator().forEachRemaining(items::add);

        return items;

    }

    private List<Item> getOverviewItems(Integer siteId) {
        Table siteOverviewTable = dynamoDB.getTable("SOLAR_EDGE_SITE_OVERVIEW");
        QuerySpec siteOverviewSpec = new QuerySpec()
                .withKeyConditionExpression("site_id = :v_id")
                .withValueMap(new ValueMap()
                        .withInt(":v_id", siteId)
                );
        ItemCollection<QueryOutcome> siteOverViewItems = siteOverviewTable.query(siteOverviewSpec);

        Iterator<Item> overviewIterator = siteOverViewItems.iterator();
        List<Item> overviewItems = new ArrayList<>();
        overviewIterator.forEachRemaining(overviewItems::add);

        return overviewItems;

    }

}


