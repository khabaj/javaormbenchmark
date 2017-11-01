package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class JDBC_UpdateBenchmark extends JdbcBenchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        if (applicationContext == null || connection == null){
            setUp();
        }
        JdbcUtils.performBatchInsert(connection, 100000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update1Entity() {
        JdbcUtils.performBatchUpdate(connection, 1, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update100Entities() {
        JdbcUtils.performBatchUpdate(connection, 100, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update1000Entities() {
        JdbcUtils.performBatchUpdate(connection, 1000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update10000Entities() {
        JdbcUtils.performBatchUpdate(connection, 10000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void update100000Entities() {
        JdbcUtils.performBatchUpdate(connection, 100000, BATCH_SIZE);
    }
}
