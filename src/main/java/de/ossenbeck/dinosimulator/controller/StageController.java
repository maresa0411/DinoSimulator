package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.dialogs.NewGameDialog;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;

import java.util.Optional;

public class StageController {
    private final DinoSimulatorStageView stage;
    public StageController(final DinoSimulatorStageView stage, SimulationController simulationController){
        this.stage = stage;
        stage.getQuitMenuItem().setOnAction(_ -> closeAllWindows());
        stage.setOnCloseRequest(event -> {
            if(!closeWindow()){
                event.consume();
            }else{
                simulationController.stopSimulation();
            }
        });
        stage.getNewButton().setOnAction(_ -> new NewGameDialog());
        stage.getNewMenuItem().setOnAction(_ -> new NewGameDialog());
        stage.getOpenMenuItem().setOnAction(_ -> loadProgram());
        stage.getOpenButton().setOnAction(_ -> loadProgram());
        stage.getSaveButton().setOnAction(_-> {
            if(!SaveLoadController.save(stage.getTextArea().getText(), stage.getTitle())){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Das Speichern hat nicht geklappt!", ButtonType.OK);
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, stage.getTitle()+ " wurde erfolreich gespeichert!", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    private void loadProgram(){
        Pair<String, String> program = SaveLoadController.load();
        if (GameController.isOpened(program.getKey())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Programm is bereits geöffnet", ButtonType.OK);
            alert.showAndWait();
        }else{
            GameController.loadDinoSimulatorGame(program.getKey(), program.getValue());
        }
    }

    private boolean closeWindow(){
        if(!SaveLoadController.save(stage.getTextArea().getText(), stage.getTitle())){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Das Speichern hat nicht geklappt!", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                if(result.get().equals(ButtonType.OK)){
                    GameController.endGame(stage.getTitle());
                    stage.close();
                    if(GameController.allWindowsClosed()) {
                        Platform.exit();
                    }
                }else {return false;}
            }
        }
        GameController.endGame(stage.getTitle());
        return true;
    }

    private void closeAllWindows(){
        if(!SaveLoadController.saveAllFiles()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Das Speichern hat nicht geklappt!", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && !result.get().equals(ButtonType.OK)){
                    return;
            }
        }
        GameController.endAllGames();
        Platform.exit();
    }
}