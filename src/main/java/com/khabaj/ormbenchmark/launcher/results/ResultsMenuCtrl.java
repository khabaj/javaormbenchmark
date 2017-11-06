package com.khabaj.ormbenchmark.launcher.results;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ResultsMenuCtrl implements Initializable {

    private final String RESULTS_DIRECTORY = "./benchmark_results/";
    @FXML
    JFXListView<String> groupResultsListView;
    @FXML
    JFXListView<String> resultsDirectoriesListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groupResultsListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> groupTableByColumn(newValue));

        resultsDirectoriesListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> showResults(newValue));
    }

    public void refreshMenu() {
        prepareGroupByList();
        prepareResultsList();
    }

    private void prepareGroupByList() {

        groupResultsListView.getItems().clear();

        groupResultsListView.getItems().add("None");
        groupResultsListView.getItems().add(TableColumn.DATABASE.getColumnName());
        groupResultsListView.getItems().add(TableColumn.BENCHMARK.getColumnName());
        groupResultsListView.getItems().add(TableColumn.PERSISTENCE_PROVIDER.getColumnName());
    }

    private void prepareResultsList() {

        resultsDirectoriesListView.getItems().clear();

        try {
            Files.createDirectories(Paths.get(RESULTS_DIRECTORY));
            List<Path> resultsList = Files.list(Paths.get(RESULTS_DIRECTORY)).collect(Collectors.toList());

            resultsList.stream()
                    .sorted(Comparator.reverseOrder())
                    .forEach(result -> resultsDirectoriesListView.getItems().add(String.valueOf(result.getFileName())));
        } catch (IOException e) {
            System.out.println("Can not access results directory: " + RESULTS_DIRECTORY);
        }

        resultsDirectoriesListView.getSelectionModel().selectFirst();
    }

    private void showResults(String selectedDirectory) {
        if (!StringUtils.isEmpty(selectedDirectory)) {
            ResultsTabCtrl resultsTabCtrl = ResultsTabCtrl.getInstance();
            resultsTabCtrl.showResults(RESULTS_DIRECTORY + selectedDirectory, selectedDirectory);
            groupResultsListView.getSelectionModel().selectFirst();
        }
    }

    private void groupTableByColumn(String column) {
        ResultsTabCtrl resultsTabCtrl = ResultsTabCtrl.getInstance();
        resultsTabCtrl.groupTableByColumn(column);
    }
}
