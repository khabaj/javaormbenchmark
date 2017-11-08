package com.khabaj.ormbenchmark.benchmarks.spring.jdbctemplate;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.jdbc.JdbcUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

public class SpringJdbcTemplate_CreateBenchmark extends SpringJdbcTemplate_Benchmark implements CreateBenchmark {

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(JdbcUtils.DELETE_ALL_FROM_PHONE_TABLE_SQL);
        jdbcTemplate.update(JdbcUtils.DELETE_ALL_FROM_USER_TABLE_SQL);
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(JdbcUtils.INSERT_USER_SQL, 1, "FirstName1", "LastName1", new Timestamp(System.currentTimeMillis()));
    }

    @Benchmark
    @Override
    public void insert100Entities() {
        batchInsertUsers(100);
    }

    @Benchmark
    @Override
    public void insert1000Entities() {
        batchInsertUsers(1000);
    }

    @Benchmark
    @Override
    public void insert10000Entities() {
        batchInsertUsers(10000);
    }

    @Benchmark
    @Override
    public void insert100000Entities() {
        batchInsertUsers(100000);
    }
}
