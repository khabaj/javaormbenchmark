package com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.khabaj.ormbenchmark.launcher.controllers.benchmark.JDBCDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataSourcesController implements Initializable {

    @FXML
    JFXTreeTableView<DataSource> dataSourcesTable;

    private ObservableList<DataSource> dataSources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeTableColumn<DataSource, String> connectionNameColumn = new TreeTableColumn<>("Name");
        connectionNameColumn.setCellValueFactory(param -> param.getValue().getValue().connectionNameProperty());

        TreeTableColumn<DataSource, String> connectionURLColumn = new TreeTableColumn<>("URL");
        connectionURLColumn.setCellValueFactory(param -> param.getValue().getValue().connectionURLProperty());

        TreeTableColumn<DataSource, JDBCDriver> jdbcDriverColumn = new TreeTableColumn<>("JDBC Driver");
        jdbcDriverColumn.setCellValueFactory(param -> param.getValue().getValue().jdbcDriverProperty());

        dataSources = FXCollections.observableArrayList();
        dataSources.add(new DataSource("MySQL", "jdbc:mysql://localhost:3306/ormbenchmarkdb?createDatabaseIfNotExist=true", JDBCDriver.MYSQL));
        dataSources.add(new DataSource("H2", "http:fdsfsgfdgfd", JDBCDriver.H2));

        TreeItem<DataSource> root = new RecursiveTreeItem<>(dataSources, RecursiveTreeObject::getChildren);
        dataSourcesTable.getColumns().setAll(connectionNameColumn, connectionURLColumn, jdbcDriverColumn);
        dataSourcesTable.setRoot(root);
        dataSourcesTable.setShowRoot(false);
    }

    @FXML
    public void showAddDataSourceForm() throws IOException {
        showDataSourceForm("Add Data Source", new DataSource());
    }

    @FXML
    public void showEditDataSourceForm() throws IOException {
        TreeItem<DataSource> item = dataSourcesTable.getSelectionModel().getSelectedItem();
        if(item != null) {
            DataSource dataSource = item.getValue();
            showDataSourceForm("Edit Data Source", dataSource);
        }
    }

    public void showDataSourceForm(String windowTitle, DataSource dataSource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/benchmark/DataSourceForm.fxml"));
        AnchorPane rootElement = loader.load();

        DataSourceFormController dataSourceController = loader.getController();
        dataSourceController.setParentForm(this);
        dataSourceController.setDataSource(dataSource);

        Scene scene = new Scene(rootElement);
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void deleteDataSource() {
        TreeItem<DataSource> item = dataSourcesTable.getSelectionModel().getSelectedItem();
        if(item != null) {
            dataSources.remove(item.getValue());
            if(dataSources.isEmpty())
                dataSourcesTable.getSelectionModel().clearSelection();
        }
    }

    protected void addDataSource(DataSource dataSource) {
        TreeItem<DataSource> item = dataSourcesTable.getSelectionModel().getSelectedItem();
        if(item != null) {
            DataSource selectedDataSource = item.getValue();
            if(selectedDataSource != dataSource) {
                dataSources.add(dataSource);
            }
        } else {
            dataSources.add(dataSource);
        }
    }
}
