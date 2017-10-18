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
import java.io.PrintStream;
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

    private void appendText(String valueOf) {
        Platform.runLater(() -> consoleTextArea.appendText(valueOf));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char) b));
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    @FXML
    public void startBenchmark() {
        //startBenchmarkButton.setDisable(true);

        BenchmarkSettingsCtrl ctrl = BenchmarkSettingsCtrl.getController();
        BenchmarkSettings benchmarkSettings = ctrl.getBenchmarkSettings();

        benchmarkRunner = new BenchmarkRunner(benchmarkSettings);
        benchmarkRunner.start();
    }

    @FXML
    public void stopBenchmark() {
        benchmarkRunner.interrupt();
        startBenchmarkButton.setDisable(false);
    }

    @FXML
    public void clearConsole() {
        consoleTextArea.clear();
    }
}
