package com.khabaj;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.khabaj.*")
public class JpaConfiguration {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("mytestdb")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    @Primary
    @Scope("prototype")
    public EntityManagerFactory entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(getJpaVendorAdapter());
        entityManagerFactory.setPersistenceUnitName("EclipseLink_JPA");
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.khabaj.*");
        entityManagerFactory.setJpaPropertyMap(getJPAProperties());
        //entityManagerFactory.setPersistenceProvider(newPersistenceProvider());
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();

    }

    public JpaVendorAdapter getJpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        //vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setShowSql(false);

        return vendorAdapter;
    }

    public Map getJPAProperties() {
        Map<String, Object> prop = new HashMap();
        prop.put(PersistenceUnitProperties.WEAVING, "false");
        return prop;
    }

}
