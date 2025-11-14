package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.Orientation;
import de.ossenbeck.dinosimulator.model.Territory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MainController {
    private Territory territory;
    private ObjectProperty<Selection> selectedAction;
    private boolean draggingDino;

    private static final int MAX_ROWS_COLS = 100;

    public MainController (Territory territory){
        this.territory = territory;
        selectedAction = new SimpleObjectProperty<>(Selection.NONE);
        draggingDino = false;
        territory.initTest();
    }

    public void setSelectedAction(Selection selectedAction){
        switch (selectedAction){
            case PLACE_DINO -> territory.getNotifier().post("Dino platzieren ausgewählt. Du kannst den Dino auf ein Feld, auf dem kein Felsen liegt, umplatzieren.");
            case PLACE_BONE -> territory.getNotifier().post("Knochen platzieren ausgewählt. Du kannst nun bis zu 9 Knochen auf einem Feld platzieren.");
            case PLACE_ROCK -> territory.getNotifier().post("Felsen platzieren ausgewählt. Du kannst nun einen Stein auf einem beliebigen Feld, außer wo der Dino steht, platzieren.");
            case DELETE -> territory.getNotifier().post("Löschen ausgewählt. Du kannst nun beliebige Felder löschem.");
            case NONE -> territory.onTerritoryChange();
        }
        this.selectedAction.setValue(selectedAction);
    }

    public Selection getSelectedAction(){
        return selectedAction.get();
    }

    public ObjectProperty<Selection> getSelectedActionProperty(){
        return selectedAction;
    }

    public boolean isDinoAt(int row, int col){
        return territory.getDino().getRow() == row && territory.getDino().getCol() == col;
    }

    public boolean isRockAt(int row, int col){
        return territory.isRock(row, col);
    }

    public int getBonesAt(int row, int col){
        return territory.getBones(row, col);
    }

    public boolean isValidBoneInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value >= 0 && value <= Dino.getMaxBones();
        }catch(NumberFormatException _){
            return false;
        }
    }

    public boolean isValidRowColInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value > 0 && value <= MAX_ROWS_COLS;
        } catch (NumberFormatException _) {
            return false;
        }
    }

    public void selectAction(Selection selection){
        if(getSelectedAction() == selection){
            setSelectedAction(Selection.NONE);
        }else{
            setSelectedAction(selection);
        }
    }

    public int getNumberOfRows(){
        return territory.getNumberOfRows();
    }

    public int getNumberOfCols(){
        return territory.getNumberOfCols();
    }

    public boolean getDraggingDino(){
        return draggingDino;
    }

    public void setDraggingDino(boolean draggionDino){
         this.draggingDino = draggionDino;
    }

    public Orientation getOrientation(){
        return territory.getDino().getOrientation();
    }

    public void handleResize(int rows, int cols){
        territory.resize(rows, cols);
    }

    public int getAmountOfBonesOfDino(){
        return territory.getDino().getAmountOfBones();
    }

    public void handleChangeAmountOfBones(int amountOfBones){
        territory.getDino().setAmountOfBones(amountOfBones);
    }

    public int getMaxRowsCols(){
        return MAX_ROWS_COLS;
    }
}
