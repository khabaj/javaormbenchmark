package com.khabaj.ormbenchmark.benchmarks.jpa.read;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class OpenJpa_ReadBenchmark extends Jpa_ReadBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.OPENJPA);
    }

}
