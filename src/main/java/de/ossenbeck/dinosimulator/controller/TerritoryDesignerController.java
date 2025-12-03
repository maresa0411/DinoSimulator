package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.dialogs.ResizeTerritoryDialog;
import de.ossenbeck.dinosimulator.model.*;
import de.ossenbeck.dinosimulator.util.DinoDragListener;
import de.ossenbeck.dinosimulator.util.Notifier;
import de.ossenbeck.dinosimulator.view.DinoContextMenu;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class TerritoryDesignerController {
    private final DinoSimulatorGame game;
    private final DinoSimulatorStageView stage;
    private final DinoSimulatorPaneView pane;
    private final Notifier notifier;

    private final List<DinoDragListener> listener;

    private ObjectProperty<Selection> selectedAction;
    private boolean draggingDino;

    public TerritoryDesignerController(DinoSimulatorGame game, DinoSimulatorStageView stage, DinoSimulatorPaneView pane, Notifier notifier){
        this.game = game;
        this.stage = stage;
        this.pane = pane;
        this.notifier = notifier;

        listener = new ArrayList<>();
        listener.add(pane);
        selectedAction = new SimpleObjectProperty<>(Selection.NONE);
        draggingDino = false;

        addActionsToMenuItems();
        addActionsToButtons();

        synchronizingButtonsAndMenuItems();

        pane.getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, this::canvasPressed);
        pane.getCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::canvasDragged);
        pane.getCanvas().addEventHandler(MouseEvent.MOUSE_RELEASED, this::canvasReleased);

        pane.setOnContextMenuRequested(this::contextMenuRequest);
    }

    private void canvasPressed(MouseEvent event){
        double x = event.getX();
        double y = event.getY();

        if(isNotInsideTerritory(x, y)){
            return;
        }
        int row = (int) (y-pane.getBorderSize())/pane.getSize();
        int col = (int) (x-pane.getBorderSize())/pane.getSize();

        if(game.getTerritory().getDino().getRow()==row && game.getTerritory().getDino().getCol() == col){
            draggingDino = true;
            onDragDino();
            notifier.post("Ziehe den Dino auf ein beliebiges Feld, auf dem kein Felsen steht.");
        }
        switch (getSelectedAction()){
            case PLACE_DINO -> {
                try{
                    game.getTerritory().placeDino(row, col);
                    notifier.post(placeItemOnText("Dino", row, col));
                }catch(DinoTerritoryException e){
                    notifier.post(e.getMessage());
                }
            }
            case PLACE_BONE -> {
                try{
                    game.getTerritory().placeBone(row, col);
                    notifier.post(placeItemOnText("Knochen", row, col));
                }catch(DinoTerritoryException e){
                    notifier.post(e.getMessage());
                }
            }
            case PLACE_ROCK -> {
                try{
                    game.getTerritory().placeRock(row, col);
                    notifier.post(placeItemOnText("Felsen", row, col));
                }catch(DinoTerritoryException e){
                    notifier.post(e.getMessage());
                }
            }
            case DELETE -> {
                game.getTerritory().removeItem(row, col);
                notifier.post("Feld ("+row+"|"+col+") wurde gelöscht.");
            }
            case NONE -> {//if none is selected, nothing has to be done
            }
        }
    }

    private void canvasDragged(MouseEvent event){
        if(!draggingDino){
            return;
        }
        onDragDino();
        double x = event.getX();
        double y = event.getY();

        if(isNotInsideTerritory(x,y)){
            notifier.post("Dino außerhalb des Territoriums.");
        }else {
            notifier.post("Ziehe den Dino auf ein beliebiges Feld, auf dem kein Felsen steht.");
            pane.drawDino(x - ((double) pane.getSize() / 2), y - ((double) pane.getSize() / 2), game.getTerritory().getDino().getOrientation());
        }
    }

    private void canvasReleased(MouseEvent event){
        if(!draggingDino){
            return;
        }
        onDragDino();
        double x = event.getX();
        double y = event.getY();

        draggingDino = false;
        if(isNotInsideTerritory(x,y)){
            notifier.post("Hier kann der Dino nicht platziert werden.");
            game.getTerritory().onTerritoryChange();
            return;
        }
        int row = (int) (y-pane.getBorderSize())/pane.getSize();
        int col = (int) (x-pane.getBorderSize())/pane.getSize();
        if(!game.getTerritory().isRock(row, col)){
            game.getTerritory().placeDino(row,col);
        }else{
            notifier.post("Felsen im Weg!");
        }
        game.getTerritory().onTerritoryChange();
    }

    private void contextMenuRequest(ContextMenuEvent event){
        double dinoXMin = (double) pane.getBorderSize() + game.getTerritory().getDino().getCol()*pane.getSize();
        double dinoYMin = (double) pane.getBorderSize() + game.getTerritory().getDino().getRow()*pane.getSize();
        if(event.getX() > dinoXMin && event.getX() < (dinoXMin + pane.getSize()) && event.getY() > dinoYMin && event.getY() < (dinoYMin + pane.getSize())){
            DinoContextMenu contextMenu = new DinoContextMenu(game);
            contextMenu.show(pane.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        }
    }

    private boolean isNotInsideTerritory(double x, double y){
        return (x<pane.getBorderSize() || y < pane.getBorderSize() || x > pane.getBorderSize() + game.getTerritory().getNumberOfCols()*pane.getSize() || y > pane.getBorderSize() + game.getTerritory().getNumberOfRows()*pane.getSize());
    }

    public void setSelectedAction(Selection selectedAction){
        switch (selectedAction){
            case PLACE_DINO -> notifier.post("Dino platzieren ausgewählt. Du kannst den Dino auf ein Feld, auf dem kein Felsen liegt, umplatzieren.");
            case PLACE_BONE -> notifier.post("Knochen platzieren ausgewählt. Du kannst nun bis zu 9 Knochen auf einem Feld platzieren.");
            case PLACE_ROCK -> notifier.post("Felsen platzieren ausgewählt. Du kannst nun einen Stein auf einem beliebigen Feld, außer wo der Dino steht, platzieren.");
            case DELETE -> notifier.post("Löschen ausgewählt. Du kannst nun beliebige Felder löschem.");
            case NONE -> {//if none is selected, nothing has to be done
            }
        }
        this.selectedAction.setValue(selectedAction);
    }

    public Selection getSelectedAction(){
        return selectedAction.get();
    }

    public ObjectProperty<Selection> getSelectedActionProperty(){
        return selectedAction;
    }

    public void selectAction(Selection selection){
        if(getSelectedAction() == selection){
            setSelectedAction(Selection.NONE);
        }else{
            setSelectedAction(selection);
        }
    }

    private void addActionsToMenuItems(){
        stage.getChangeSizeMenuItem().setOnAction(_ -> new ResizeTerritoryDialog(game.getTerritory(), notifier));

        stage.getPlaceDinoCheckMenuItem().setOnAction(_ -> selectAction(Selection.PLACE_DINO));
        stage.getPlaceBoneCheckMenuItem().setOnAction(_ -> selectAction(Selection.PLACE_BONE));
        stage.getPlaceRockCheckMenuItem().setOnAction(_ -> selectAction(Selection.PLACE_ROCK));
        stage.getDeleteCheckMenuItem().setOnAction(_ -> selectAction(Selection.DELETE));
    }

    private void addActionsToButtons(){
        stage.getAdjustSizeButton().setOnAction(_ -> new ResizeTerritoryDialog(game.getTerritory(), notifier));

        stage.getPlaceDinoButton().setOnAction(_ -> selectAction(Selection.PLACE_DINO));
        stage.getPlaceBoneButton().setOnAction(_ -> selectAction(Selection.PLACE_BONE));
        stage.getPlaceRockButton().setOnAction(_ -> selectAction(Selection.PLACE_ROCK));
        stage.getDeleteButton().setOnAction(_ -> selectAction(Selection.DELETE));
    }


    private void synchronizingButtonsAndMenuItems(){
        getSelectedActionProperty().addListener(((_, _, newValue) ->
        {
            stage.getPlaceDinoCheckMenuItem().setSelected(newValue == Selection.PLACE_DINO);
            stage.getPlaceBoneCheckMenuItem().setSelected(newValue == Selection.PLACE_BONE);
            stage.getPlaceRockCheckMenuItem().setSelected(newValue == Selection.PLACE_ROCK);
            stage.getDeleteCheckMenuItem().setSelected(newValue == Selection.DELETE);

            stage.getPlaceDinoButton().setSelected(newValue == Selection.PLACE_DINO);
            stage.getPlaceBoneButton().setSelected(newValue == Selection.PLACE_BONE);
            stage.getPlaceRockButton().setSelected(newValue == Selection.PLACE_ROCK);
            stage.getDeleteButton().setSelected(newValue == Selection.DELETE);

            switch(newValue){
                case PLACE_DINO -> stage.getPlaceDinoButton().requestFocus();
                case PLACE_BONE -> stage.getPlaceBoneButton().requestFocus();
                case PLACE_ROCK -> stage.getPlaceRockButton().requestFocus();
                case DELETE -> stage.getDeleteButton().requestFocus();
                case NONE -> new ToggleButton().requestFocus();
            }
        }));
    }

    private String placeItemOnText(String item, int row, int col){
        return item + " auf (" + row + "|" + col +") platziert";
    }

    private void onDragDino() {
        for(DinoDragListener l : listener){
            l.onDinoDrag();
        }
    }

    public void addDinoDragListener(DinoDragListener l){
        listener.add(l);
    }
}
