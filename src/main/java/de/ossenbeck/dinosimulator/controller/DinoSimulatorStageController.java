package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Platform;
import javafx.scene.control.ToggleButton;

public class DinoSimulatorStageController {
    public DinoSimulatorStageController(Territory territory, DinoSimulatorStageView stage, MainController mainController){
        stage.getStopMenuItem().setOnAction(_ -> Platform.exit());
        stage.getChangeSizeMenuItem().setOnAction(_ -> stage.changeSizeDialog());

        stage.getPlaceDinoCheckMenuItem().setOnAction(_ -> mainController.selectAction(Selection.PLACE_DINO));
        stage.getPlaceBoneCheckMenuItem().setOnAction(_ -> mainController.selectAction(Selection.PLACE_BONE));
        stage.getPlaceRockCheckMenuItem().setOnAction(_ -> mainController.selectAction(Selection.PLACE_ROCK));
        stage.getDeleteCheckMenuItem().setOnAction(_ -> mainController.selectAction(Selection.DELETE));

        stage.getTurnLeftMenuItem().setOnAction(_-> territory.getDino().turnLeft());
        stage.getAdjustAmountOfBonesMenuItem().setOnAction(_ -> stage.changeAmountOfBonesDialog());
        stage.getForwardMenuItem().setOnAction(_-> territory.getDino().moveForward());
        stage.getPickUpBoneButton().setOnAction(_-> territory.getDino().pickUpBone());
        stage.getPutDownBoneButton().setOnAction(_-> territory.getDino().putDownBone());
        stage.getAdjustSizeButton().setOnAction(_ -> stage.changeSizeDialog());

        stage.getPlaceDinoButton().setOnAction(_ -> mainController.selectAction(Selection.PLACE_DINO));
        stage.getPlaceBoneButton().setOnAction(_ -> mainController.selectAction(Selection.PLACE_BONE));
        stage.getPlaceRockButton().setOnAction(_ -> mainController.selectAction(Selection.PLACE_ROCK));
        stage.getDeleteButton().setOnAction(_ -> mainController.selectAction(Selection.DELETE));
        stage.getAdjustAmountOfBonesButton().setOnAction(_-> stage.changeAmountOfBonesDialog());
        stage.getTurnLeftButton().setOnAction(_-> territory.getDino().turnLeft());
        stage.getMoveForwardButton().setOnAction(_-> territory.getDino().moveForward());
        stage.getPickUpBoneButton().setOnAction(_-> territory.getDino().pickUpBone());
        stage.getPutDownBoneButton().setOnAction(_-> territory.getDino().putDownBone());

        //synchronizing buttons and menu items
        mainController.getSelectedActionProperty().addListener(((_, _, newValue) ->
        {
            stage.getPlaceDinoCheckMenuItem().setSelected(newValue == Selection.PLACE_DINO);
            stage.getPlaceBoneCheckMenuItem().setSelected(newValue == Selection.PLACE_BONE);
            stage.getPlaceRockCheckMenuItem().setSelected(newValue == Selection.PLACE_ROCK);
            stage.getDeleteCheckMenuItem().setSelected(newValue == Selection.DELETE);

            stage.getPlaceDinoButton().setSelected(newValue == Selection.PLACE_DINO);
            stage.getPlaceBoneButton().setSelected(newValue == Selection.PLACE_BONE);
            stage.getPlaceRockButton().setSelected(newValue == Selection.PLACE_ROCK);
            stage.getDeleteButton().setSelected(newValue == Selection.DELETE);

            switch(newValue){
                case PLACE_DINO -> stage.getPlaceDinoButton().requestFocus();
                case PLACE_BONE -> stage.getPlaceBoneButton().requestFocus();
                case PLACE_ROCK -> stage.getPlaceRockButton().requestFocus();
                case DELETE -> stage.getDeleteButton().requestFocus();
                case NONE -> new ToggleButton().requestFocus();
            }
        }));
    }
}
