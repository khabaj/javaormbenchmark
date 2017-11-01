package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public class Hibernate_CreateBenchmark extends HibernateBenchmark implements CreateBenchmark{

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        session.getTransaction().begin();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
        session.clear();
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        User user = new User("John", "Stevens");
        session.getTransaction().begin();
        session.persist(user);
        session.getTransaction().commit();
    }

    @Benchmark
    @Override
    public void insert100Entities() {
        performBatchInsert(100 );
    }

    @Benchmark
    @Override
    public void insert1000Entities() {
        performBatchInsert(100 );
    }

    @Benchmark
    @Override
    public void insert10000Entities() {
        performBatchInsert(10000);
    }

    @Benchmark
    @Override
    public void insert100000Entities() {
        performBatchInsert(100000);
    }
}
