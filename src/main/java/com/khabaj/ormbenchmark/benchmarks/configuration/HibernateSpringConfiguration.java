package com.khabaj.ormbenchmark.benchmarks.configuration;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(DataSourceConfiguration.class)
public class HibernateSpringConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.khabaj.ormbenchmark.benchmarks.");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        //properties.put(AvailableSettings.SHOW_SQL, "true");
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, "100");
        properties.put(AvailableSettings.ORDER_INSERTS, "true");
        properties.put(AvailableSettings.ORDER_UPDATES, "true");
        properties.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
        return properties;
    }
}

