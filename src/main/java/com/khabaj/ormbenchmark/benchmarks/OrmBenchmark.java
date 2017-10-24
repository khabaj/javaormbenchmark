package com.khabaj.ormbenchmark.benchmarks;

public interface OrmBenchmark {

    void insertOneRow();
    void insert100Rows();
    void insert10000Rows();
    void insert100000Rows();
}
