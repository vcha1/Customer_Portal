package com.my1stle.customer.portal.service.installation.configuration.truckroll;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.TruckRollClient;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.credentials.DefaultTruckRollClientCredentials;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.Auditor;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;
import java.util.function.Supplier;

@Configuration
public class TruckRollClientConfiguration {

    @Bean
    @Primary
    @Autowired
    public TruckRollClient truckRollClient(com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.credentials.TruckRollClientConfiguration credentials) {
        return new TruckRollClient(credentials);
    }

    @Bean
    @Primary
    @Autowired
    public com.dev1stle.scheduling.system.v1.service.truck.roll.api.client.credentials.TruckRollClientConfiguration truckRollClientCredentials(
            @Value("${1le.salesforce.username}") String salesforceUsername,
            @Value("${1le.salesforce.password}") String salesforcePassword,
            @Value("${1le.salesforce.auth.endpoint}") String salesforceEndpoint,

            @Value("${truckroll_system.jdbc.username}") String truckRollDatabaseUsername,
            @Value("${truckroll_system.jdbc.password}") String truckRollDatabasePassword,
            @Value("${truckroll_system.jdbc.url}") String truckRollDatabaseUrl,
            @Value("${truckroll_system.jdbc.driver}") String truckRollDatabaseDriver,
            @Value("${truckroll_system.jpa.dialect}") String truckRollDatabaseDialect,
            @Value("${truckroll_system.jpa.hbm2ddl.auto}") String truckRollDatabaseHbm2dllAuto,

            @Value("${calendar_system.jdbc.username}") String calendarDatabaseUsername,
            @Value("${calendar_system.jdbc.password}") String calendarDatabasePassword,
            @Value("${calendar_system.jdbc.url}") String calendarDatabaseUrl,
            @Value("${calendar_system.jdbc.driver}") String calendarDatabaseDriver,
            @Value("${calendar_system.jpa.dialect}") String calendarDatabaseDialect,
            @Value("${calendar_system.jpa.hbm2ddl.auto}") String calendarDatabaseHbm2dllAuto,

            @Value("${mapquest.api.key}") String mapQuestApiKey,
            Supplier<Auditor> auditorSupplier
    ) {


        DefaultTruckRollClientCredentials configuration = new DefaultTruckRollClientCredentials();

        configuration.setSalesforceUsername(salesforceUsername);
        configuration.setSalesforcePassword(salesforcePassword);
        configuration.setSalesforceAuthEndpoint(salesforceEndpoint);

        configuration.setMapQuestApiKey(mapQuestApiKey);

        Properties calendarSystemHibernateProperties = new Properties();
        configuration.setCalendarSystemHibernateProperties(calendarSystemHibernateProperties);
        calendarSystemHibernateProperties.setProperty(Environment.USER, calendarDatabaseUsername);
        calendarSystemHibernateProperties.setProperty(Environment.PASS, calendarDatabasePassword);
        calendarSystemHibernateProperties.setProperty(Environment.URL, calendarDatabaseUrl);
        calendarSystemHibernateProperties.setProperty(Environment.DRIVER, calendarDatabaseDriver);
        calendarSystemHibernateProperties.setProperty(Environment.DIALECT, calendarDatabaseDialect);
        calendarSystemHibernateProperties.setProperty(Environment.HBM2DDL_AUTO, calendarDatabaseHbm2dllAuto);
        calendarSystemHibernateProperties.put("hibernate.c3p0.idle_test_period", 3600);



        Properties truckRollSystemHibernateProperties = new Properties();
        configuration.setTruckRollHibernateProperties(truckRollSystemHibernateProperties);
        truckRollSystemHibernateProperties.setProperty(Environment.USER, truckRollDatabaseUsername);
        truckRollSystemHibernateProperties.setProperty(Environment.PASS, truckRollDatabasePassword);
        truckRollSystemHibernateProperties.setProperty(Environment.URL, truckRollDatabaseUrl);
        truckRollSystemHibernateProperties.setProperty(Environment.DRIVER, truckRollDatabaseDriver);
        truckRollSystemHibernateProperties.setProperty(Environment.DIALECT, truckRollDatabaseDialect);
        truckRollSystemHibernateProperties.setProperty(Environment.HBM2DDL_AUTO, truckRollDatabaseHbm2dllAuto);
        truckRollSystemHibernateProperties.put("hibernate.c3p0.idle_test_period", 3600);

        configuration.setAuditorSupplier(auditorSupplier);

        return configuration;
    }

}
