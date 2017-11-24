package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.DeleteBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

import java.util.ArrayList;
import java.util.List;

public class SpringData_DeleteBenchmark extends SpringData_Benchmark implements DeleteBenchmark {

    List<User> users;
    int entitiesCount = 0;

    @Setup(Level.Invocation)
    public void populateDatabase() {
        if (entitiesCount < 1) {
            userRepository.deleteAllInBatch();
            batchInsertUsers(NUMBER_OF_ROWS_IN_DB);
            entitiesCount = NUMBER_OF_ROWS_IN_DB;
            users = userRepository.findAll();
        }
    }

    @Benchmark
    @Override
    public void delete1Entity() {
        performBatchDelete(1);
    }

    @Benchmark
    @Override
    public void delete100Entities() {
        performBatchDelete(100);
    }

    @Benchmark
    @Override
    public void delete1000Entities() {
        performBatchDelete(1000);
    }

    @Benchmark
    @Override
    public void delete10000Entities() {
        performBatchDelete(10000);
    }

    private void performBatchDelete(int rowsToDelete) {
        List<User> usersToDelete = new ArrayList<>();
        for (int i = 0; i < rowsToDelete; i++) {
            if (i % BATCH_SIZE == 0) {
                userRepository.delete(usersToDelete);
                usersToDelete.clear();
            }
            User user = users.get(entitiesCount - 1);
            usersToDelete.add(user);
            entitiesCount--;
        }
        userRepository.delete(usersToDelete);
    }
}
