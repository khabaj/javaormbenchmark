package com.khabaj.ormbenchmark.benchmarks.configuration.jpa;

public enum JpaVendor {
    HIBERNATE("Hibernate"),
    ECLIPSE_LINK("EclipseLink"),
    OPENJPA("OpenJPA");

    private final String jpaVendorName;

    JpaVendor(String jpaVendorName) {
        this.jpaVendorName = jpaVendorName;
    }

    public String getJpaVendorName() {
        return jpaVendorName;
    }
}
