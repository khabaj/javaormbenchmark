package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.DeleteBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.util.List;

public class Hibernate_DeleteBenchmark extends HibernateBenchmark implements DeleteBenchmark {

    List<User> users;
    int entitiesCount = 0;

    @Setup(Level.Invocation)
    public void populateDatabase() {
        if (entitiesCount < 1) {
            session.getTransaction().begin();
            session.createQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
            performBatchInsert(100000);
            entitiesCount = 100000;
            users = getUsers();
            session.clear();
        }
    }

    @TearDown(Level.Invocation)
    public void clearSession() {
        session.clear();
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

        session.getTransaction().begin();
        for (int i = 0; i < rowsToDelete; i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();

                session.getTransaction().commit();
                session.getTransaction().begin();
            }
            User user = users.get(entitiesCount - 1);
            user = (User) session.merge(user);
            session.remove(user);
            entitiesCount--;
        }
        session.getTransaction().commit();
    }
}
