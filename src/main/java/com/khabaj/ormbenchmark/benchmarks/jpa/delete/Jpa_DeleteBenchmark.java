package com.khabaj.ormbenchmark.benchmarks.jpa.delete;

import com.khabaj.ormbenchmark.benchmarks.DeleteBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import com.khabaj.ormbenchmark.benchmarks.jpa.JpaBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.util.List;

public abstract class Jpa_DeleteBenchmark extends JpaBenchmark implements DeleteBenchmark {

    List<User> users;
    int entitiesCount = 0;

    @Setup(Level.Invocation)
    public void populateDatabase() {
        if (entitiesCount < 1) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("delete from User").executeUpdate();
            entityManager.getTransaction().commit();
            performBatchInsert(100000);
            entitiesCount = 100000;
            users = getUsers();
            entityManager.clear();
        }
    }

    @TearDown(Level.Invocation)
    public void clearEntityManager() {
        entityManager.clear();
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

    @Benchmark
    @Override
    public void delete100000Entities() {
        performBatchDelete(100000);
    }

    private void performBatchDelete(int rowsToDelete) {
        entityManager.getTransaction().begin();

        for (int i = 0; i < rowsToDelete; i++) {

            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();

                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
            User user = users.get(entitiesCount - 1);
            user = entityManager.merge(user);
            entityManager.remove(user);
            entitiesCount--;
        }
        entityManager.getTransaction().commit();
    }
}
