package com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DataSourcesController {

    @FXML
    public void showAddConnectionForm () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/benchmark/DataSourceForm.fxml"));
        AnchorPane rootElement = loader.load();

        Scene scene = new Scene(rootElement);
        Stage stage = new Stage();
        stage.setTitle("Data Source");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
}
