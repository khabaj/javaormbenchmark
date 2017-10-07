package com.khabaj.ormbenchmark.launcher.controllers.benchmark;

public enum JDBCDriver {
    MYSQL("MySQL", "com.mysql.jdbc.Driver"),
    H2("H2", "org.h2.Driver");

    private String name;
    private String driver;

    JDBCDriver(String name, String driver) {
        this.name = name;
        this.driver = driver;
    }

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getFullText() {
        return name + " - " + driver;
    }

}
