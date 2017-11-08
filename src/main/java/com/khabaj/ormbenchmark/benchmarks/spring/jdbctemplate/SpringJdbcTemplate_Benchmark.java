package com.khabaj.ormbenchmark.benchmarks.spring.jdbctemplate;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.DataSourceConfiguration;
import com.khabaj.ormbenchmark.benchmarks.jdbc.JdbcUtils;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.object.BatchSqlUpdate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.sql.Types;

public abstract class SpringJdbcTemplate_Benchmark extends BaseBenchmark {

    ConfigurableApplicationContext applicationContext;
    DataSource dataSource;

    @Setup
    public void setUp() {
        try {
            this.applicationContext = new AnnotationConfigApplicationContext(DataSourceConfiguration.class);
            this.dataSource = applicationContext.getBean(DataSource.class);

            prepareSchema();

        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    private void prepareSchema() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.execute(JdbcUtils.DROP_PHONE_TABLE_SQL);
            jdbcTemplate.execute(JdbcUtils.DROP_USER_TABLE_SQL);
        } catch (RuntimeException ignored) {
        }
        jdbcTemplate.execute(JdbcUtils.CREATE_USER_TABLE_SQL);
        jdbcTemplate.execute(JdbcUtils.CREATE_INDEX_SQL);
        jdbcTemplate.execute(JdbcUtils.CREATE_PHONE_TABLE_SQL);
    }

    @TearDown
    public void clear() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(JdbcUtils.DROP_PHONE_TABLE_SQL);
        jdbcTemplate.execute(JdbcUtils.DROP_USER_TABLE_SQL);
        applicationContext.close();
    }

    protected void batchInsertUsers(int rowsNumber) {

        BatchSqlUpdate batch = new BatchSqlUpdate();
        batch.setDataSource(dataSource);
        batch.setSql(JdbcUtils.INSERT_USER_SQL);
        batch.declareParameter(new SqlParameterValue(Types.INTEGER, "id"));
        batch.declareParameter(new SqlParameterValue(Types.VARCHAR, "firstName"));
        batch.declareParameter(new SqlParameterValue(Types.VARCHAR, "lastDate"));
        batch.declareParameter(new SqlParameterValue(Types.TIMESTAMP, "updateDate"));
        batch.setBatchSize(BATCH_SIZE);

        for (int i = 1; i <= rowsNumber; i++) {
            batch.update(i, "FirstName" + i, "LastName" + i, new Timestamp(System.currentTimeMillis()));
        }
        batch.flush();
        batch.reset();
    }

    protected void batchInsertPhones(int parentRowsNumbers) {

        BatchSqlUpdate batch = new BatchSqlUpdate();
        batch.setDataSource(dataSource);
        batch.setSql(JdbcUtils.INSERT_PHONE_SQL);
        batch.declareParameter(new SqlParameterValue(Types.INTEGER, "id"));
        batch.declareParameter(new SqlParameterValue(Types.VARCHAR, "phoneNumber"));
        batch.declareParameter(new SqlParameterValue(Types.INTEGER, "userId"));
        batch.setBatchSize(BATCH_SIZE);

        int phoneId = 1;

        for (int i = 1; i <= parentRowsNumbers; i++) {
            for (int j = 1; j <= 3; j++) {
                batch.update(phoneId++, JdbcUtils.generatePhoneNumber(String.valueOf(i), String.valueOf(j)), i);
            }
        }
        batch.flush();
        batch.reset();


    }
}
