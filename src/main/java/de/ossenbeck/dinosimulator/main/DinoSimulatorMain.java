package de.ossenbeck.dinosimulator.main;

import de.ossenbeck.dinosimulator.controller.DinoSimulatorPaneController;
import de.ossenbeck.dinosimulator.controller.DinoSimulatorStageController;
import de.ossenbeck.dinosimulator.controller.MainController;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Application;
import javafx.stage.Stage;

public class DinoSimulatorMain extends Application {

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
        MainController mainController = new MainController(territory);
        DinoSimulatorPaneView paneView = new DinoSimulatorPaneView(mainController);
        territory.addListener(paneView);
        DinoSimulatorStageView stageView = new DinoSimulatorStageView(mainController, paneView);
        territory.setNotifier(stageView.getNotifier());
        new DinoSimulatorStageController(territory, stageView, mainController);
        new DinoSimulatorPaneController(territory, paneView, mainController);
        stageView.show();
    }
}