package com.khabaj.ormbenchmark.launcher;

import com.khabaj.ormbenchmark.launcher.benchmark.datasources.DataSource;
import com.khabaj.ormbenchmark.launcher.benchmark.datasources.DataSourceService;
import com.khabaj.ormbenchmark.launcher.benchmark.settings.BenchmarkSettings;
import com.khabaj.ormbenchmark.launcher.results.ResultsTabCtrl;
import javafx.application.Platform;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        options.resultFormat(ResultFormatType.CSV);

        DataSourceService dataSourceService = DataSourceService.getInstance();
        List<DataSource> dataSources = dataSourceService.getDataSources();

        String resultsDirectoryPath = "./benchmark_results/" + getCurrentDTString();
        createResultsDirectories(resultsDirectoryPath);

        for (DataSource dataSource : dataSources) {
            String resultFileName = prepareResultFileName(resultsDirectoryPath + "/" + dataSource.getConnectionName());
            options.result(resultFileName);

            try {
                dataSourceService.testConnection(dataSource);
                dataSourceService.saveActiveDataSourceProperties(dataSource);
                new Runner(options).run();
                System.gc();
            } catch (Exception e) {
                System.out.println("Benchmark for " + dataSource.getConnectionName() + " stopped because of exception: "
                        + e.getMessage());
            }
        }

        Platform.runLater(() -> {
            ResultsTabCtrl resultsTabCtrl = ResultsTabCtrl.getInstance();
            resultsTabCtrl.refreshMenu();
        });
    }

    private void createResultsDirectories(String resultsDirectoryPath) {

        try {
            Files.createDirectories(Paths.get(resultsDirectoryPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDTString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return LocalDateTime.now().format(dateFormatter);
    }

    private String prepareResultFileName(String dataSourceName) {
        return dataSourceName + ".csv";
    }
}
