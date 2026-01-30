package de.ossenbeck.dinosimulator.model;

import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;

public record Program(DinoSimulatorStageView stage) {

    public String getTitle() {
        return stage.getTitle();
    }

    public String getCode() {
        return stage.getTextArea().getText();
    }
}
