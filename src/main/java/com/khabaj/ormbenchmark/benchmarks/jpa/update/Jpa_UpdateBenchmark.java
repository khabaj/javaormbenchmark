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
    }

    @Override
    @Benchmark
    public void update1Entity() {
        performBatchUpdate(1);
    }

    @Override
    @Benchmark
    public void update10000Entities() {
        performBatchUpdate(10000);
    }

    private void performBatchUpdate(int rowsToUpdate) {

        entityManager.getTransaction().begin();
        for (int i = 1; i <= rowsToUpdate; i++) {
            if (i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();

                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
            User user = entityManager.getReference(User.class, i);
            user.update();
            entityManager.merge(user);
        }
        entityManager.flush();
        entityManager.clear();
        entityManager.getTransaction().commit();
    }
}
