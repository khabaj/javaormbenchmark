package com.khabaj.ormbenchmark.benchmarks.jpa.create;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class OpenJpa_CreateBenchmark extends Jpa_CreateBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.OPENJPA);
    }

}
