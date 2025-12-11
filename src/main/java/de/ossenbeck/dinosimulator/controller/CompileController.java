package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.Territory;
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
    private final Territory territory;
    private final SimulationController simulationController;

    public CompileController(DinoSimulatorStageView stage, Territory territory, SimulationController simulationController){
        this.stage = stage;
        this.territory = territory;
        this.simulationController = simulationController;
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
        String file = SaveLoadController.PROGRAMS_PATH + File.separator + filename + SaveLoadController.FILENAME_END;
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean success = javac.run(null, null, err, file) == 0;
        if (!success) {
            return err.toString();
        }
        Dino newDino = loadClass(filename);
        territory.replaceDino(newDino);
        return null;
    }

    /**
     * Loads the given file as dino
     * @param filename Class to be loaded
     * @return Returns loaded class extending dino
     */
    private Dino loadClass(String filename) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(SaveLoadController.PROGRAMS_PATH).toURI().toURL() })) {
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
     * stops simulation if it is running,
     * shows alert with errors if compiling was not successful,
     * shows alert with information success if compiling was successful
     */
    private void compileFile(){
        simulationController.stopSimulation();
        SaveLoadController.save(stage.getTextArea().getText(), stage.getTitle());
        String err = compile(stage.getTitle());
        if(err != null){
            Alert alert = new Alert(Alert.AlertType.ERROR, err, ButtonType.OK);
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kompilierung erfolgreich!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
