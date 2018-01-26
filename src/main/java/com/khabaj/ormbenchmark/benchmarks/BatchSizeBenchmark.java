package com.khabaj.ormbenchmark.benchmarks;

public interface BatchSizeBenchmark extends PersistenceBenchmark{

    int NUMBER_OF_ENTITIES=100000;

    void batchSize1_();
    void batchSize10_();
    void batchSize100_();
    void batchSize1000_();
    void batchSize10000_();
    void batchSize100000_();
}
