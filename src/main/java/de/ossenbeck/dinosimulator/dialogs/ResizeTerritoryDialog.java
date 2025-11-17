package de.ossenbeck.dinosimulator.dialogs;

import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Notifier;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class ResizeTerritoryDialog extends Dialog<Pair<Integer, Integer>> {
    private static final int GAP = 10;
    private static final int MAX_ROWS_COLS = 100;

    //StackOverflow (https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields)
    public ResizeTerritoryDialog(final Territory territory, final Notifier notifier){
        setTitle("Größe des Territoriums anpassen");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label rowLabel = new Label("Anzahl der Reihen (1-" + MAX_ROWS_COLS + "):");
        TextField rows = new TextField();
        rows.setPromptText("1-"+MAX_ROWS_COLS);
        rows.setText(String.valueOf(territory.getNumberOfRows()));

        Label colLabel = new Label("Anzahl der Spalten (1-" + MAX_ROWS_COLS + "):");
        TextField cols = new TextField();
        cols.setPromptText("1-"+MAX_ROWS_COLS);
        cols.setText(String.valueOf(territory.getNumberOfCols()));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.add(rowLabel, 0, 0);
        gridPane.add(rows, 1, 0);
        gridPane.add(colLabel, 0, 1);
        gridPane.add(cols, 1, 1);

        getDialogPane().setContent(gridPane);

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        BooleanBinding invalidInput = Bindings.createBooleanBinding(()-> (!isValidRowColInput(rows.getText()) || !isValidRowColInput(cols.getText())), rows.textProperty(), cols.textProperty());
        okButton.disableProperty().bind(invalidInput);

        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new Pair<>(Integer.parseInt(rows.getText()), Integer.parseInt(cols.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = showAndWait();

        if(result.isPresent()){
            int r = result.get().getKey();
            int c = result.get().getValue();
            territory.resize(r, c);
            notifier.post("Größe des Territoriums auf " + r + "x" + c + " geändert.");
        }
    }

    private boolean isValidRowColInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value > 0 && value <= MAX_ROWS_COLS;
        } catch (NumberFormatException _) {
            return false;
        }
    }
}
