package com.khabaj.ormbenchmark.benchmarks.jdbc;

import java.sql.*;

public class JdbcUtils {

    public final static String USER_TABLE = "user_table";

    public static void closeStatement(Statement statement) {
        try {
            if(statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearUserTable(Connection connection) {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM " + USER_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    public static void createUserTable(Connection connection) throws SQLException {

        PreparedStatement statement = null;
        try {
            // id is not auto incrementing
            statement = connection.prepareStatement("CREATE TABLE " + USER_TABLE + " (id integer not NULL, " +
                    " firstName VARCHAR(50), " +
                    " lastName VARCHAR(50), " +
                    " updateDate TIMESTAMP , " +
                    "PRIMARY KEY (id))");
            statement.executeUpdate();
        } finally {
            closeStatement(statement);
        }
    }

    public static void dropUserTable(Connection connection) {

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE " + USER_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + USER_TABLE + " not exists.");
        } finally {
            closeStatement(statement);
        }
    }

    public static void performBatchInsert(Connection connection, int rowsNumber, int batchSize) {

        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String insertSQL = "INSERT INTO " + USER_TABLE + " (id, firstName, lastName, updateDate) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(insertSQL);

            for (int i = 0; i < rowsNumber; i++) {

                if (i > 0 && i % batchSize == 0) {
                    statement.executeBatch();
                    connection.commit();

                }
                statement.setInt(1, i);
                statement.setString(2, "John" + i);
                statement.setString(3, "LastName" + i);
                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    public static void performBatchUpdate(Connection connection, int rowsToUpdate, int batchSize) {
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String updateSQL = "UPDATE " + USER_TABLE + " SET updateDate = ? WHERE id = ?";
            statement = connection.prepareStatement(updateSQL);

            for (int i = 0; i < rowsToUpdate; i++) {
                if (i > 0 && i % batchSize == 0) {
                    statement.executeBatch();
                    connection.commit();

                }
                statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement.setInt(2, i);
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }
}
