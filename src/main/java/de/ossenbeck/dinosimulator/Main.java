package de.ossenbeck.dinosimulator;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // VM Args: --module-path "\path\to\javafx-sdk-24\lib" --add-modules javafx.controls,javafx.fxml
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        newDinoSimulatorGame();
    }

    public static void newDinoSimulatorGame(){
        Territory territory = new Territory();
        DinoSimulatorStage stage = new DinoSimulatorStage(territory);
        stage.show();
    }
}