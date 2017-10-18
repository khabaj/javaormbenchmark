package com.khabaj.ormbenchmark.launcher.benchmark.datasources;

import com.khabaj.ormbenchmark.launcher.benchmark.JDBCDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceService {

    private static DataSourceService instance;
    private ObservableList<DataSource> dataSources;
    private DataSource activeDataSource;

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

    public DataSource getActiveDataSource() {
        return activeDataSource;
    }

    public void setActiveDataSource(DataSource activeDataSource) {
        this.activeDataSource = activeDataSource;
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
