package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class JDBC_UpdateBenchmark extends JdbcBenchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        super.setUp();
        JdbcUtils.performBatchUsersInsert(connection, 100000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update1Entity() {
        JdbcUtils.performBatchUsersUpdate(connection, 1, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update100Entities() {
        JdbcUtils.performBatchUsersUpdate(connection, 100, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update1000Entities() {
        JdbcUtils.performBatchUsersUpdate(connection, 1000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update10000Entities() {
        JdbcUtils.performBatchUsersUpdate(connection, 10000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update100000Entities() {
        JdbcUtils.performBatchUsersUpdate(connection, 100000, BATCH_SIZE);
    }
}
