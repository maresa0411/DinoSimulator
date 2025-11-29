package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class CompileController {
    private final DinoSimulatorStageView stage;

    public CompileController (DinoSimulatorStageView stage){
        this.stage = stage;
        stage.getCompileMenuItem().setOnAction(_ -> compileFile());
        stage.getCompileButton().setOnAction(_ -> compileFile());
    }

    /**
     * Compiles the given filename in programs
     * @param filename File to be compiled
     * @return
     * null if compiling was successful,
     * string of errors if compiling was not successful
     */
    public String compile(String filename) {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        if (javac == null){
            return "Kein Java-Compiler gefunden";
        }
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean success = javac.run(null, null, err, filename) == 0;
        if (!success) {
            return err.toString();
        }
        return null;
    }

    /**
     * Compiles currently opened file,
     * shows alert with errors if compiling was not successful,
     * shows alert with information success if compiling was successful
     */
    private void compileFile(){
        SaveLoadController.save(stage.getTextArea().getText(), stage.getTitle());
        String err = compile(SaveLoadController.PROGRAMS_PATH + File.separator + stage.getTitle() + SaveLoadController.FILENAME_END);
        if(err != null){
            Alert alert = new Alert(Alert.AlertType.ERROR, err, ButtonType.OK);
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kompilierung erfolgreich!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
