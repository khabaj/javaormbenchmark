package com.khabaj.ormbenchmark.benchmarks;

public interface ReadBenchmark extends PersistenceBenchmark{

    void findEntityByID();
    void findAllEntities();
    void findByStringProperty();
    void findByIndexedStringColumn();
    void findByCollectionElement();
}
