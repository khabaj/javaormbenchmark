package com.khabaj.ormbenchmark.benchmarks.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("file:active_datasource.properties")
public class DataSourceConfiguration {

    @Value("${datasource.url:}")
    private String dbUrl;

    @Value("${datasource.driver:}")
    private String jdbcDriver;

    @Value("${datasource.username:}")
    private String dbUserName;

    @Value("${datasource.password:}")
    private String dbPassword;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUsername(dbUserName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
}
