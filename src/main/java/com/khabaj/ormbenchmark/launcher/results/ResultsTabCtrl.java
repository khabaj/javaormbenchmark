package com.khabaj.ormbenchmark.launcher.results;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsTabCtrl implements Initializable{

    private static ResultsTabCtrl instance;

    private String resultsDirectoryPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.instance = this;
    }

    public void loadResults() {

    }

    public String getResultsDirectoryPath() {
        return resultsDirectoryPath;
    }

    public void setResultsDirectoryPath(String resultsDirectoryPath) {
        this.resultsDirectoryPath = resultsDirectoryPath;
    }

    public static ResultsTabCtrl getInstance() {
        return instance;
    }
}
