package de.ossenbeck.dinosimulator.main;

import de.ossenbeck.dinosimulator.controller.DesignController;
import de.ossenbeck.dinosimulator.controller.DinoChangeControlller;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Notifier;
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
        DinoSimulatorPaneView paneView = new DinoSimulatorPaneView(territory);
        DinoSimulatorStageView stageView = new DinoSimulatorStageView(territory, paneView);
        Notifier notifier = stageView.getNotifier();
        DesignController designController = new DesignController(territory, stageView, paneView, notifier);
        new DinoChangeControlller(territory, stageView, notifier);
        designController.addDinoDragListener(paneView);
        territory.addListener(paneView);
        stageView.show();
    }
}