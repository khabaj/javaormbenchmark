package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public class JDBC_CreateBenchmark extends JdbcBenchmark implements CreateBenchmark {

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        JdbcUtils.clearUserTable(connection);
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        JdbcUtils.performBatchInsert(connection, 1, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert100Entities() {
        JdbcUtils.performBatchInsert(connection, 100, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert1000Entities() {
        JdbcUtils.performBatchInsert(connection, 1000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert10000Entities() {
        JdbcUtils.performBatchInsert(connection, 10000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void insert100000Entities() {
        JdbcUtils.performBatchInsert(connection, 100000, BATCH_SIZE);
    }
}
