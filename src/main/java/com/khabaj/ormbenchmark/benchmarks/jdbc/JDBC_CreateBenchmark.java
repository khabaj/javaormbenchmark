package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.DataSourceConfiguration;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC_CreateBenchmark extends BaseBenchmark implements CreateBenchmark {

    ConfigurableApplicationContext applicationContext;
    Connection connection;

    @Setup
    public void setUp() {
        try {
            this.applicationContext = new AnnotationConfigApplicationContext(DataSourceConfiguration.class);
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            connection = dataSource.getConnection();

            initializeDatabase();

        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    private void initializeDatabase() {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("CREATE TABLE USER (id integer not NULL, " +
                    " firstName VARCHAR, " +
                    " lastName VARCHAR, " +
                    "PRIMARY KEY (id)");
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }

    @Override
    public void insert1Entity() {

    }

    @Override
    public void insert100Entities() {

    }

    @Override
    public void insert10000Entities() {

    }

    @Override
    public void insert100000Entities() {

    }
}
