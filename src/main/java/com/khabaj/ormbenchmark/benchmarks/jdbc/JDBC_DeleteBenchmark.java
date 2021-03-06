package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.DeleteBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC_DeleteBenchmark extends JdbcBenchmark implements DeleteBenchmark {

    int entitiesCount = 0;

    @Setup
    public void setUp() {
        super.setUp();
    }

    @Setup(Level.Invocation)
    public void populateDatabase() {
        if (entitiesCount < 1) {
            JdbcUtils.clearTables(connection);
            JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ROWS_IN_DB, BATCH_SIZE);
            entitiesCount = NUMBER_OF_ROWS_IN_DB;
        }
    }

    @Benchmark
    @Override
    public void delete1Entity() {
        PreparedStatement statement = null;
        try {
            String updateSQL = "DELETE FROM " + JdbcUtils.USER_TABLE + " WHERE id = ?";
            statement = connection.prepareStatement(updateSQL);
            statement.setInt(1, entitiesCount);
            entitiesCount--;
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }
    @Benchmark
    @Override
    public void delete10000Entities() {
        performBatchUsersDelete(connection, 10000, BATCH_SIZE);
    }

    private void performBatchUsersDelete(Connection connection, int rowsToDelete, int batchSize) {
        PreparedStatement statement = null;
        try {

            String updateSQL = "DELETE FROM " + JdbcUtils.USER_TABLE + " WHERE id = ?";
            statement = connection.prepareStatement(updateSQL);

            for (int i = 1; i <= rowsToDelete; i++) {
                if (i % batchSize == 0) {
                    statement.executeBatch();
                }
                statement.setInt(1, entitiesCount);
                statement.addBatch();
                entitiesCount--;
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }
}
