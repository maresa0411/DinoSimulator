package de.ossenbeck.dinosimulator.model;

import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;

public class Program {
    private DinoSimulatorStageView stage;

    public Program(DinoSimulatorStageView stage) {
        this.stage = stage;
    }

    public String getTitle() {
        return stage.getTitle();
    }

    public String getCode() {
        return stage.getTextArea().getText();
    }
}
