package com.khabaj.ormbenchmark.benchmarks;

import com.khabaj.ormbenchmark.Globals;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(Globals.FORKS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = Globals.MEASUREMENT_ITERATIONS)
@Warmup(iterations = Globals.WARMUP_ITERATIONS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class BaseBenchmark {
}
