package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Slider;

public class SimulationController {
    private final DinoSimulatorStageView stage;
    private Territory territory;
    private Simulation simulation = null;

    private static final int MAX_SPEED = 1000;
    private static final int MIN_SPEED = 1;
    private static final int DEF_SPEED = 500;

    private volatile int speed = DEF_SPEED;
    private final BooleanProperty simulationRunning;

    public SimulationController(DinoSimulatorStageView stage, Territory territory){
        this.stage = stage;
        this.territory = territory;

        stage.getPauseButton().setOnAction(_ -> pauseSimulation());
        stage.getStartContinueButton().setOnAction(_ -> startOrContinueSimulation());
        stage.getStopButton().setOnAction(_ -> stopSimulation());

        stage.getPauseMenuItem().setOnAction(_ -> pauseSimulation());
        stage.getStartContinueMenuItem().setOnAction(_ -> startOrContinueSimulation());
        stage.getStopDinoMenuItem().setOnAction(_ -> stopSimulation());

        Slider slider = stage.getSlider();
        slider.setMax(MAX_SPEED);
        slider.setMin(MIN_SPEED);
        slider.setValue(speed);
        slider.valueProperty().addListener((_, _ , n) -> speed = n.intValue());
        simulationRunning = new SimpleBooleanProperty(false);
    }

    private void pauseSimulation(){
        stage.getStartContinueButton().setDisable(false);
        stage.getStartContinueMenuItem().setDisable(false);
        stage.getPauseButton().setDisable(true);
        stage.getPauseMenuItem().setDisable(true);
        stage.getStopButton().setDisable(false);
        stage.getStopDinoMenuItem().setDisable(false);
        simulation.setPause(true);
        simulationRunning.setValue(false);
    }

    private void startOrContinueSimulation(){
        stage.getStartContinueButton().setDisable(true);
        stage.getStartContinueMenuItem().setDisable(true);
        stage.getPauseButton().setDisable(false);
        stage.getPauseMenuItem().setDisable(false);
        stage.getStopButton().setDisable(false);
        stage.getStopDinoMenuItem().setDisable(false);
        speed = (int) stage.getSlider().getValue();
        simulationRunning.setValue(true);
        if(simulation == null){
            simulation = new Simulation(territory, this);
            simulation.setDaemon(true);
            simulation.start();
        }else{
            simulation.setPause(false);
            synchronized (simulation){
                simulation.notify();
            }
        }
    }

    public void stopSimulation(){
        if(simulation == null){
            return;
        }
        stage.getStartContinueButton().setDisable(false);
        stage.getStartContinueMenuItem().setDisable(false);
        stage.getPauseButton().setDisable(true);
        stage.getPauseMenuItem().setDisable(true);
        stage.getStopButton().setDisable(true);
        stage.getStopDinoMenuItem().setDisable(true);
        simulation.setStop(true);
        simulation.setPause(false);
        simulationRunning.setValue(false);
        synchronized (simulation){
            simulation.interrupt();
        }
    }

    public int getSpeed() {
        return Math.max((MAX_SPEED - speed), MIN_SPEED);
    }

    public void simEnded(){
        Platform.runLater(() -> {
            stage.getStartContinueButton().setDisable(false);
            stage.getStartContinueMenuItem().setDisable(false);
            stage.getPauseButton().setDisable(true);
            stage.getPauseMenuItem().setDisable(true);
            stage.getStopButton().setDisable(true);
            stage.getStopDinoMenuItem().setDisable(true);
        });
        simulation = null;
    }

    public BooleanProperty isSimulationRunning(){
        return simulationRunning;
    }
}