package com.khabaj.ormbenchmark.benchmarks;

public interface DeleteBenchmark extends PersistenceBenchmark {
    int NUMBER_OF_ROWS_IN_DB = 10000;

    void delete1Entity();
    void delete10000Entities();
}
