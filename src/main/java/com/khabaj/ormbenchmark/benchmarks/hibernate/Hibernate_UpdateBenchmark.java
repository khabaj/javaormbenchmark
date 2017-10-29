package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.UpdateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

public class Hibernate_UpdateBenchmark extends HibernateBenchmark implements UpdateBenchmark {

    @Setup
    public void populateDatabase() {
        session.getTransaction().begin();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();

        performBatchInsert(100000, 5000);
        session.clear();
    }

    @TearDown
    public void clear() {
        session.getTransaction().begin();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
        session.clear();
    }

    @Override
    @Benchmark
    public void update1Entity(Blackhole blackhole) {
        User user = session.get(User.class, 10);
        blackhole.consume(user);
    }
}
