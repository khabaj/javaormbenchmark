package com.khabaj.ormbenchmark.benchmarks;

import org.openjdk.jmh.infra.Blackhole;

public interface UpdateBenchmark extends PersistenceBenchmark {
    void update1Entity(Blackhole blackhole);
}
