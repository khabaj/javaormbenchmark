package com.khabaj.ormbenchmark.benchmarks.jdbc;

import java.sql.*;

public class JdbcUtils {

    public final static String USER_TABLE = "user_table";
    public final static String PHONE_TABLE = "phone";

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
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

    public static void clearTables(Connection connection) {
        clearTable(connection, PHONE_TABLE);
        clearTable(connection, USER_TABLE);
    }

    public static void clearTable(Connection connection, String tableName) {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM " + tableName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    public static void prepareSchema(Connection connection) throws SQLException {
        createUserTable(connection);
        createPhoneTable(connection);
    }

    public static void createUserTable(Connection connection) throws SQLException {

        PreparedStatement statement = null;
        try {
            // id is not auto incrementing
            statement = connection.prepareStatement("CREATE TABLE " + USER_TABLE + " (id integer not NULL, " +
                    " firstName VARCHAR(50), " +
                    " lastName VARCHAR(50) , " +
                    " updateDate TIMESTAMP, " +
                    "PRIMARY KEY (id))");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE INDEX lastNameIndex ON " + USER_TABLE +"(lastName)");
            statement.executeUpdate();
        } finally {
            closeStatement(statement);
        }
    }

    public static void createPhoneTable(Connection connection) throws SQLException {

        PreparedStatement statement = null;
        try {
            // id is not auto incrementing
            statement = connection.prepareStatement("CREATE TABLE " + PHONE_TABLE + " (id integer not NULL, " +
                    " phoneNumber VARCHAR(20), " +
                    " userId INTEGER , " +
                    "PRIMARY KEY (id), " +
                    "FOREIGN KEY (userId) REFERENCES user_table(id))");
            statement.executeUpdate();
        } finally {
            closeStatement(statement);
        }
    }

    public static void dropSchema(Connection connection) {
        dropTable(connection, PHONE_TABLE);
        dropTable(connection, USER_TABLE);
    }

    private static void dropTable(Connection connection, String tableName) {

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE " + tableName);
        } catch (SQLException e) {
            System.out.println("Table " + tableName + " not exists.");
        } finally {
            closeStatement(statement);
        }
    }

    public static void performBatchUsersInsert(Connection connection, int rowsNumber, int batchSize) {

        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String insertSQL = "INSERT INTO " + USER_TABLE + " (id, firstName, lastName, updateDate) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(insertSQL);

            for (int i = 1; i <=rowsNumber; i++) {

                if (i % batchSize == 0) {
                    statement.executeBatch();
                    connection.commit();
                }
                statement.setInt(1, i);
                statement.setString(2, "FirstName" + i);
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

    public static void performBatchUsersUpdate(Connection connection, int rowsToUpdate, int batchSize) {
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String updateSQL = "UPDATE " + USER_TABLE + " SET updateDate = ? WHERE id = ?";
            statement = connection.prepareStatement(updateSQL);

            for (int i = 1; i <= rowsToUpdate; i++) {
                if (i % batchSize == 0) {
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

    public static void performBatchUsersDelete(Connection connection, int rowsToDelete, int batchSize) {
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String updateSQL = "DELETE FROM " + USER_TABLE + " WHERE id = ?";
            statement = connection.prepareStatement(updateSQL);

            for (int i = 1; i <= rowsToDelete; i++) {
                if (i % batchSize == 0) {
                    statement.executeBatch();
                    connection.commit();
                }
                statement.setInt(1, i);
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

    public static void performBatchPhonesInsert(Connection connection, int parentRowsNumbers, int batchSize) {

        PreparedStatement statement = null;
        int phoneId = 1;

        try {
            connection.setAutoCommit(false);

            String insertSQL = "INSERT INTO " + PHONE_TABLE + " (id, phoneNumber, userId) VALUES (?,?,?)";
            statement = connection.prepareStatement(insertSQL);

            for (int i = 1; i <= parentRowsNumbers; i++) {
                for (int j = 1; j <= 3; j++) {
                    if (phoneId > 0 && phoneId % batchSize == 0) {
                        statement.executeBatch();
                        connection.commit();
                    }
                    statement.setInt(1, phoneId++);
                    statement.setString(2, generatePhoneNumber(String.valueOf(i),String.valueOf(j)));
                    statement.setInt(3, i);
                    statement.addBatch();
                }
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

    public static String generatePhoneNumber(String startWith, String fillWith) {

        StringBuilder phone = new StringBuilder(startWith);

        while (phone.length() <= 9) {
            phone.append(fillWith);
        }
        return phone.toString();
    }
}
