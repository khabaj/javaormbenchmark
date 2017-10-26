package com.khabaj.ormbenchmark.benchmarks.jpa.create;

import com.khabaj.ormbenchmark.benchmarks.jpa.config.JpaVendor;

public class EclipseLinkJpa_CreateBenchmark extends Jpa_CreateBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.ECLIPSE_LINK);
    }
}
