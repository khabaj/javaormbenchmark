package com.khabaj.ormbenchmark.launcher.results;

import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ResultsMenuCtrl implements Initializable {

    @FXML
    JFXTreeView<String> resultsMenuTreeView;

    ResultsService resultsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resultsService = ResultsService.getInstance();
        resultsMenuTreeView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showResults(newValue));

        refreshMenu();
    }

    public void refreshMenu() {
        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        resultsMenuTreeView.setRoot(rootItem);
        resultsMenuTreeView.setShowRoot(false);

        TreeItem<String> operationsItem = new TreeItem<>(MenuCategory.OPERATIONS);
        Set<String> operations = resultsService.getOperations();
        operations.forEach((operation) -> operationsItem.getChildren().add(new TreeItem<>(operation)));

        TreeItem<String> persistenceProvidersItem = new TreeItem<>(MenuCategory.PERSISTENCE_PROVIDERS);
        Set<String> persistenceProviders = resultsService.getPersistenceProviders();
        persistenceProviders.forEach((persistenceProvider) -> persistenceProvidersItem.getChildren().add(new TreeItem<>(persistenceProvider)));

        TreeItem<String> databasesItem = new TreeItem<>(MenuCategory.DATABASES);
        Set<String> databases = resultsService.getDataSources();
        databases.forEach((dbName) -> databasesItem.getChildren().add(new TreeItem<>(dbName)));

        rootItem.getChildren().add(operationsItem);
        rootItem.getChildren().add(persistenceProvidersItem);
        rootItem.getChildren().add(databasesItem);

    }

    private void showResults(TreeItem<String> selectedItem) {
        ResultsTabCtrl resultsTabCtrl = ResultsTabCtrl.getInstance();
        resultsTabCtrl.showResults(selectedItem);
    }
}
