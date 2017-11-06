package com.khabaj.ormbenchmark.benchmarks.jpa.read;

import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;

public class EclipseLinkJpa_ReadBenchmark extends Jpa_ReadBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.ECLIPSE_LINK);
    }
}
