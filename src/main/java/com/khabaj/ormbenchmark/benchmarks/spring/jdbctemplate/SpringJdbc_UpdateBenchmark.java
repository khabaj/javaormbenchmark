package com.khabaj.ormbenchmark.benchmarks.spring.jdbctemplate;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.jdbc.JdbcUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Timestamp;
import java.sql.Types;

public class SpringJdbc_UpdateBenchmark extends SpringJdbc_Benchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        super.setUp();
        batchInsertUsers(NUMBER_OF_ROWS_IN_DB);
    }

    @Benchmark
    @Override
    public void update1Entity() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(JdbcUtils.UPDATE_USER_TABLE_SQL, new Timestamp(System.currentTimeMillis()), 5);
    }

    @Benchmark
    @Override
    public void update10000Entities() {
        batchUpdateUsers(10000);
    }

    private void batchUpdateUsers(int rowsNumber) {


        BatchSqlUpdate batchUpdate = new BatchSqlUpdate();
        batchUpdate.setDataSource(dataSource);
        batchUpdate.setSql(JdbcUtils.UPDATE_USER_TABLE_SQL);
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
