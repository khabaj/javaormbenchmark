package com.khabaj.ormbenchmark.benchmarks.jpa.delete;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class OpenJpa_DeleteBenchmark extends Jpa_DeleteBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.OPENJPA);
    }

}
