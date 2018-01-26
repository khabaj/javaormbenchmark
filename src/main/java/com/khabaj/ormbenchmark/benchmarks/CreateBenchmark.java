package com.khabaj.ormbenchmark.benchmarks;

public interface CreateBenchmark extends PersistenceBenchmark{

    void insert1Entity();
    void insert10000Entities();
}
