package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.util.List;

public class Hibernate_UpdateBenchmark extends HibernateBenchmark implements UpdateBenchmark {

    List<User> users;

    @Setup
    public void populateDatabase() {
        session.getTransaction().begin();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
        performBatchInsert(NUMBER_OF_ROWS_IN_DB);
        users = getUsers();
        session.clear();
    }

    @TearDown(Level.Invocation)
    public void clearSession() {
        session.clear();
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
        session.getTransaction().begin();

        for (int i = 0; i < rowsToUpdate; i++) {

            if (i > 0 && i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();

                session.getTransaction().commit();
                session.getTransaction().begin();
            }
            User user = users.get(i);
            user.update();
            session.update(user);
        }
        session.getTransaction().commit();
    }
}
