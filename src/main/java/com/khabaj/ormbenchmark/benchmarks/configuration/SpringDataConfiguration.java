package com.khabaj.ormbenchmark.benchmarks.configuration;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(DataSourceConfiguration.class)
@EnableJpaRepositories(basePackages = {
        "com.khabaj.ormbenchmark.benchmarks.spring.springdata.repositories"
})
@EnableTransactionManagement
public class SpringDataConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        Properties properties = new Properties();
        properties.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, "1000");
        properties.put(AvailableSettings.ORDER_INSERTS, "true");
        properties.put(AvailableSettings.ORDER_UPDATES, "true");

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.khabaj.ormbenchmark.benchmarks.*");
        entityManagerFactory.setPersistenceUnitName("SpringData");
        entityManagerFactory.setJpaProperties(properties);
        entityManagerFactory.afterPropertiesSet();

        return entityManagerFactory.getObject();
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
