package com.khabaj.ormbenchmark.launcher;

import com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources.DataSource;
import com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources.DataSourceService;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;

public class BenchmarkRunner extends Thread {



    public void run() {
        Options options = new OptionsBuilder()
                //.shouldDoGC(true)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        DataSourceService dataSourceService = DataSourceService.getInstance();
        List<DataSource> dataSources = dataSourceService.getDataSources();

        for (DataSource dataSource : dataSources) {
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
}
