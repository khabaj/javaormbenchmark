package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.BatchSizeBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

public class SpingData_BatchSizeBenchmark extends SpringData_Benchmark implements BatchSizeBenchmark{

    boolean initialized = false;

    public void initialize(int batchSize) {
        if (!initialized) {
            super.setUp(batchSize);
            initialized = true;
        }
    }

    @TearDown(Level.Invocation)
    public void clearAfterEveryInvocation() {
        userRepository.deleteAll();
    }

    @Benchmark
    @Override
    public void batchSize1_() {
        perfomBatchInsert(1);
    }

    @Benchmark
    @Override
    public void batchSize10_() {
        perfomBatchInsert(10);
    }

    @Benchmark
    @Override
    public void batchSize100_() {
        perfomBatchInsert(100);
    }

    @Benchmark
    @Override
    public void batchSize1000_() {
        perfomBatchInsert(1000);
    }

    @Benchmark
    @Override
    public void batchSize10000_() {
        perfomBatchInsert(10000);
    }

    @Benchmark
    @Override
    public void batchSize100000_() {
        perfomBatchInsert(100000);
    }

    private void perfomBatchInsert(int batchSize) {
        initialize(batchSize);
        batchInsertUsers(NUMBER_OF_ENTITIES, batchSize);
    }
}
