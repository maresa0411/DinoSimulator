package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class DinoSimulatorPaneController {
    private Territory territory;
    private final DinoSimulatorPaneView pane;
    private ObjectProperty<Selection> selectedAction = new SimpleObjectProperty<>(Selection.NONE);
    private boolean draggingDino = false;


    public DinoSimulatorPaneController(DinoSimulatorPane pane, Territory territory){
        this.pane = pane;
        this.territory = territory;
        pane.getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, this::canvasPressed);
        pane.getCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::canvasDragged);
        pane.getCanvas().addEventHandler(MouseEvent.MOUSE_RELEASED, this::canvasReleased);
    }

    private void canvasPressed(MouseEvent event){
        double x = event.getX();
        double y = event.getY();

        if(isNotInsideTerritory(x, y)){
            return;
        }
        int row = (int) (y-pane.getBorderSize())/pane.getSize();
        int col = (int) (x-pane.getBorderSize())/pane.getSize();

        if(territory.getDino().getRow()==row && territory.getDino().getCol() == col){
            draggingDino = true;
        }
        switch (selectedAction.get()){
            case PLACE_DINO -> territory.placeDino(row, col);
            case PLACE_BONE -> territory.placeBone(row, col);
            case PLACE_ROCK -> territory.placeRock(row, col);
            case DELETE -> territory.removeItem(row, col);
        }
        pane.printBoard();
    }

    private void canvasDragged(MouseEvent event){
        if(!draggingDino){
            return;
        }
        double x = event.getX();
        double y = event.getY();

        if(isNotInsideTerritory(x,y)){
            return;
        }

        GraphicsContext gc = pane.getCanvas().getGraphicsContext2D();
        pane.printBoard();
        pane.drawDino(gc, x - ((double) pane.getSize() / 2), y - ((double) pane.getSize() / 2));
    }

    private void canvasReleased(MouseEvent event){
        if(!draggingDino){
            return;
        }
        double x = event.getX();
        double y = event.getY();
        draggingDino = false;
        if(isNotInsideTerritory(x,y)){
            pane.printBoard();
            return;
        }
        int row = (int) (y-pane.getBorderSize())/pane.getSize();
        int col = (int) (x-pane.getBorderSize())/pane.getSize();
        if(!territory.isRock(row, col)){
            territory.placeDino(row,col);
        }
        pane.printBoard();
    }

    private boolean isNotInsideTerritory(double x, double y){
        return (x<pane.getBorderSize() || y < pane.getBorderSize() || x > pane.getBorderSize() + territory.getNumberOfCols()*pane.getSize() || y > pane.getBorderSize() + territory.getNumberOfRows()*pane.getSize());
    }

    public void setSelectedAction(Selection selectedAction){
        this.selectedAction.setValue(selectedAction);
    }

    public Selection getSelectedAction(){
        return selectedAction.get();
    }

    public ObjectProperty<Selection> getSelectedActionProperty(){
        return selectedAction;
    }

    public Territory getTerritory(){
        return territory;
    }

    public boolean getDraggingDino(){
        return draggingDino;
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
}
