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
        entityManager.getTransaction().begin();
        for (int i = 0; i<100; i++) {
            entityManager.persist(new User("John" + i, "LastName" + i));
        }
        entityManager.getTransaction().commit();
    }

    @Benchmark
    @Override
    public void insert10000Entities() {
        entityManager.getTransaction().begin();
        performBatchInsert(10000, 5000);
        entityManager.getTransaction().commit();
    }

    @Benchmark
    @Override
    public void insert100000Entities() {
        entityManager.getTransaction().begin();
        performBatchInsert(100000, 5000);
        entityManager.getTransaction().commit();
    }

    private void performBatchInsert(int rowsNumber, int batchSize) {
        for (int i = 0; i<rowsNumber; i++) {

            if ( i > 0 && i % batchSize == 0 ) {
                entityManager.flush();
                entityManager.clear();

                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
            entityManager.persist(new User("John" + i, "LastName" + i));
        }
    }

}
