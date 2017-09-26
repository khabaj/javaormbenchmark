package com.khabaj.ormbenchmark.benchmarks;

import com.khabaj.Globals;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(Globals.FORKS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 20)
@Warmup(iterations = 20)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class BaseBenchmark {
}
