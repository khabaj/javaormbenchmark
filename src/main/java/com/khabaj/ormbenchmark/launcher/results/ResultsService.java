package com.khabaj.ormbenchmark.launcher.results;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ResultsService {

    private static ResultsService instance;

    private List<Result> results;
    private Set<String> dataSources;
    private Set<String> persistenceProviders;
    private Set<String> operations;



    private ResultsService() {
        this.results = new ArrayList<>();
        this.dataSources = new HashSet<>();
        this.persistenceProviders = new HashSet<>();
        this.operations = new HashSet<>();
    }

    public static ResultsService getInstance() {
        if (instance == null) {
            instance = new ResultsService();
        }

        return instance;
    }

    public List<Result> loadResults(String resultsDirectoryPath) {

        results = new ArrayList<>();
        this.dataSources = new HashSet<>();
        this.persistenceProviders = new HashSet<>();
        this.operations = new HashSet<>();

        try {
            List<Path> filesList = getFilesList(resultsDirectoryPath);

            for (Path path : filesList) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(new File(String.valueOf(path)));

                String dataSourceName = StringUtils.remove(String.valueOf(path.getFileName()), ".json");
                readResults(root, dataSourceName);
                dataSources.add(dataSourceName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    private void readResults(JsonNode root, String dataSourceName) {
        root.forEach((jsonNode) -> {
            Result result = new Result();

            JsonNode primaryMetricNode = jsonNode.get("primaryMetric");
            result.setScore(primaryMetricNode.get("score").doubleValue());
            result.setScoreError(primaryMetricNode.get("scoreError").doubleValue());
            result.setScoreUnit(primaryMetricNode.get("scoreUnit").textValue());

            String benchmark = jsonNode.get("benchmark").textValue();
            List<String> benchmarksStrings = Arrays.asList(StringUtils.split(benchmark, "."));
            String operation = benchmarksStrings.get(benchmarksStrings.size() - 1);
            String persistenceProvider = StringUtils.remove(benchmarksStrings.get(benchmarksStrings.size() - 2), "Benchmark");

            result.setOperation(operation);
            result.setPersistenceProvider(persistenceProvider);
            result.setDatabase(dataSourceName);

            results.add(result);
            this.persistenceProviders.add(persistenceProvider);
            this.operations.add(operation);
        });
    }

    private List<Path> getFilesList(String resultsDirectoryPath) throws IOException {
        return Files.list(Paths.get(resultsDirectoryPath))
                .filter((path) -> String.valueOf(path).endsWith(".json"))
                .collect(Collectors.toList());
    }

    public List<Result> getResults() {
        return results;
    }

    public Set<String> getDataSources() {
        return dataSources;
    }

    public Set<String> getPersistenceProviders() {
        return persistenceProviders;
    }

    public Set<String> getOperations() {
        return operations;
    }
}
