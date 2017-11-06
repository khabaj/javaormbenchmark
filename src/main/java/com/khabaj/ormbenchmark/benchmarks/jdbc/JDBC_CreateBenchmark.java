package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public class JDBC_CreateBenchmark extends JdbcBenchmark implements CreateBenchmark {

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        JdbcUtils.clearTables(connection);
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        JdbcUtils.performBatchUsersInsert(connection, 1, BATCH_SIZE);
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
