package com.khabaj.ormbenchmark.benchmarks;

public interface CreateBenchmark extends PersistenceBenchmark{

    void insert1Entity();
    void insert100Entities();
    void insert10000Entities();
    void insert100000Entities();
}