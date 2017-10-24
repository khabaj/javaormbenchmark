package com.khabaj.ormbenchmark.launcher.benchmark.datasources;

import com.khabaj.ormbenchmark.launcher.benchmark.JDBCDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceService {

    private static DataSourceService instance;
    private ObservableList<DataSource> dataSources;

    private DataSourceService() {
        this.dataSources = FXCollections.observableArrayList();
    }

    public static DataSourceService getInstance() {
        if(instance == null) {
            instance = new DataSourceService();
        }
        return instance;
    }

    public ObservableList<DataSource> getDataSources() {
        return dataSources;
    }

    public void addDataSource(DataSource dataSource) {
        if(dataSource != null)
            this.dataSources.add(dataSource);
    }

    public void deleteDataSource(DataSource dataSource) {
        if(dataSource != null) {
            this.dataSources.remove(dataSource);
        }
    }

    public void saveActiveDataSourceProperties(DataSource dataSource) throws IOException {

        OutputStream out = null;

        try {
            Properties props = new Properties();
            if(!StringUtils.isEmpty(dataSource.getConnectionURL()))
                props.setProperty("datasource.url", dataSource.getConnectionURL());

            if(!StringUtils.isEmpty(dataSource.getJdbcDriver().getDriver()))
                props.setProperty("datasource.driver", dataSource.getJdbcDriver().getDriver());

            if(!StringUtils.isEmpty(dataSource.getUsername()))
                props.setProperty("datasource.username", dataSource.getUsername());

            if(!StringUtils.isEmpty(dataSource.getPassword()))
                props.setProperty("datasource.password", dataSource.getPassword());

            File file = new File("./active_datasource.properties");
            out = new FileOutputStream(file);
            props.store(out, "Active data source");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public boolean testConnection(DataSource dataSource) throws Exception {

        Connection connection = null;
        boolean connectionSuccess;
        JDBCDriver jdbcDriver = dataSource.getJdbcDriver();

        try {
            Class.forName(jdbcDriver.getDriver());
            connection = DriverManager.getConnection(dataSource.getConnectionURL(), dataSource.getUsername(), dataSource.getPassword());
            connectionSuccess = connection.isValid(1000);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return connectionSuccess;
    }
}
