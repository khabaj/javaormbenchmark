package com.khabaj.ormbenchmark.benchmarks.jpa.batchsize;

import com.khabaj.ormbenchmark.benchmarks.BatchSizeBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;
import com.khabaj.ormbenchmark.benchmarks.jpa.JpaBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public abstract class JpaBatchSizeBenchmark extends JpaBenchmark implements BatchSizeBenchmark {

    abstract void initialize(int batchSize);

    @Override
    public void setUp() {
    }

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Benchmark
    @Override
    public void batchSize1_() {
        initialize(1);
        performBatchInsert(NUMBER_OF_ENTITIES, 1);
    }

    @Benchmark
    @Override
    public void batchSize10_() {
        initialize(10);
        performBatchInsert(NUMBER_OF_ENTITIES, 10);
    }

    @Benchmark
    @Override
    public void batchSize100_() {
        initialize(100);
        performBatchInsert(NUMBER_OF_ENTITIES, 100);
    }

    @Benchmark
    @Override
    public void batchSize1000_() {
        init(JpaVendor.ECLIPSE_LINK, 1000);
        performBatchInsert(NUMBER_OF_ENTITIES, 1000);
    }

    @Benchmark
    @Override
    public void batchSize10000_() {
        initialize(10000);
        performBatchInsert(NUMBER_OF_ENTITIES, 10000);
    }

    @Benchmark
    @Override
    public void batchSize100000_() {
        initialize(100000);
        performBatchInsert(NUMBER_OF_ENTITIES, 100000);
    }


}
