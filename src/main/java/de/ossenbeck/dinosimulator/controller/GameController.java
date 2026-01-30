package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.Program;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Notifier;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

public class GameController {
    protected static final ConcurrentHashMap<String, Program> OPENED_PROGRAMS = new ConcurrentHashMap<>();
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
     * Opens default game with {@code DEFAULT_NAME}.
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
        Territory territory = new Territory();
        DinoSimulatorPaneView paneView = new DinoSimulatorPaneView(territory);
        DinoSimulatorStageView stageView = new DinoSimulatorStageView(paneView);
        Notifier notifier = stageView.getNotifier();
        SimulationController simulationController = new SimulationController(stageView, territory);
        CompileController compileController = new CompileController(stageView, territory, simulationController);
        new SerializationController(territory, stageView, simulationController);
        new TerritoryDesignerController(territory, stageView, paneView, notifier, simulationController);
        new DinoChangeControlller(territory, stageView, notifier);
        new StageController(stageView, simulationController);
        new DBSerializationController(territory, stageView, simulationController);
        new LanguageController(stageView, notifier);
        OPENED_PROGRAMS.put(title, new Program(stageView));
        Platform.runLater(() -> {
            stageView.setTitle(title);
            stageView.getTextArea().setText(code);
            if (compileController.compile(title) != null) {
                territory.setDino(new Dino(territory, 0, 0));
            }
            stageView.show();
        });
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
