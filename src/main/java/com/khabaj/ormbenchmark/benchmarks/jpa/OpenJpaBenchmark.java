package com.khabaj.ormbenchmark.benchmarks.jpa;

import com.khabaj.ormbenchmark.benchmarks.config.JpaVendor;

public class OpenJpaBenchmark extends JpaBenchmark {

    @Override
    public void setUp() {
        init(JpaVendor.OPENJPA);
    }

}
