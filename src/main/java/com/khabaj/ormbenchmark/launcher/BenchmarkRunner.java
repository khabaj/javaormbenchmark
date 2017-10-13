package com.khabaj.ormbenchmark.launcher;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner extends Thread {

    public void run() {
        Options options = new OptionsBuilder()
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.TEXT)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            e.printStackTrace();
        }
    }
}
