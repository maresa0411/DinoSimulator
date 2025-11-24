package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Platform;

public class StageController {
    private final DinoSimulatorStageView stage;
    public StageController(final DinoSimulatorStageView stage){
        this.stage = stage;
        stage.getQuitMenuItem().setOnAction(_ -> Platform.exit());
    }
}