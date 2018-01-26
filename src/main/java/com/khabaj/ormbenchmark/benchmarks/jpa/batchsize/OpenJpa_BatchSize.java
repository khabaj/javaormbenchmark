package com.khabaj.ormbenchmark.benchmarks.jpa.batchsize;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class OpenJpa_BatchSize extends JpaBatchSizeBenchmark {

    boolean initialized = false;

    @Override
    void initialize(int batchSize) {
        if (!initialized) {
            init(JpaVendor.OPENJPA, batchSize);
            initialized = true;
        }
    }
}
