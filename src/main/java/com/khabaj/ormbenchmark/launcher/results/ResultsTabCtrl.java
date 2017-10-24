package com.khabaj.ormbenchmark.launcher.results;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ResultsTabCtrl implements Initializable {

    private static ResultsTabCtrl instance;

    @FXML
    private ResultsMenuCtrl resultsMenuController;

    @FXML
    private VBox resultsContent;

    ResultsService resultsService;

    public static ResultsTabCtrl getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resultsService = ResultsService.getInstance();
        this.instance = this;


        /******TO remove***/
        String resultsDirectoryPath = "src/test/resources/2017-10-19 14-39-32";

        ResultsService resultsService = ResultsService.getInstance();
        List<Result> results = resultsService.loadResults(resultsDirectoryPath);
        resultsMenuController.refreshMenu();
    }

    public void loadResults(String resultsDirectoryPath) {

        List<Result> results = resultsService.loadResults(resultsDirectoryPath);
        resultsMenuController.refreshMenu();
    }

    public void showResults(TreeItem<String> selectedItem) {
        resultsContent.getChildren().clear();

        String menuCategory;

        String value = selectedItem.getParent().getValue();
        if (!StringUtils.isEmpty(value))
            menuCategory = value;
        else
            menuCategory = selectedItem.getValue();

        switch (menuCategory) {
            case MenuCategory.OPERATIONS:
                printChart(selectedItem.getValue());
                break;
            case MenuCategory.PERSISTENCE_PROVIDERS:
                break;
            case MenuCategory.DATABASES:
                break;
        }
    }

    private void printChart(String operation) {

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

    }
}
