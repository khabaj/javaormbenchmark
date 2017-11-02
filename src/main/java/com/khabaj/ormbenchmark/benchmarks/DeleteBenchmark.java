package com.khabaj.ormbenchmark.benchmarks;

public interface DeleteBenchmark extends PersistenceBenchmark {
    int NUMBER_OF_ROWS_IN_DB = 100000;

    void delete1Entity();
    void delete100Entities();
    void delete1000Entities();
    void delete10000Entities();
    void delete100000Entities();
}
