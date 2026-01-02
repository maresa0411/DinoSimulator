package de.ossenbeck.dinosimulator.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.ossenbeck.dinosimulator.model.Orientation;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class SerializationController {

    private static FileChooser fileChooser = null;

    static {
        fileChooser = new FileChooser();
        File dir = new File(".");
        fileChooser.setInitialDirectory(dir);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.ser", "*.ser");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public SerializationController(final Territory territory, final DinoSimulatorStageView stage,
                                   final SimulationController simController) {
        stage.getSerializeMenuItem().setOnAction(_ -> saveTerritory(territory, stage));
        stage.getDeserializeMenuItem().setOnAction(_ -> loadTerritory(territory, stage));
        stage.getDeserializeMenuItem().disableProperty().bind(simController.isSimulationRunning());
    }

    public void saveTerritory(final Territory territory, final DinoSimulatorStageView stage) {
        fileChooser.setTitle("Territorium serialisiert speichern");
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        if (!file.getName().endsWith(".ser")) {
            file = new File(file.getAbsolutePath().concat(".ser"));
        }
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file))) {
            os.writeObject(territory.getTerritoryField());
            os.writeInt(territory.getDino().getRow());
            os.writeInt(territory.getDino().getCol());
            os.writeInt(territory.getDino().getAmountOfBones());
            os.writeObject(territory.getDino().getOrientation());
        } catch (Exception exc) {
            exc.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Fehler beim Speichern!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void loadTerritory(final Territory territory, final DinoSimulatorStageView stage) {
        fileChooser.setTitle("Serialisiertes Territorium laden");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
                int[][] territoryField = (int[][]) is.readObject();
                territory.setTerritoryField(territoryField);
                territory.getDino().setPosition(is.readInt(), is.readInt());
                territory.getDino().setAmountOfBones(is.readInt());
                territory.getDino().setOrientation((Orientation) is.readObject());
            } catch (Exception exc) {
                exc.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Ungültige Datei!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

}
