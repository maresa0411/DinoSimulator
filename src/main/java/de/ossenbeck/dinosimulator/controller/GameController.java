package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.DinoSimulatorGame;
import de.ossenbeck.dinosimulator.model.Program;
import de.ossenbeck.dinosimulator.util.Notifier;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class GameController {
    protected static final HashMap<String, Program> OPENED_PROGRAMS = new HashMap<>();
    private static final String DEFAULT_TEXT = "void main(){" +System.lineSeparator()+"}";
    private static final String DEFAULT_NAME = "DefaultDino";
    private static final Path DEFAULT_DINO_FILE = Path.of(SaveLoadController.PROGRAMS_PATH + File.separator + DEFAULT_NAME + SaveLoadController.FILENAME_END);

    /**
     * Creates a new Dino Simulator Game in a new window with the given name if the name does not exist yet
     * @param title Name of the Game
     */
    public static void newDinoSimulatorGame(String title){
        if(Files.exists(Path.of(SaveLoadController.PROGRAMS_PATH + File.separator + title + SaveLoadController.FILENAME_END))){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ein Spiel mit dem Namen " + title + " existiert bereits", ButtonType.OK);
            alert.showAndWait();
        }else{
            SaveLoadController.save(DEFAULT_TEXT, title);
            initialize(title, DEFAULT_TEXT);
        }
    }

    /**
     * opens default game with {@code DEFAULT_NAME}
     */
    public static void openDefault(){

        if(!isOpened(DEFAULT_NAME)){
            String code = SaveLoadController.readFile(DEFAULT_DINO_FILE);

            if(code != null) {
                initialize(DEFAULT_NAME, code);
            } else{
                try {
                    Files.createFile(DEFAULT_DINO_FILE);
                }catch(IOException _){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Erstellen der default Datei", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                SaveLoadController.save(DEFAULT_TEXT, DEFAULT_NAME);
                initialize(DEFAULT_NAME, DEFAULT_TEXT);
            }
        }
    }

    /**
     * opens Dino Simulator Game with the given title and code if it is not open yet
     * @param title title of the program to be opened
     * @param code code in the text area of the program to be opened
     */

    public static void loadDinoSimulatorGame(String title, String code){
        if(!isOpened(title)) {
            if(code.isEmpty()){
                initialize(title, DEFAULT_TEXT);
            }else{
                initialize(title, code);
            }
        }
    }

    private static void initialize(String title, String code){
        DinoSimulatorGame game = new DinoSimulatorGame();
        DinoSimulatorPaneView paneView = new DinoSimulatorPaneView(game);
        DinoSimulatorStageView stageView = new DinoSimulatorStageView(paneView);
        stageView.setTitle(title);
        Notifier notifier = stageView.getNotifier();
        CompileController compileController = new CompileController(stageView, game);
        new TerritoryDesignerController(game, stageView, paneView, notifier);
        new DinoChangeControlller(game, stageView, notifier);
        new StageController(stageView);
        stageView.getTextArea().setText(code);
        OPENED_PROGRAMS.put(title, new Program(stageView));
        if(compileController.compile(title) != null){
            game.getTerritory().setDino(new Dino(game.getTerritory(), 0, 0));
        }
        stageView.show();
    }

    public static boolean isOpened(String title){
        if(title == null) {return false;}
        return OPENED_PROGRAMS.containsKey(title);
    }

    public static void endGame(String title){
        OPENED_PROGRAMS.remove(title);}

    public static void endAllGames(){
        OPENED_PROGRAMS.clear();
    }

    public static boolean allWindowsClosed(){return OPENED_PROGRAMS.isEmpty();}
}
