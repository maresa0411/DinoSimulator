package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.DinoTerritoryException;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.TerritoryChangeListener;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Simulation extends Thread implements TerritoryChangeListener {
    private Territory territory;
    private final SimulationController simulationController;
    private volatile boolean pause;
    private volatile boolean stop;

    private final MediaPlayer errorSound;

    public Simulation(Territory territory, SimulationController simulationController) {
        this.territory = territory;
        this.simulationController = simulationController;

        errorSound = new MediaPlayer(new Media(new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "death.wav").toURI().toString()));
    }

    public void setPause(boolean pause){this.pause = pause;}

    public void setStop(boolean stop){this.stop = stop;}

    @Override
    public void run() {
        Dino dino = territory.getDino();
        territory.addListener(this);
        try {
            Method mainMethod = dino.getClass().getDeclaredMethod("main");
            mainMethod.setAccessible(true);
            mainMethod.invoke(dino);
        }
        catch(DinoTerritoryException _){
            errorSound.play();
        }
        catch (StoppedException _) {}
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getCause().getMessage(), ButtonType.OK);
            Platform.runLater(alert::showAndWait);
        } finally {
            territory.deleteListener(this);
            simulationController.simEnded();
        }
    }

    @Override
    public void onTerritoryChanged() {
        if (Platform.isFxApplicationThread()) {
            return;
        }
        if(stop){
            throw new StoppedException();
        }
        try {
            Thread.sleep(simulationController.getSpeed());
        } catch (InterruptedException _) {
            throw new StoppedException();
        }
        while (pause) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException _) {
                    interrupt();
                }
            }
            if(stop){
                throw new StoppedException();
            }
        }
        if(stop) {
            throw new StoppedException();
        }
    }
}