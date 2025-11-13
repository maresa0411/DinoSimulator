package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.controller.Selection;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Platform;
import javafx.scene.control.ToggleButton;

public class DinoSimulatorStageController {
    private Territory territory;
    private final DinoSimulatorStageView stage;

    public DinoSimulatorStageController(Territory territory, DinoSimulatorStageView stage){
        this.territory = territory;
        this.stage = stage;
        territory.initTest();

        stage.getStopMenuItem().setOnAction(_ -> Platform.exit());
        stage.getChangeSizeMenuItem().setOnAction(_ -> stage.changeSizeDialog());

        stage.getPlaceDinoCheckMenuItem().setOnAction(_ -> selectAction(Selection.PLACE_DINO));
        stage.getPlaceBoneCheckMenuItem().setOnAction(_ -> selectAction(Selection.PLACE_BONE));
        stage.getPlaceRockCheckMenuItem().setOnAction(_ -> selectAction(Selection.PLACE_ROCK));
        stage.getDeleteCheckMenuItem().setOnAction(_ -> selectAction(Selection.DELETE));

        stage.getTurnLeftMenuItem().setOnAction(_-> {territory.getDino().turnLeft(); stage.getPane().printBoard();});
        stage.getAdjustAmountOfBonesMenuItem().setOnAction(_ -> stage.changeAmountOfBonesDialog());
        stage.getForwardMenuItem().setOnAction(_-> {territory.getDino().moveForward(); stage.getPane().printBoard();});
        stage.getPickUpBoneButton().setOnAction(_-> {territory.getDino().pickUpBone(); stage.getPane().printBoard();});
        stage.getPutDownBoneButton().setOnAction(_-> {territory.getDino().putDownBone(); stage.getPane().printBoard();});
        stage.getAdjustSizeButton().setOnAction(_ -> stage.changeSizeDialog());
        stage.getPlaceDinoButton().setOnAction(_ -> selectAction(Selection.PLACE_DINO));
        stage.getPlaceBoneButton().setOnAction(_ -> selectAction(Selection.PLACE_BONE));
        stage.getPlaceRockButton().setOnAction(_ -> selectAction(Selection.PLACE_ROCK));
        stage.getDeleteButton().setOnAction(_ -> selectAction(Selection.DELETE));
        stage.getAdjustAmountOfBonesButton().setOnAction(_-> stage.changeAmountOfBonesDialog());
        stage.getTurnLeftButton().setOnAction(_-> {territory.getDino().turnLeft(); stage.getPane().printBoard();});
        stage.getMoveForwardButton().setOnAction(_-> {territory.getDino().moveForward(); stage.getPane().printBoard();});
        stage.getPickUpBoneButton().setOnAction(_-> {territory.getDino().pickUpBone(); stage.getPane().printBoard();});
        stage.getPutDownBoneButton().setOnAction(_-> {territory.getDino().putDownBone(); stage.getPane().printBoard();});

        //synchronizing buttons and menu items
        stage.getPane().getSelectedActionProperty().addListener(((_, _, newValue) ->
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

    public boolean isValidBoneInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value >= 0 && value <= Dino.getMaxBones();
        }catch(NumberFormatException e){
            return false;
        }
    }

    public boolean isValidRowColInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value > 0 && value <= stage.getMaxRowsCols();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void selectAction(Selection selection){
        if(dinoSimulatorPane.getSelectedAction() == selection){
            dinoSimulatorPane.setSelectedAction(Selection.NONE);
        }else{
            dinoSimulatorPane.setSelectedAction(selection);
        }
    }

}
