package com.khabaj.ormbenchmark.benchmarks.spring.jdbctemplate;

import com.khabaj.ormbenchmark.benchmarks.ReadBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Random;

public class SpringJdbc_ReadBenchmark extends SpringJdbc_Benchmark implements ReadBenchmark {

    int wantedUserId;
    Blackhole blackhole;

    @Setup
    public void populateDatabase(Blackhole blackhole) {
        this.blackhole = blackhole;
        super.setUp();
        batchInsertUsers(NUMBER_OF_ROWS_IN_DB);
        batchInsertPhones(NUMBER_OF_ROWS_IN_DB);
    }

    @Setup(Level.Invocation)
    public void randomUserId() {
        this.wantedUserId = new Random().nextInt(NUMBER_OF_ROWS_IN_DB - 1) + 1;
    }

    @Benchmark
    @Override
    public void findEntityByID() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        User user = jdbcTemplate.queryForObject("SELECT * FROM USER_TABLE WHERE id = ?",
                new Object[]{wantedUserId},
                new BeanPropertyRowMapper<>(User.class));

        blackhole.consume(user);
    }

    @Benchmark
    @Override
    public void findAllEntities() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query("SELECT * FROM USER_TABLE",
                new BeanPropertyRowMapper<>(User.class));

        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByStringProperty() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query("SELECT * FROM USER_TABLE WHERE firstName = ?",
                new Object[]{"FirstName" + wantedUserId},
                new BeanPropertyRowMapper<>(User.class));

        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByIndexedStringColumn() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query("SELECT * FROM USER_TABLE WHERE lastName = ?",
                new Object[]{"LastName" + wantedUserId},
                new BeanPropertyRowMapper<>(User.class));

        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByCollectionElement() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query("SELECT u.* FROM USER_TABLE u, PHONE p WHERE p.userId = u.id and p.phoneNumber = ?",
                new Object[]{"111111111"},
                new BeanPropertyRowMapper<>(User.class));
        blackhole.consume(users);
    }
}
