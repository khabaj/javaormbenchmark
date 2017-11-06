package com.khabaj.ormbenchmark.launcher.results;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.khabaj.ormbenchmark.launcher.Main;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @FXML
    public void exportToExcel() {

        File file = showFileChooser();

        if (file != null) {

            try (Workbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = (XSSFSheet) workbook.createSheet();

                // Create
                XSSFTable table = sheet.createTable();
                table.setName("Java_Persistence_Benchmark_Table");
                table.setDisplayName("Java_Persistence_Benchmark_Table");

                // For now, create the initial style in a low-level way
                table.getCTTable().addNewTableStyleInfo();
                table.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");

                // Style the table
                XSSFTableStyleInfo style = (XSSFTableStyleInfo) table.getStyle();
                style.setName("TableStyleMedium2");
                style.setFirstColumn(false);
                style.setLastColumn(false);
                style.setShowRowStripes(false);
                style.setShowColumnStripes(true);

                XSSFCell cell;
                int rowNumber = 0;
                int cellNumber = 0;

                //prepareHeader
                XSSFRow headerRow = sheet.createRow(rowNumber++);
                for (TableColumn tableColumn : TableColumn.values()) {
                    XSSFCell headerCell = headerRow.createCell(cellNumber++);
                    headerCell.setCellValue(tableColumn.getColumnName());
                    table.addColumn();
                }

                for (Result result : resultsService.getResults()) {
                    // Create row
                    XSSFRow row = sheet.createRow(rowNumber++);
                    cellNumber = 0;
                    for (TableColumn column : TableColumn.values()) {
                        // Create cell
                        cell = row.createCell(cellNumber++);
                        switch (column) {
                            case DATABASE:
                                cell.setCellValue(result.getDatabase());
                                break;
                            case BENCHMARK:
                                cell.setCellValue(result.getOperation());
                                break;
                            case PERSISTENCE_PROVIDER:
                                cell.setCellValue(result.getPersistenceProvider());
                                break;
                            case SCORE:
                                cell.setCellValue(result.getScore());
                                break;
                            case SCORE_ERROR:
                                cell.setCellValue(result.getScoreError());
                                break;
                            case UNIT:
                                cell.setCellValue(result.getScoreUnit());
                                break;
                        }
                    }
                }

                for (int i=0; i< table.getNumberOfMappedColumns(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Set which area the table should be placed in
                AreaReference reference = workbook.getCreationHelper().createAreaReference(
                        new CellReference(0, 0), new CellReference(resultsService.getResults().size(), TableColumn.values().length - 1));
                table.setCellReferences(reference);

                // Save
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File showFileChooser() {
        Stage stage = Main.getPrimaryStage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Results to Excel");
        fileChooser.setInitialFileName("Java-Persistence-Benchmark-Results");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Skoroszyt programu Excel", ".xlsx"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return fileChooser.showSaveDialog(stage);
    }

    public void showResults(String resultsDirectoryPath, String selectedBenchmark) {

        resultsContent.getChildren().clear();
        resultsService.loadResults(resultsDirectoryPath);
        buildResultsTable();

        Label label = new Label();
        label.setFont(new Font("Arial", 24));
        label.setText(selectedBenchmark);

        resultsContent.getChildren().addAll(label, tableView);
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

        JFXTreeTableColumn<Result, String> databaseColumn = new JFXTreeTableColumn<>(TableColumn.DATABASE.getColumnName());
        databaseColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (databaseColumn.validateValue(param)) {
                return param.getValue().getValue().databaseProperty();
            } else {
                return databaseColumn.getComputedValue(param);
            }
        });
        tableColumns.add(databaseColumn);

        JFXTreeTableColumn<Result, String> benchmarkColumn = new JFXTreeTableColumn<>(TableColumn.BENCHMARK.getColumnName());
        benchmarkColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (benchmarkColumn.validateValue(param)) {
                return param.getValue().getValue().operationProperty();
            } else {
                return benchmarkColumn.getComputedValue(param);
            }
        });
        tableColumns.add(benchmarkColumn);

        JFXTreeTableColumn<Result, String> persistenceProviderColumn = new JFXTreeTableColumn<>(TableColumn.PERSISTENCE_PROVIDER.getColumnName());
        persistenceProviderColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, String> param) -> {
            if (persistenceProviderColumn.validateValue(param)) {
                return param.getValue().getValue().persistenceProviderProperty();
            } else {
                return persistenceProviderColumn.getComputedValue(param);
            }
        });
        tableColumns.add(persistenceProviderColumn);

        JFXTreeTableColumn<Result, Number> scoreColumn = new JFXTreeTableColumn<>(TableColumn.SCORE.getColumnName());
        scoreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, Number> param) -> {
            if (scoreColumn.validateValue(param)) {
                return param.getValue().getValue().scoreProperty();
            } else {
                return scoreColumn.getComputedValue(param);
            }
        });
        tableColumns.add(scoreColumn);

        JFXTreeTableColumn<Result, Number> scoreErrorColumn = new JFXTreeTableColumn<>(TableColumn.SCORE_ERROR.getColumnName());
        scoreErrorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Result, Number> param) -> {
            if (scoreErrorColumn.validateValue(param)) {
                return param.getValue().getValue().scoreErrorProperty();
            } else {
                return scoreErrorColumn.getComputedValue(param);
            }
        });
        tableColumns.add(scoreErrorColumn);

        JFXTreeTableColumn<Result, String> unitColumn = new JFXTreeTableColumn<>(TableColumn.UNIT.getColumnName());
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
