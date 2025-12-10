package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.scene.control.Slider;

public class SimulationController {
    private DinoSimulatorStageView stage;

    public SimulationController(DinoSimulatorStageView stage){
        this.stage = stage;
        stage.getPauseButton().setOnAction(_ -> pause());
        stage.getStartContinueButton().setOnAction(_ -> start());
        stage.getStopButton().setOnAction(_ -> stop());
        Slider slider = stage.getSlider();
    }

    private void pause(){}

    private void start(){}

    private void stop(){}
}
