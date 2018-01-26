package com.khabaj.ormbenchmark.benchmarks;

public interface UpdateBenchmark extends PersistenceBenchmark {

    int NUMBER_OF_ROWS_IN_DB = 10000;

    void update1Entity();
    void update10000Entities();
}
