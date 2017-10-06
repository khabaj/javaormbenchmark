package com.khabaj.ormbenchmark.launcher.controllers.benchmark;

import com.khabaj.ormbenchmark.launcher.BenchmarkRunner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class BenchmarkController implements Initializable{

    @FXML
    TextArea consoleTextArea;

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
    public void startBenchmark() throws RunnerException {

        BenchmarkRunner benchmarkRunner = new BenchmarkRunner();
        benchmarkRunner.start();
    }

    @FXML
    public void clearConsole() {
        consoleTextArea.clear();
    }
}
