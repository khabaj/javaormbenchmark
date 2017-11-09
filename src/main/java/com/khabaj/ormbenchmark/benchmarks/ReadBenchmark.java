package com.khabaj.ormbenchmark.benchmarks;

public interface ReadBenchmark extends PersistenceBenchmark{

    int NUMBER_OF_ROWS_IN_DB = 10000;

    void findEntityByID();
    void findAllEntities();
    void findByStringProperty();
    void findByIndexedStringColumn();
    void findByCollectionElement();
}
