package com.khabaj.ormbenchmark.benchmarks.jpa.batchsize;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class EclipseLink_BatchSize extends JpaBatchSizeBenchmark {

    boolean initialized = false;

    @Override
    void initialize(int batchSize) {
        if (!initialized) {
            init(JpaVendor.ECLIPSE_LINK, batchSize);
            initialized = true;
        }
    }
}
