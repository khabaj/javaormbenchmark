package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.BatchSizeBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

public class JDBC_BatchSizeBenchmark extends JdbcBenchmark implements BatchSizeBenchmark{

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
    public void batchSize1_() {
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ENTITIES, 1);
    }

    @Benchmark
    @Override
    public void batchSize10_() {
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ENTITIES, 10);
    }

    @Benchmark
    @Override
    public void batchSize100_() {
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ENTITIES, 100);
    }

    @Benchmark
    @Override
    public void batchSize1000_() {
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ENTITIES, 1000);
    }

    @Benchmark
    @Override
    public void batchSize10000_() {
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ENTITIES, 10000);
    }

    @Benchmark
    @Override
    public void batchSize100000_() {
        JdbcUtils.performBatchUsersInsert(connection, NUMBER_OF_ENTITIES, 10000);
    }
}
