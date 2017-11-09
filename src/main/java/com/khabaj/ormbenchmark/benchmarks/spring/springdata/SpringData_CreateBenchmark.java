package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public class SpringData_CreateBenchmark extends SpringData_Benchmark implements CreateBenchmark {

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        userRepository.deleteAll();
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        userRepository.save(new User("FirstName", "LastName"));
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
