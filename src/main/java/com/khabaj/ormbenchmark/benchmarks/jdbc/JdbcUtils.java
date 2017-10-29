package com.khabaj.ormbenchmark.benchmarks.jdbc;

import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {

    public static void closeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
