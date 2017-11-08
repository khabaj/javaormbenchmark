package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JDBC_CreateBenchmark extends JdbcBenchmark implements CreateBenchmark {

    @Setup
    public void setUp() {
        super.setUp();
    }

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        JdbcUtils.clearTables(connection);
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(JdbcUtils.INSERT_USER_SQL);
            statement.setInt(1, 1);
            statement.setString(2, "FirstName" + 1);
            statement.setString(3, "LastName" + 1);
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }

    @Benchmark
    @Override
    public void insert100Entities() {
        JdbcUtils.performBatchUsersInsert(connection, 100, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert1000Entities() {
        JdbcUtils.performBatchUsersInsert(connection, 1000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert10000Entities() {
        JdbcUtils.performBatchUsersInsert(connection, 10000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert100000Entities() {
        JdbcUtils.performBatchUsersInsert(connection, 100000, BATCH_SIZE);
    }
}
