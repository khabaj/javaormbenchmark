package com.khabaj.ormbenchmark.launcher.results;

import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsMenuCtrl implements Initializable {

    @FXML
    JFXTreeView<String> resultsMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        TreeItem<String> operations = new TreeItem<>("Operations");

        operations.getChildren().add(new TreeItem<>("insertOneRow"));
        operations.getChildren().add(new TreeItem<>("insert100Row"));

        rootItem.getChildren().add(operations);
        rootItem.getChildren().add(new TreeItem<>("Persistence providers"));
        rootItem.getChildren().add(new TreeItem<>("Databases (DBMS)"));

        resultsMenu.setRoot(rootItem);
        resultsMenu.setShowRoot(false);

    }
}
