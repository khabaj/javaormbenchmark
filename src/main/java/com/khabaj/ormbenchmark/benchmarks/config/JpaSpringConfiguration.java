package com.khabaj.ormbenchmark.benchmarks.config;

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

    @Lazy
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(JpaVendor jpaVendor) {

        JpaVendorAdapter jpaVendorAdapter;
        Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("javax.persistence.schema-generation.database.action", "drop-and-create");

        switch (jpaVendor) {
            case HIBERNATE:
                jpaVendorAdapter = new HibernateJpaVendorAdapter();
                break;
            case ECLIPSE_LINK:
                jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
                jpaPropertyMap.put(PersistenceUnitProperties.WEAVING, "false");
                break;
            case OPENJPA:
                jpaVendorAdapter = new OpenJpaVendorAdapter();
                jpaPropertyMap.put("openjpa.RuntimeUnenhancedClasses", "supported");
                jpaPropertyMap.put("javax.persistence.schema-generation.database.action", "drop-and-create");
                jpaPropertyMap.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(SchemaAction='dropDB,add')");
                break;
            default:
                jpaVendorAdapter = new HibernateJpaVendorAdapter();
        }

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPersistenceUnitName(jpaVendor.getJpaVendorName());
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.khabaj.ormbenchmark.*");
        entityManagerFactory.setJpaPropertyMap(jpaPropertyMap);
        entityManagerFactory.afterPropertiesSet();

        return entityManagerFactory.getObject();
    }
}
