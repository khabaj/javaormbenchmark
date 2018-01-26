package com.khabaj.ormbenchmark.benchmarks.jpa.update;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class Hibernate_UpdateBenchmark extends Jpa_UpdateBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.HIBERNATE);
    }
}
