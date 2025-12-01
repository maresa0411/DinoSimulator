package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.DinoSimulatorGame;
import de.ossenbeck.dinosimulator.model.Program;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Notifier;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    protected static final List<String> OPENED_PROGRAMS = new ArrayList<>();
    private static final String DEFAULT_TEXT = "void main(){" +System.lineSeparator()+"}";
    private static final String DEFAULT_NAME = "DefaultDino";
    private static final Path DEFAULT_DINO_FILE = Path.of(SaveLoadController.PROGRAMS_PATH + File.separator + DEFAULT_NAME + SaveLoadController.FILENAME_END);

    /**
     * Creates a new Dino Simulator Game in a new windows with the given name
     * @param title Name of the Game
     */
    public static void newDinoSimulatorGame(String title){
        DinoSimulatorStageView stageView = initialize(title);
        stageView.show();
    }

    /**
     * opens default game with {@code DEFAULT_NAME}
     */
    public static void openDefault(){
        if(!isOpened(DEFAULT_NAME)){
            DinoSimulatorStageView stageView = initialize(DEFAULT_NAME);
            if(Files.exists(DEFAULT_DINO_FILE)){
                String code = SaveLoadController.readFile(DEFAULT_DINO_FILE);
                stageView.getTextArea().setText(code);
            }else{
                try {
                    Files.createFile(DEFAULT_DINO_FILE);
                }catch(IOException _){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Erstellen der default Datei", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                SaveLoadController.save(DEFAULT_TEXT, DEFAULT_NAME);
                stageView.getTextArea().setText(DEFAULT_TEXT);
            }
            stageView.show();
        }
    }

    /**
     * opens Dino Simulator Game with the given title and code if it is not open yet
     * @param program program to be opened
     */

    public static void loadDinoSimulatorGame(Program program){
        if(!isOpened(program.getTitle())) {
            DinoSimulatorStageView stageView = initialize(program.getTitle());
            stageView.getTextArea().setText(program.getCode());
            stageView.show();
        }
    }

    private static DinoSimulatorStageView initialize(String title){
        OPENED_PROGRAMS.add(title);
        DinoSimulatorGame game = new DinoSimulatorGame();
        DinoSimulatorPaneView paneView = new DinoSimulatorPaneView(game);
        DinoSimulatorStageView stageView = new DinoSimulatorStageView(paneView);
        stageView.setTitle(title);
        Notifier notifier = stageView.getNotifier();
        CompileController compileController = new CompileController(stageView, game);
        new TerritoryDesignerController(game, stageView, paneView, notifier);
        new DinoChangeControlller(game, stageView, notifier);
        new StageController(stageView, compileController);
        return stageView;
    }

    public static boolean isOpened(String title){
        if(title == null) {return false;}
        return OPENED_PROGRAMS.contains(title);
    }

    public static void endGame(String title){
        OPENED_PROGRAMS.remove(title);}

    public static boolean allWindowsClosed(){return OPENED_PROGRAMS.isEmpty();}
}
