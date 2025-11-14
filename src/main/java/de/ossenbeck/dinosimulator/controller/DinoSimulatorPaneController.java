package de.ossenbeck.dinosimulator.controller;

import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorPaneView;
import javafx.scene.input.MouseEvent;

public class DinoSimulatorPaneController {
    private Territory territory;
    private final DinoSimulatorPaneView pane;
    private final MainController mainController;

    public DinoSimulatorPaneController(Territory territory, DinoSimulatorPaneView pane, MainController mainController){
        this.territory = territory;
        this.pane = pane;
        this.mainController = mainController;

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
            mainController.setDraggingDino(true);
            territory.getNotifier().post("Ziehe den Dino auf ein beliebiges Feld, auf dem kein Felsen steht.");
        }
        switch (mainController.getSelectedAction()){
            case PLACE_DINO -> territory.placeDino(row, col);
            case PLACE_BONE -> territory.placeBone(row, col);
            case PLACE_ROCK -> territory.placeRock(row, col);
            case DELETE -> territory.removeItem(row, col);
            case NONE -> territory.onTerritoryChange();
        }
    }

    private void canvasDragged(MouseEvent event){
        if(!mainController.getDraggingDino()){
            return;
        }
        double x = event.getX();
        double y = event.getY();

        if(isNotInsideTerritory(x,y)){
            territory.getNotifier().post("Dino außerhalb des Territoriums.");
        }else {
            territory.getNotifier().post("Ziehe den Dino auf ein beliebiges Feld, auf dem kein Felsen steht.");
            territory.onActorChange(x - ((double) pane.getSize() / 2), y - ((double) pane.getSize() / 2));
        }
    }

    private void canvasReleased(MouseEvent event){
        if(!mainController.getDraggingDino()){
            return;
        }
        double x = event.getX();
        double y = event.getY();
        mainController.setDraggingDino(false);
        if(isNotInsideTerritory(x,y)){
            territory.getNotifier().post("Hier kann der Dino nicht platziert werden.");
            territory.onTerritoryChange();
            return;
        }
        int row = (int) (y-pane.getBorderSize())/pane.getSize();
        int col = (int) (x-pane.getBorderSize())/pane.getSize();
        if(!territory.isRock(row, col)){
            territory.placeDino(row,col);
        }else{
            territory.getNotifier().post("Felsen im Weg!");
        }
        territory.onTerritoryChange();
    }

    private boolean isNotInsideTerritory(double x, double y){
        return (x<pane.getBorderSize() || y < pane.getBorderSize() || x > pane.getBorderSize() + territory.getNumberOfCols()*pane.getSize() || y > pane.getBorderSize() + territory.getNumberOfRows()*pane.getSize());
    }
}
