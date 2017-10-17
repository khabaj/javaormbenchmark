package com.khabaj.ormbenchmark.launcher;

import com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources.DataSource;
import com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources.DataSourceService;
import com.khabaj.ormbenchmark.launcher.controllers.benchmark.settings.BenchmarkSettings;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BenchmarkRunner extends Thread {

    private BenchmarkSettings benchmarkSettings;

    public BenchmarkRunner(BenchmarkSettings benchmarkSettings) {
        this.benchmarkSettings = benchmarkSettings;
    }

    public void run() {
        OptionsBuilder options = new OptionsBuilder();
        options.forks(benchmarkSettings.getForks())
                .warmupIterations(benchmarkSettings.getWarmupIterations())
                .measurementIterations(benchmarkSettings.getMeasurementIteriations())
                .mode(benchmarkSettings.getBenchmarkMode())
                .timeUnit(benchmarkSettings.getTimeUnit());

        for(String benchmark : benchmarkSettings.getBenchmarksToRun()) {
            options.include(benchmark);
        }

        options.resultFormat(ResultFormatType.TEXT);

        DataSourceService dataSourceService = DataSourceService.getInstance();
        List<DataSource> dataSources = dataSourceService.getDataSources();

        String benchmarkDT = getCurrentDTString();

        for (DataSource dataSource : dataSources) {
            String resultFileName = prepareResultFileName(dataSource.getConnectionName(), benchmarkDT);
            options.result(resultFileName);

            try {
                dataSourceService.testConnection(dataSource);
                dataSourceService.setActiveDataSource(dataSource);
                new Runner(options).run();
                System.gc();
            } catch (Exception e) {
                System.out.println("Benchmark for " + dataSource.getConnectionName() + " stopped because of exception: "
                        + e.getMessage());
            }
        }
    }

    private String getCurrentDTString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return LocalDateTime.now().format(dateFormatter);
    }

    private String prepareResultFileName(String dataSourceName, String benchmarkDT) {
        return dataSourceName + "_Benchmark_" + benchmarkDT + ".out";
    }
}
