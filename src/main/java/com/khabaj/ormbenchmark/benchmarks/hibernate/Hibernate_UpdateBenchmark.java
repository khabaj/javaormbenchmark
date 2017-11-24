package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

public class Hibernate_UpdateBenchmark extends HibernateBenchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        session.getTransaction().begin();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
        performBatchInsert(NUMBER_OF_ROWS_IN_DB);
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

    private void performBatchUpdate(int rowsToUpdate) {
        session.getTransaction().begin();
        for (int i = 1; i <= rowsToUpdate; i++) {
            if ( i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();

                session.getTransaction().commit();
                session.getTransaction().begin();
            }
            User user = session.get(User.class, i);
            user.update();
        }
        session.getTransaction().commit();
    }
}
