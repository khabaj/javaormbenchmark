package com.khabaj.ormbenchmark.launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void init() throws Exception {
        ReflectionsService.getInstance().initialize();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.FINEST);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            BorderPane rootElement = loader.load();

            Scene scene = new Scene(rootElement, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/fxml/styles.css").toExternalForm());

            primaryStage.setTitle("Java Persistence Benchmark");
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(800);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            this.primaryStage = primaryStage;
            primaryStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
