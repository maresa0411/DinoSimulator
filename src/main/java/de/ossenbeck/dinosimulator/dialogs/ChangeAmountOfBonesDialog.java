package de.ossenbeck.dinosimulator.dialogs;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Notifier;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class ChangeAmountOfBonesDialog extends TextInputDialog {
    private static final ImageView dinoWithBones;

    static{
        dinoWithBones = new ImageView("TrexWithBone.png");
        dinoWithBones.setFitWidth(50);
        dinoWithBones.setFitHeight(50);
    }

    public ChangeAmountOfBonesDialog(Territory territory, Notifier notifier){
        this.setTitle("Knochenmenge anpassen");
        this.setHeaderText("Gib die gewünschte Anzahl an Knochen ein (0-" + Dino.getMaxBones() + ")");
        this.setGraphic(dinoWithBones);
        this.getEditor().setPromptText("0-" + Dino.getMaxBones());
        this.getEditor().setText(String.valueOf(territory.getDino().getAmountOfBones()));

        // mit ChatGPT
        Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        BooleanBinding invalidInput = Bindings.createBooleanBinding(() -> !isValidBoneInput(this.getEditor().getText()), this.getEditor().textProperty());
        okButton.disableProperty().bind(invalidInput);
        Optional<String> result = this.showAndWait();

        result.ifPresent(s -> {
            int amount = Integer.parseInt(s);
            territory.getDino().setAmountOfBones(amount);
            notifier.post("Anzahl der Knochen im Maul des Dinos auf " + amount +" geändert.");
        });
    }

    private boolean isValidBoneInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value >= 0 && value <= Dino.getMaxBones();
        }catch(NumberFormatException _){
            return false;
        }
    }
}
