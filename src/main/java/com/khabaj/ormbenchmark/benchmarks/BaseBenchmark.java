package com.khabaj.ormbenchmark.benchmarks;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public abstract class BaseBenchmark {
    public final int BATCH_SIZE = 100;
}
