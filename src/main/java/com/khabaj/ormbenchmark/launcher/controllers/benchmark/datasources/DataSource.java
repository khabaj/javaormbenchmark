package com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.khabaj.ormbenchmark.launcher.controllers.benchmark.JDBCDriver;
import javafx.beans.property.*;

public class DataSource extends RecursiveTreeObject<DataSource> {

    private StringProperty connectionName;
    private StringProperty connectionURL;
    private StringProperty username;
    private StringProperty password;
    private ObjectProperty<JDBCDriver> jdbcDriver;

    public DataSource() {
        this(null, null);
    }

    public DataSource(String connectionName, String connectionURL) {
        this(connectionName, connectionURL, null);
    }

    public DataSource(String connectionName, String connectionURL, JDBCDriver jdbcDriver) {
        this(connectionName, connectionURL, null, null, jdbcDriver);
    }

    public DataSource(String connectionName, String connectionURL, String username, String password, JDBCDriver jdbcDriver) {
        this.connectionName = new SimpleStringProperty(connectionName);
        this.connectionURL = new SimpleStringProperty(connectionURL);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.jdbcDriver = new SimpleObjectProperty<>(jdbcDriver);
    }

    public DataSource(DataSource dataSource) {
        this(dataSource.getConnectionName(), dataSource.getConnectionURL(), dataSource.getUsername(),
                dataSource.getPassword(), dataSource.getJdbcDriver());
    }

    public String getConnectionName() {
        return connectionName.get();
    }

    public void setConnectionName(String connectionName) {
        this.connectionName.set(connectionName);
    }

    public StringProperty connectionNameProperty() {
        return connectionName;
    }

    public String getConnectionURL() {
        return connectionURL.get();
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL.set(connectionURL);
    }

    public StringProperty connectionURLProperty() {
        return connectionURL;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public JDBCDriver getJdbcDriver() {
        return jdbcDriver.get();
    }

    public void setJdbcDriver(JDBCDriver jdbcDriver) {
        this.jdbcDriver.set(jdbcDriver);
    }

    public ObjectProperty<JDBCDriver> jdbcDriverProperty() {
        return jdbcDriver;
    }
}
