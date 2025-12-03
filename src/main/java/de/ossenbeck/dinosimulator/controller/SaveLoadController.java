package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Program;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadController {
    public static final String PROGRAMS_PATH = "programs";
    private static final String PREFIX_1 = "public class ";
    private static final String PREFIX_2 = " extends de.ossenbeck.dinosimulator.model.Dino { public" ;
    private static final String POSTFIX = " }";
    public static final String FILENAME_END = ".java";

    // create directory programs if necessary
    static {
        Path path = Path.of(PROGRAMS_PATH);
        if(!Files.exists(path)){
            try{
                Files.createDirectory(path);
            }catch (IOException _){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ordner programs konnte nicht erstellt werden", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    /**
     * Saves the given code in the given file in programs
     * @param code code to be saved
     * @param filename file in programs in which the code will be saved
     * @return if saving was successful
     */
    public static boolean save(String code, String filename){
        code = code.replace("\n", System.lineSeparator());
        ArrayList<String> lines = new ArrayList<>();
        lines.add(PREFIX_1 + filename + PREFIX_2);
        lines.add(code);
        lines.add(POSTFIX);

        try {
            Path path = Path.of(PROGRAMS_PATH + File.separator + filename + FILENAME_END);
            if(!Files.exists(path)){
                Files.createFile(path);
            }
            Files.write(path, lines, StandardCharsets.UTF_8);

        } catch (Exception _) {
            return false;
        }
        return true;
    }

    /**
     * Opens a file chooser and opens a new window with the selected file
     * @return chosen program
     */
    public static Pair<String, String> load(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(PROGRAMS_PATH));
        fileChooser.setTitle("Wähle eine Datei");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile == null){
            return null;
        }
        String filename = selectedFile.toString().substring(selectedFile.toString().lastIndexOf(File.separator) + 1, selectedFile.toString().indexOf(FILENAME_END));
        try {
            Path path = selectedFile.toPath();
            return new Pair<>(filename, readFile(path));

        } catch (Exception _) {
            return null;
        }
    }

    /**
     * Reads the file in the given path if it exists
     * @param path File to be read
     * @return content of the file
     */
    public static String readFile(Path path){
        try{
            if (!Files.exists(path)) {
                return null;
            }
            StringBuilder text = new StringBuilder();
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int l = 0; l < lines.size(); l++) {
                text.append(lines.get(l));
                if (l < lines.size() - 1) {
                    text.append(System.lineSeparator());
                }
            }
            String fullText = text.toString();
            int prefixIndex = fullText.indexOf(PREFIX_2);
            if (prefixIndex == -1) {
                return null;
            }

            int start = prefixIndex + PREFIX_2.length();
            int end = fullText.length() - 1;

            if (end < start) {
                return null;
            }

            return fullText.substring(start, end).trim();
        }catch(IOException _){
            return null;
        }
    }

    public static boolean saveAllFiles(){
        for(Program p : GameController.OPENED_PROGRAMS.values()){
            if(!save(p.getCode(), p.getTitle())){
                return false;
            }
        }
        return true;
    }
}
