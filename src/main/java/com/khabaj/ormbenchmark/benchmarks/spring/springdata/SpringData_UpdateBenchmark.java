package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.util.List;

public class SpringData_UpdateBenchmark extends SpringData_Benchmark implements UpdateBenchmark {

    List<User> users;

    @Setup
    public void populateDatabase() {
        userRepository.deleteAll();
        batchInsertUsers(100000);
        users = userRepository.findAll();
    }

    @Override
    @Benchmark
    public void update1Entity() {
        performBatchUpdate(1);
    }

    @Override
    @Benchmark
    public void update100Entities() {
        performBatchUpdate(100);
    }

    @Override
    @Benchmark
    public void update1000Entities() {
        performBatchUpdate(1000);
    }

    @Override
    @Benchmark
    public void update10000Entities() {
        performBatchUpdate(10000);
    }

    @Override
    @Benchmark
    public void update100000Entities() {
        performBatchUpdate(100000);
    }

    private void performBatchUpdate(int rowsToUpdate) {

        for (int i = 0; i < rowsToUpdate; i++) {
            User user = users.get(i);
            user.update();
            userRepository.save(user);
        }
    }
}
