package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.util.ArrayList;
import java.util.List;

public class SpringData_UpdateBenchmark extends SpringData_Benchmark implements UpdateBenchmark {

    List<User> users;

    @Setup
    public void populateDatabase() {
        userRepository.deleteAll();
        batchInsertUsers(NUMBER_OF_ROWS_IN_DB);
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

    private void performBatchUpdate(int rowsToUpdate) {

        List<User> usersToUpdate = new ArrayList<>();
        for (int i = 0; i < rowsToUpdate; i++) {

            if (i > 0 && i % BATCH_SIZE == 0) {
                userRepository.save(usersToUpdate);
                usersToUpdate.clear();
            }

            User user = users.get(i);
            user.update();
            usersToUpdate.add(user);
        }
        userRepository.save(usersToUpdate);
    }
}
