package com.khabaj.ormbenchmark.benchmarks.jpa.create;

import com.khabaj.ormbenchmark.benchmarks.CreateBenchmark;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import com.khabaj.ormbenchmark.benchmarks.jpa.JpaBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public abstract class Jpa_CreateBenchmark extends JpaBenchmark implements CreateBenchmark {

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Benchmark
    @Override
    public void insert1Entity() {
        User user = new User("John", "Stevens");
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    @Benchmark
    @Override
    public void insert100Entities() {
        performBatchInsert(100);
    }

    @Benchmark
    @Override
    public void insert1000Entities() {
        performBatchInsert(1000);
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
