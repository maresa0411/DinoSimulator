package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.dialogs.ChangeAmountOfBonesDialog;
import de.ossenbeck.dinosimulator.model.*;
import de.ossenbeck.dinosimulator.util.Notifier;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;

public class DinoChangeControlller {
    private final Territory territory;
    private final DinoSimulatorStageView stage;
    private final Notifier notifier;

    public DinoChangeControlller(Territory territory, DinoSimulatorStageView stage, Notifier notifier){
        this.territory = territory;
        this.stage = stage;
        this.notifier = notifier;
        addActionsToMenuItems();
        addActionsToButtons();
    }

    private void addActionsToMenuItems(){
        stage.getTurnLeftMenuItem().setOnAction(_-> {
            territory.getDino().turnLeft();
            printAction(Action.TURN_LEFT);
        });
        stage.getForwardMenuItem().setOnAction(_-> {
            try{
                territory.getDino().moveForward();
                printAction(Action.MOVE_FORWARD);
            }catch(DinoTerritoryException e){
                notifier.post(e.getMessage());
            }
        });

        stage.getAdjustAmountOfBonesMenuItem().setOnAction(_ -> new ChangeAmountOfBonesDialog(territory, notifier));
    }

    private void addActionsToButtons(){
        stage.getTurnLeftButton().setOnAction(_-> {
            territory.getDino().turnLeft();
            printAction(Action.TURN_LEFT);
        });
        stage.getMoveForwardButton().setOnAction(_-> {
            try{
                territory.getDino().moveForward();
                printAction(Action.MOVE_FORWARD);
            }catch(DinoTerritoryException e){
                notifier.post(e.getMessage());
            }
        });
        stage.getPickUpBoneButton().setOnAction(_-> {
            try{
                territory.getDino().pickUpBone();
                printAction(Action.PICK_UP_BONE);
            }catch(DinoTerritoryException e){
                notifier.post(e.getMessage());
            }
        });
        stage.getPutDownBoneButton().setOnAction(_-> {
            try{
                territory.getDino().putDownBone();
                printAction(Action.PUT_DOWN_BONE);
            }catch(DinoTerritoryException e){
                notifier.post(e.getMessage());
            }
        });

        stage.getAdjustAmountOfBonesButton().setOnAction(_-> new ChangeAmountOfBonesDialog(territory, notifier));

    }

    private void printAction(Action action){
        switch(action){
            case MOVE_FORWARD -> notifier.post("Dino vorwärts bewegt.");
            case TURN_LEFT -> notifier.post("Dino nach links gedreht.");
            case PICK_UP_BONE -> notifier.post("Knochen aufgehoben.");
            case PUT_DOWN_BONE -> notifier.post("Knochen abgelegt.");
            case null, default -> notifier.post("");
        }
    }
}
