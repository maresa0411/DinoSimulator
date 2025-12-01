package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.DinoSimulatorGame;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CompileController {
    private final DinoSimulatorStageView stage;
    private final DinoSimulatorGame game;

    public CompileController(DinoSimulatorStageView stage, DinoSimulatorGame game){
        this.stage = stage;
        this.game = game;
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
        Dino newDino = loadClass(filename);
        if(newDino != null){
            game.getTerritory().setDino(newDino);
        }else{
            game.getTerritory().setDino(new Dino(game.getTerritory(), 0, 0));
        }
        return null;
    }

    private Dino loadClass(String filename){
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(".").toURI().toURL() })) {
            return (Dino) classLoader.loadClass(filename).getConstructor().newInstance();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Compiles currently opened file,
     * shows alert with errors if compiling was not successful,
     * shows alert with information success if compiling was successful
     */
    void compileFile(){
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
