package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JDBC_UpdateBenchmark extends JdbcBenchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        super.setUp();
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ROWS_IN_DB, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update1Entity() {
        try (PreparedStatement statement = connection.prepareStatement(JdbcUtils.UPDATE_USER_TABLE_SQL)) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setInt(2, 5);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @Override
    public void update10000Entities() {
        JdbcUtils.performBatchUsersUpdate(connection, 10000, BATCH_SIZE);
    }

}
