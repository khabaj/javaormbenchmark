package com.khabaj.ormbenchmark.benchmarks.jpa.update;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import com.khabaj.ormbenchmark.benchmarks.jpa.JpaBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.util.List;

public abstract class Jpa_UpdateBenchmark extends JpaBenchmark implements UpdateBenchmark {

    List<User> users;

    @Setup
    public void populateDatabase() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
        performBatchInsert(NUMBER_OF_ROWS_IN_DB);
        users = getUsers();
        entityManager.clear();
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

        entityManager.getTransaction().begin();
        for (int i = 0; i < rowsToUpdate; i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();

                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
            User user = users.get(i);
            user.update();
            entityManager.merge(user);
        }
        entityManager.getTransaction().commit();
    }
}
