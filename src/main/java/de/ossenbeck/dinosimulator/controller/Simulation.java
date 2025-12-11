package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.DinoTerritoryException;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.TerritoryChangeListener;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

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
        territory.addListener(this);
        try {
            territory.getDino().main();
        }
        catch(DinoTerritoryException _){
            errorSound.play();
        }
        catch (StoppedException _) {}
        finally {
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
            interrupt();
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