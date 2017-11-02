package com.khabaj.ormbenchmark.benchmarks.jpa.delete;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class HibernateJpa_DeleteBenchmark extends Jpa_DeleteBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.HIBERNATE);
    }
}
