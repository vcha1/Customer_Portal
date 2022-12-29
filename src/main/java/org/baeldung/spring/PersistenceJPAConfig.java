package org.baeldung.spring;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.my1stle.customer.portal", "org.baeldung"})
@EntityScan(basePackages = {"com.my1stle.customer.portal", "org.baeldung"})
public class PersistenceJPAConfig {

    @Value("${customer_portal.jdbc.driver}")
    String customerPortalJdbcDriver;
    @Value("${customer_portal.jdbc.url}")
    String customerPortalDatabaseUrl;
    @Value("${customer_portal.jdbc.username}")
    String customerPortalDatabaseUsername;
    @Value("${customer_portal.jdbc.password}")
    String customerPortalDatabasePassword;
    @Value("${customer_portal.jpa.hbm2ddl.auto}")
    String customerPortalJpaHbm2dllAuto;
    @Value("${customer_portal.jpa.dialect}")
    String customerPortalJpaDialect;


    public PersistenceJPAConfig() {
        super();
    }

    //

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.baeldung.persistence.model", "com.my1stle.customer.portal.persistence.model");
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(customerPortalJdbcDriver);
        dataSource.setUrl(customerPortalDatabaseUrl);
        dataSource.setUsername(customerPortalDatabaseUsername);
        dataSource.setPassword(customerPortalDatabasePassword);
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    protected Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(Environment.HBM2DDL_AUTO, customerPortalJpaHbm2dllAuto);
        hibernateProperties.setProperty(Environment.DIALECT, customerPortalJpaDialect);
        return hibernateProperties;
    }

}
