package com.khabaj.ormbenchmark.benchmarks.jdbc;

import com.khabaj.ormbenchmark.benchmarks.DeleteBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class JDBC_DeleteBenchmark extends JdbcBenchmark implements DeleteBenchmark {

    int entitiesCount = 0;

    @Setup(Level.Invocation)
    public void populateDatabase() {
        if (entitiesCount < 1) {
            JdbcUtils.clearUserTable(connection);
            JdbcUtils.performBatchInsert(connection, 100000, BATCH_SIZE);
            entitiesCount = 100000;
        }
    }

    @Benchmark
    @Override
    public void delete1Entity() {
        JdbcUtils.performBatchDelete(connection, 1, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void delete100Entities() {
        JdbcUtils.performBatchDelete(connection, 100, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void delete1000Entities() {
        JdbcUtils.performBatchDelete(connection, 1000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void delete10000Entities() {
        JdbcUtils.performBatchDelete(connection, 10000, BATCH_SIZE);
    }

    @Benchmark
    @Override
    public void delete100000Entities() {
        JdbcUtils.performBatchDelete(connection, 100000, BATCH_SIZE);
    }
}
