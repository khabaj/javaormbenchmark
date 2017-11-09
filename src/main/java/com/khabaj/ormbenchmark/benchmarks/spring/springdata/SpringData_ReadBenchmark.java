package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.ReadBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.Random;

public class SpringData_ReadBenchmark extends SpringData_Benchmark implements ReadBenchmark {

    int wantedUserId;
    Blackhole blackhole;

    @Setup
    public void prepare(Blackhole blackhole) {
        this.blackhole = blackhole;
        userRepository.deleteAll();
        batchInsertUsersWithPhones(100000);
    }

    @Setup(Level.Invocation)
    public void randomUserId() {
        this.wantedUserId = new Random().nextInt(NUMBER_OF_ROWS_IN_DB);
    }

    @Benchmark
    @Override
    public void findEntityByID() {
        User user = userRepository.findOne(wantedUserId);
        blackhole.consume(user);
    }

    @Benchmark
    @Override
    public void findAllEntities() {
        List users = userRepository.findAll();
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByStringProperty() {
        List users = userRepository.findByFirstName("FirstName" + wantedUserId);
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByIndexedStringColumn() {
        List users = userRepository.findByLastName("LastName" + wantedUserId);
        blackhole.consume(users);
    }

    @Benchmark
    @Override
    public void findByCollectionElement() {

        List users = userRepository.findByPhoneNumber("111111111");
        blackhole.consume(users);
    }
}
