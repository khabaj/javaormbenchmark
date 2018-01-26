package com.khabaj.ormbenchmark.launcher.benchmark;

import com.jfoenix.controls.JFXButton;
import com.khabaj.ormbenchmark.launcher.BenchmarkRunner;
import com.khabaj.ormbenchmark.launcher.benchmark.settings.BenchmarkSettings;
import com.khabaj.ormbenchmark.launcher.benchmark.settings.BenchmarkSettingsCtrl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class BenchmarkTabCtrl implements Initializable{

    @FXML
    TextArea consoleTextArea;
    @FXML
    JFXButton startBenchmarkButton;
    @FXML
    JFXButton stopBenchmarkButton;

    BenchmarkRunner benchmarkRunner;

    public static BenchmarkTabCtrl instance;

    private void appendText(String valueOf) {
        Platform.runLater(() -> consoleTextArea.appendText(valueOf));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char) b));
            }
        };

        //System.setOut(new PrintStream(out, true));
        //System.setErr(new PrintStream(out, true));
    }

    @FXML
    public void startBenchmark() {
        startBenchmarkButton.setDisable(true);
        stopBenchmarkButton.setDisable(false);

        BenchmarkSettingsCtrl ctrl = BenchmarkSettingsCtrl.getController();
        BenchmarkSettings benchmarkSettings = ctrl.getBenchmarkSettings();

        benchmarkRunner = new BenchmarkRunner(benchmarkSettings);
        benchmarkRunner.start();
    }

    @FXML
    public void stopBenchmark() {
        //unsafe
        benchmarkRunner.stop();
        startBenchmarkButton.setDisable(false);
        stopBenchmarkButton.setDisable(true);
    }

    @FXML
    public void clearConsole() {
        consoleTextArea.clear();
    }

    public static BenchmarkTabCtrl getInstance() {
        return instance;
    }

    public JFXButton getStartBenchmarkButton() {
        return startBenchmarkButton;
    }

    public JFXButton getStopBenchmarkButton() {
        return stopBenchmarkButton;
    }
}
