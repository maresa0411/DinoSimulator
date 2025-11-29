package de.ossenbeck.dinosimulator.dialogs;

import de.ossenbeck.dinosimulator.controller.GameController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import javax.lang.model.SourceVersion;
import java.util.Optional;

public class NewGameDialog extends TextInputDialog {

    public NewGameDialog(){
        setTitle("Neues Spiel erstellen");
        setHeaderText("Spielname");
        getEditor().setPromptText("Gib einen Namen ein");

        Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        BooleanBinding invalidInput = Bindings.createBooleanBinding(() -> isNotValidNameInput(this.getEditor().getText()), this.getEditor().textProperty());
        okButton.disableProperty().bind(invalidInput);
        Optional<String> result = this.showAndWait();

        result.ifPresent(GameController::newDinoSimulatorGame);
    }

    //mit ChatGPT generiert am 24.11.2025
    private boolean isNotValidNameInput(String name){
        if (name == null || name.isBlank() || SourceVersion.isKeyword(name) || !SourceVersion.isIdentifier(name)) {
            return true;
        }
        return false;
    }
}
