package com.khabaj.ormbenchmark.benchmarks.spring.jdbctemplate;

import com.khabaj.ormbenchmark.benchmarks.DeleteBenchmark;
import com.khabaj.ormbenchmark.benchmarks.jdbc.JdbcUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;

public class SpringJdbcTemplate_DeleteBenchmark extends SpringJdbcTemplate_Benchmark implements DeleteBenchmark {
    int entitiesCount = 0;

    @Setup
    public void setUp() {
        super.setUp();
    }

    @Setup(Level.Invocation)
    public void populateDatabase() {
        if (entitiesCount < 1) {
            batchInsertUsers(100000);
            entitiesCount = 100000;
        }
    }

    @Benchmark
    @Override
    public void delete1Entity() {
        batchDeleteUsers(1);
    }

    @Benchmark
    @Override
    public void delete100Entities() {
        batchDeleteUsers(100);
    }

    @Benchmark
    @Override
    public void delete1000Entities() {
        batchDeleteUsers(1000);
    }

    @Benchmark
    @Override
    public void delete10000Entities() {
        batchDeleteUsers(10000);
    }

    @Benchmark
    @Override
    public void delete100000Entities() {
        batchDeleteUsers(100000);
    }

    private void batchDeleteUsers(int rowsNumber) {

        BatchSqlUpdate batchUpdate = new BatchSqlUpdate();
        batchUpdate.setDataSource(dataSource);
        batchUpdate.setSql("DELETE FROM " + JdbcUtils.USER_TABLE + " WHERE id = ?");
        batchUpdate.declareParameter(new SqlParameterValue(Types.INTEGER, "id"));
        batchUpdate.setBatchSize(BATCH_SIZE);

        for (int i = 1; i <= rowsNumber; i++) {
            batchUpdate.update(entitiesCount);
            entitiesCount--;
        }
        batchUpdate.flush();
        batchUpdate.reset();
    }
}
