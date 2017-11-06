package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.DataSourceConfiguration;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

public abstract class JdbcBenchmark extends BaseBenchmark {

    ConfigurableApplicationContext applicationContext;
    Connection connection;

    public void setUp() {
        try {
            if (applicationContext == null || connection == null){
                this.applicationContext = new AnnotationConfigApplicationContext(DataSourceConfiguration.class);
                DataSource dataSource = applicationContext.getBean(DataSource.class);
                connection = dataSource.getConnection();

                JdbcUtils.dropSchema(connection);
                JdbcUtils.prepareSchema(connection);
            }

        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    @TearDown
    public void clear() {
        JdbcUtils.dropSchema(connection);
        JdbcUtils.closeConnection(connection);
        applicationContext.close();
    }
}
