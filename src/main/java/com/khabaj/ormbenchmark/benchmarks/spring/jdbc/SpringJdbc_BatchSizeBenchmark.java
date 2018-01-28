package com.khabaj.ormbenchmark.benchmarks.spring.jdbc;

import com.khabaj.ormbenchmark.benchmarks.BatchSizeBenchmark;
import com.khabaj.ormbenchmark.benchmarks.jdbc.JdbcUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpringJdbc_BatchSizeBenchmark extends SpringJdbc_Benchmark implements BatchSizeBenchmark {

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(JdbcUtils.DELETE_ALL_FROM_PHONE_TABLE_SQL);
        jdbcTemplate.update(JdbcUtils.DELETE_ALL_FROM_USER_TABLE_SQL);
    }

    @Benchmark
    @Override
    public void batchSize1_() {
        batchInsertUsers(NUMBER_OF_ENTITIES, 1);
    }

    @Benchmark
    @Override
    public void batchSize10_() {
        batchInsertUsers(NUMBER_OF_ENTITIES, 10);
    }

    @Benchmark
    @Override
    public void batchSize100_() {
        batchInsertUsers(NUMBER_OF_ENTITIES, 100);
    }

    @Benchmark
    @Override
    public void batchSize1000_() {
        batchInsertUsers(NUMBER_OF_ENTITIES, 1000);
    }

    @Benchmark
    @Override
    public void batchSize10000_() {
        batchInsertUsers(NUMBER_OF_ENTITIES, 10000);
    }

    @Benchmark
    @Override
    public void batchSize100000_() {
        batchInsertUsers(NUMBER_OF_ENTITIES, 100000);
    }
}
