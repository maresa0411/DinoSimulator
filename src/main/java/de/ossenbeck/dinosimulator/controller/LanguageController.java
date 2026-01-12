package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.util.Notifier;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;

import java.util.Locale;

public class LanguageController {
    public LanguageController(DinoSimulatorStageView stage){
        stage.getLanguageGroup().selectedToggleProperty().addListener((_, _, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (newValue == stage.getLanguageEnglishMenuItem()) {
                ResourcesController.getResourcesController().setLocale(Locale.ENGLISH);
            } else if (newValue == stage.getLanguageGermanMenuItem()) {
                ResourcesController.getResourcesController().setLocale(Locale.GERMAN);
            }
        });
    }
}
