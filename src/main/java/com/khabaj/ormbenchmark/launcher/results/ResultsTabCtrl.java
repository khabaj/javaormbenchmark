package com.khabaj.ormbenchmark.launcher.results;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ResultsTabCtrl implements Initializable {

    private static ResultsTabCtrl instance;
    private ResultsService resultsService;
    private JFXTreeTableView<Result> tableView;

    @FXML
    private ResultsMenuCtrl resultsMenuController;
    @FXML
    private VBox resultsContent;

    public static ResultsTabCtrl getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.instance = this;
        resultsService = ResultsService.getInstance();
        refreshMenu();
    }

    public void showResults(String resultsDirectoryPath, String selectedBenchmark) {

        resultsContent.getChildren().clear();
        resultsService.loadResults(resultsDirectoryPath);
        buildResultsTable();

        Label label = new Label();
        label.setFont(new Font("Arial", 24));
        label.setText(selectedBenchmark);

        resultsContent.getChildren().addAll(label,tableView);
    }

    private void buildResultsTable() {

        tableView = new JFXTreeTableView<>();
        tableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.getColumns().setAll(prepareTableColumns());
        tableView.setShowRoot(false);
        tableView.setEditable(false);

        TreeItem<Result> root = new RecursiveTreeItem<>(FXCollections.observableList(resultsService.getResults()), RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
    }

    public void refreshMenu() {
        resultsMenuController.refreshMenu();
    }

    private List<JFXTreeTableColumn<Result, ?>> prepareTableColumns() {

        List<JFXTreeTableColumn<Result, ?>> tableColumns = new ArrayList<>();

        JFXTreeTableColumn<Result, String> databaseColumn = new JFXTreeTableColumn<>(TableColumns.DATABASE);
        databaseColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (databaseColumn.validateValue(param)) {
                return param.getValue().getValue().databaseProperty();
            } else {
                return databaseColumn.getComputedValue(param);
            }
        });
        tableColumns.add(databaseColumn);

        JFXTreeTableColumn<Result, String> benchmarkColumn = new JFXTreeTableColumn<>(TableColumns.BENCHMARK);
        benchmarkColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (benchmarkColumn.validateValue(param)) {
                return param.getValue().getValue().operationProperty();
            } else {
                return benchmarkColumn.getComputedValue(param);
            }
        });
        tableColumns.add(benchmarkColumn);

        JFXTreeTableColumn<Result, String> persistenceProviderColumn = new JFXTreeTableColumn<>(TableColumns.PERSISTENCE_PROVIDER);
        persistenceProviderColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (persistenceProviderColumn.validateValue(param)) {
                return param.getValue().getValue().persistenceProviderProperty();
            } else {
                return persistenceProviderColumn.getComputedValue(param);
            }
        });
        tableColumns.add(persistenceProviderColumn);

        JFXTreeTableColumn<Result, Number> scoreColumn = new JFXTreeTableColumn<>(TableColumns.SCORE);
        scoreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, Number> param) -> {
            if (scoreColumn.validateValue(param)) {
                return param.getValue().getValue().scoreProperty();
            } else {
                return scoreColumn.getComputedValue(param);
            }
        });
        tableColumns.add(scoreColumn);

        JFXTreeTableColumn<Result, Number> scoreErrorColumn = new JFXTreeTableColumn<>(TableColumns.SCORE_ERROR);
        scoreErrorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, Number> param) -> {
            if (scoreErrorColumn.validateValue(param)) {
                return param.getValue().getValue().scoreErrorProperty();
            } else {
                return scoreErrorColumn.getComputedValue(param);
            }
        });
        tableColumns.add(scoreErrorColumn);

        JFXTreeTableColumn<Result, String> unitColumn = new JFXTreeTableColumn<>(TableColumns.Unit);
        unitColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (unitColumn.validateValue(param)) {
                return param.getValue().getValue().scoreUnitProperty();
            } else {
                return unitColumn.getComputedValue(param);
            }
        });
        tableColumns.add(unitColumn);

        return tableColumns;
    }

    public void groupTableByColumn(String columnName) {

        if (tableView != null) {
            unGroupAll();
            TreeTableColumn<Result, ?> column = tableView.getColumns()
                    .stream()
                    .filter(col -> col.getText().equals(columnName))
                    .findFirst().orElse(null);

            if (column != null) {
                new Thread(() -> tableView.group(column)).start();
            }
        }
    }

    private void unGroupAll() {
        tableView.getColumns().forEach(col -> tableView.unGroup(col));
    }


    /*private void printChart(String operation) {

        List<Result> results = resultsService.getResults();

        List filteredResults = results.stream()
                .filter((result) -> StringUtils.equals(result.getOperation(), operation))
                .collect(Collectors.toList());

        //Defining the x axis
        CategoryAxis xAxis = new CategoryAxis();

        xAxis.setLabel("Databases");

        //Defining the y axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Score");

        //Creating the Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(operation);


        List<XYChart.Series<String, Number>> series = new ArrayList<>();


        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Hibernate");
        series1.getData().add(new XYChart.Data<>("H2", 1.0));
        series1.getData().add(new XYChart.Data<>("MySQL", 3.0));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("EclipseLink");
        series2.getData().add(new XYChart.Data<>("H2", 5.0));
        series2.getData().add(new XYChart.Data<>("MySQL", 6.0));

        barChart.getData().addAll(series1, series2);
        resultsContent.getChildren().add(barChart);

    }*/
}
