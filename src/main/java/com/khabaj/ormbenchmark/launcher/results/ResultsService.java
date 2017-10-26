package com.khabaj.ormbenchmark.launcher.results;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ResultsService {

    private static ResultsService instance;

    private List<Result> results;

    private ResultsService() {
        this.results = new ArrayList<>();
    }

    public static ResultsService getInstance() {
        if (instance == null) {
            instance = new ResultsService();
        }
        return instance;
    }

    public List<Result> loadResults(String resultsDirectoryPath) {

        results = new ArrayList<>();

        try {
            List<Path> filesList = getFilesList(resultsDirectoryPath);
            final String SEPARATOR = ",";

            for (Path path : filesList) {

                BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)));
                reader.lines()
                        .skip(1)
                        .forEach(line -> {
                            line = StringUtils.remove(line, '\"');
                            String[] elements = line.split(SEPARATOR);

                            Result result = new Result();

                            result.setDatabase(StringUtils.remove(String.valueOf(path.getFileName()), ".csv"));

                            DecimalFormat decimalFormat = new DecimalFormat("#.####");
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                            decimalFormat.setDecimalFormatSymbols(symbols);

                            result.setScore(Double.valueOf(decimalFormat.format(Double.parseDouble(elements[4]))));
                            result.setScoreError(Double.valueOf(decimalFormat.format(Double.parseDouble(elements[5]))));
                            result.setScoreUnit(elements[6]);

                            String benchmark = elements[0];
                            List<String> benchmarksStrings = Arrays.asList(StringUtils.split(benchmark, "._"));
                            String operation = benchmarksStrings.get(benchmarksStrings.size() - 1);
                            String persistenceProvider = benchmarksStrings.get(benchmarksStrings.size() - 3);

                            result.setOperation(operation);
                            result.setPersistenceProvider(persistenceProvider);

                            results.add(result);
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<Path> getFilesList(String resultsDirectoryPath) throws IOException {
        return Files.list(Paths.get(resultsDirectoryPath))
                .filter((path) -> String.valueOf(path).endsWith(".csv"))
                .collect(Collectors.toList());
    }

    public List<Result> getResults() {
        return results;
    }
}
