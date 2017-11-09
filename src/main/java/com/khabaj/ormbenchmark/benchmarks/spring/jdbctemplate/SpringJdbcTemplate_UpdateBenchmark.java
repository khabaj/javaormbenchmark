package com.khabaj.ormbenchmark.benchmarks.spring.jdbctemplate;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.jdbc.JdbcUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Timestamp;
import java.sql.Types;

public class SpringJdbcTemplate_UpdateBenchmark extends SpringJdbcTemplate_Benchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        super.setUp();
        batchInsertUsers(NUMBER_OF_ROWS_IN_DB);
    }

    @Benchmark
    @Override
    public void update1Entity() {
        batchUpdateUsers(1);
    }

    @Benchmark
    @Override
    public void update100Entities() {
        batchUpdateUsers(100);
    }

    @Benchmark
    @Override
    public void update1000Entities() {
        batchUpdateUsers(1000);
    }

    @Benchmark
    @Override
    public void update10000Entities() {
        batchUpdateUsers(10000);
    }

    private void batchUpdateUsers(int rowsNumber) {

        BatchSqlUpdate batchUpdate = new BatchSqlUpdate();
        batchUpdate.setDataSource(dataSource);
        batchUpdate.setSql("UPDATE " + JdbcUtils.USER_TABLE + " SET updateDate = ? WHERE id = ?");
        batchUpdate.declareParameter(new SqlParameterValue(Types.TIMESTAMP, "updateDate"));
        batchUpdate.declareParameter(new SqlParameterValue(Types.INTEGER, "id"));
        batchUpdate.setBatchSize(BATCH_SIZE);

        for (int i = 1; i <= rowsNumber; i++) {
            batchUpdate.update(new Timestamp(System.currentTimeMillis()), i);
        }
        batchUpdate.flush();
        batchUpdate.reset();

    }
}
