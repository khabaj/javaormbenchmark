package com.khabaj.ormbenchmark.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
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

        Map<String, Object> jpaPropertyMap = new HashMap();
        jpaPropertyMap.put("javax.persistence.schema-generation.database.action", "drop-and-create");

        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                getEntityManagerFactory(hibernateJpaVendorAdapter, "Hibernate_JPA", jpaPropertyMap);
        return entityManagerFactory.getObject();
    }

    @Lazy
    @Bean(name = "eclipseLinkEntityManagerFactory")
    public EntityManagerFactory eclipseLinkEntityManagerFactory() {
        EclipseLinkJpaVendorAdapter eclipseLinkJpaVendorAdapter = new EclipseLinkJpaVendorAdapter();

        Map<String, Object> jpaPropertyMap = new HashMap();
        jpaPropertyMap.put(PersistenceUnitProperties.WEAVING, "false");
        jpaPropertyMap.put("javax.persistence.schema-generation.database.action", "drop-and-create");

        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                getEntityManagerFactory(eclipseLinkJpaVendorAdapter, "EclipseLink_JPA", jpaPropertyMap);

        return entityManagerFactory.getObject();
    }

    @Lazy
    @Bean(name = "openJpaEntityManagerFactory")
    public EntityManagerFactory openJpaEntityManagerFactory() {
        OpenJpaVendorAdapter openJpaVendorAdapter = new OpenJpaVendorAdapter();

        Map<String, Object> jpaPropertyMap = new HashMap();
        jpaPropertyMap.put("openjpa.RuntimeUnenhancedClasses", "supported");
        jpaPropertyMap.put("javax.persistence.schema-generation.database.action", "drop-and-create");
        jpaPropertyMap.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(SchemaAction='dropDB,add')");

        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                getEntityManagerFactory(openJpaVendorAdapter, "OpenJPA", jpaPropertyMap);
        return entityManagerFactory.getObject();
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
