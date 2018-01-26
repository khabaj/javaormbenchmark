package com.khabaj.ormbenchmark.benchmarks.configuration;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(DataSourceConfiguration.class)
public class EntityManagerFactoryConfiguration {

    @Autowired
    DataSource dataSource;

    @Lazy
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(Integer batchSize) {

        Properties properties = new Properties();
        properties.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        //properties.put(AvailableSettings.SHOW_SQL, "true");
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, Integer.toString(batchSize));
        properties.put(AvailableSettings.ORDER_INSERTS, "true");
        properties.put(AvailableSettings.ORDER_UPDATES, "true");
        properties.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.khabaj.ormbenchmark.benchmarks.*");
        entityManagerFactory.setPersistenceUnitName("SpringData");
        entityManagerFactory.setJpaProperties(properties);
        entityManagerFactory.afterPropertiesSet();

        return entityManagerFactory.getObject();
    }
}
