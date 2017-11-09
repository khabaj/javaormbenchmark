package com.khabaj.ormbenchmark.benchmarks.configuration.jpa;

import com.khabaj.ormbenchmark.benchmarks.configuration.DataSourceConfiguration;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
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
@Import(DataSourceConfiguration.class)
public class JpaSpringConfiguration {

    @Autowired
    DataSource dataSource;

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
                jpaPropertyMap.put("openjpa.RuntimeUnenhancedClasses", "unsupported");
                jpaPropertyMap.put("openjpa.DynamicEnhancementAgent", "true");
                jpaPropertyMap.put("javax.persistence.schema-generation.database.action", "drop-and-create");
                jpaPropertyMap.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(foreignKeys=true,schemaAction='add')");
                jpaPropertyMap.put("openjpa.jdbc.SchemaFactory", "native(foreignKeys=true)");

                jpaPropertyMap.put("openjpa.jdbc.MappingDefaults", "ForeignKeyDeleteAction=restrict, JoinForeignKeyDeleteAction=restrict");
                break;
            default:
                jpaVendorAdapter = new HibernateJpaVendorAdapter();
        }

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPersistenceUnitName(jpaVendor.getJpaVendorName());
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.khabaj.ormbenchmark.benchmarks.*");
        entityManagerFactory.setJpaPropertyMap(jpaPropertyMap);
        entityManagerFactory.afterPropertiesSet();

        return entityManagerFactory.getObject();
    }
}
