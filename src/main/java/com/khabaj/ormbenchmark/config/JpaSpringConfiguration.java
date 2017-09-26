package com.khabaj.ormbenchmark.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:data_source.properties")
public class JpaSpringConfiguration {

    @Value("${datasource.url:jdbc:h2:mem:ormbenchmarkdb}")
    private String dbUrl;

    @Value("${datasource.driver:org.h2.Driver}")
    private String jdbcDriver;

    @Value("${datasource.username:root}")
    private String dbUserName;

    @Value("${datasource.password:}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUsername(dbUserName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Primary
    @Lazy
    @Bean(name = "hibernateEntityManagerFactory")
    public EntityManagerFactory hibernateEntityManagerFactory() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                getEntityManagerFactory(hibernateJpaVendorAdapter, "Hibernate_JPA");
        return entityManagerFactory.getObject();
    }

    @Lazy
    @Bean
    public EntityManagerFactory eclipseLinkEntityManagerFactory() {
        EclipseLinkJpaVendorAdapter eclipseLinkJpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        eclipseLinkJpaVendorAdapter.setGenerateDdl(true);

        Map<String, Object> jpaPropertyMap = new HashMap();
        jpaPropertyMap.put(PersistenceUnitProperties.WEAVING, "false");

        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                getEntityManagerFactory(eclipseLinkJpaVendorAdapter, "EclipseLink_JPA", jpaPropertyMap);

        return entityManagerFactory.getObject();
    }

    private LocalContainerEntityManagerFactoryBean getEntityManagerFactory(JpaVendorAdapter jpaVendorAdapter, String persistenceUnitName) {
        return getEntityManagerFactory(jpaVendorAdapter, persistenceUnitName, null);
    }

    private LocalContainerEntityManagerFactoryBean getEntityManagerFactory(JpaVendorAdapter jpaVendorAdapter,
                                                                           String persistenceUnitName, Map<String, ?> jpaPropertyMap) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPersistenceUnitName(persistenceUnitName);
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.khabaj.ormbenchmark.*");
        entityManagerFactory.setJpaPropertyMap(jpaPropertyMap);
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory;
    }
}
