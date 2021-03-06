package com.khabaj.ormbenchmark.launcher.benchmark;

public enum JDBCDriver {
    MYSQL("MySQL", "com.mysql.jdbc.Driver"),
    H2("H2", "org.h2.Driver"),
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver");

    private final String name;
    private final String driver;

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
}
