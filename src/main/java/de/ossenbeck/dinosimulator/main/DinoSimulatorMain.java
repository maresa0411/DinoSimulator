package de.ossenbeck.dinosimulator.main;

import de.ossenbeck.dinosimulator.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class DinoSimulatorMain extends Application {

    // VM Args: --module-path "\path\to\javafx-sdk-24\lib" --add-modules javafx.controls,javafx.fxml
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameController.openDefault();
    }
}