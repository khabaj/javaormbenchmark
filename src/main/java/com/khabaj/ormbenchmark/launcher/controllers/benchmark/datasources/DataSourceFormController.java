package com.khabaj.ormbenchmark.launcher.controllers.benchmark.datasources;

import com.jfoenix.controls.JFXComboBox;
import com.khabaj.ormbenchmark.launcher.controllers.benchmark.JDBCDriver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DataSourceFormController implements Initializable{

    @FXML
    JFXComboBox jdbcDriverComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(JDBCDriver jdbcDriver : JDBCDriver.values()) {
            jdbcDriverComboBox.getItems().add(jdbcDriver.getFullText());
        }
    }
}
