package de.ossenbeck.dinosimulator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class DinoSimulatorPane extends StackPane {
    private Territory territory;
    private static final int BORDER_SIZE = 20;
    private static final int SIZE = 50;
    private static final int BONE_SIZE = SIZE/3;
    private static final Color TERRITORY_COLOR = Color.rgb(5, 87, 34);

    private static final Image dinoImageEast;
    private static final Image dinoImageSouth;
    private static final Image dinoImageWest;
    private static final Image dinoImageNorth;
    private static final Image rockImage;
    private static final Image boneImage;

    private ObjectProperty<Selection> selectedAction = new SimpleObjectProperty<>(Selection.NONE);
    private Canvas canvas;
    private boolean draggingDino = false;
    private int dinoOriginalRow;
    private int dinoOriginalCol;

    static{
        dinoImageEast = new Image("Trex.png");
        dinoImageSouth = new Image("Trex_south.png");
        dinoImageWest = new Image("Trex_west.png");
        dinoImageNorth = new Image("Trex_north.png");
        rockImage = new Image("Rock.png");
        boneImage = new Image("Bone.png");
    }

    public DinoSimulatorPane(Territory territory){
        this.territory = territory;
        canvas = new Canvas(calcWidth(), calcHeight());
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::canvasPressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::canvasDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::canvasReleased);
        printBoard();
        this.getChildren().add(canvas);
    }

    private int calcWidth(){
        return (2*BORDER_SIZE + territory.getNumberOfCols() * SIZE);
    }

    private int calcHeight(){
        return (2*BORDER_SIZE + territory.getNumberOfRows() * SIZE);
    }

    public void printBoard(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setWidth(calcWidth());
        canvas.setHeight(calcHeight());
        gc.setFill(TERRITORY_COLOR);

        for(int r=0; r<territory.getNumberOfRows(); r++){
            for(int c=0; c<territory.getNumberOfCols(); c++){
                gc.fillRect(BORDER_SIZE + c* SIZE, BORDER_SIZE + r* SIZE, SIZE, SIZE);
                if(territory.getDino().getRow() == r && territory.getDino().getCol() == c && !draggingDino){
                    drawDino(gc, BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE);
                } else if (territory.isRock(r, c)) {
                    gc.drawImage(rockImage,BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE, SIZE, SIZE);
                } else if (territory.getBones(r, c) > 0) {
                    int bones = territory.getBones(r,c);
                    for(int i=0; i< bones; i++){
                        gc.drawImage(boneImage, BORDER_SIZE+c*SIZE + i%3 * BONE_SIZE, BORDER_SIZE+r*SIZE + (double) i /3 * BONE_SIZE, BONE_SIZE, BONE_SIZE);
                    }
                }
            }
        }
        gc.setLineWidth(2.0);

        for(int rows = 0; rows <= territory.getNumberOfRows(); rows++){
            gc.strokeLine(BORDER_SIZE, BORDER_SIZE + rows*SIZE, BORDER_SIZE+territory.getNumberOfCols()*SIZE, BORDER_SIZE+rows*SIZE);
        }
        for(int cols = 0; cols <= territory.getNumberOfCols(); cols++){
            gc.strokeLine(BORDER_SIZE + cols*SIZE, BORDER_SIZE, BORDER_SIZE+cols*SIZE, BORDER_SIZE+territory.getNumberOfRows()*SIZE);
        }

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

    private void canvasPressed(MouseEvent event){
        double x = event.getX();
        double y = event.getY();

        if(isNotInsideTerritory(x, y)){
            System.out.println("Outside of territory");
            return;
        }
        int row = (int) (y-BORDER_SIZE)/SIZE;
        int col = (int) (x-BORDER_SIZE)/SIZE;

        if(territory.getDino().getRow()==row && territory.getDino().getCol() == col){
            draggingDino = true;
            dinoOriginalRow = row;
            dinoOriginalCol = col;
        }
        switch (selectedAction.get()){
            case PLACE_DINO -> territory.placeDino(row, col);
            case PLACE_BONE -> territory.placeBone(row, col);
            case PLACE_ROCK -> territory.placeRock(row, col);
            case DELETE -> territory.removeItem(row, col);
        }
        printBoard();
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

        GraphicsContext gc = canvas.getGraphicsContext2D();
        printBoard();
        drawDino(gc, x - ((double) SIZE / 2), y - ((double) SIZE / 2));
    }

    private void canvasReleased(MouseEvent event){
        if(!draggingDino){
            return;
        }
        double x = event.getX();
        double y = event.getY();
        draggingDino = false;
        if(isNotInsideTerritory(x,y)){
            printBoard();
            return;
        }
        int row = (int) (y-BORDER_SIZE)/SIZE;
        int col = (int) (x-BORDER_SIZE)/SIZE;
        if(!territory.isRock(row, col)){
            territory.placeDino(row,col);
        }
        printBoard();
    }

    private boolean isNotInsideTerritory(double x, double y){
        return (x<BORDER_SIZE || y < BORDER_SIZE || x > BORDER_SIZE + territory.getNumberOfCols()*SIZE || y > BORDER_SIZE + territory.getNumberOfRows()*SIZE);
    }

    private void drawDino(GraphicsContext gc, double x, double y){
        switch(territory.getDino().getOrientation()){
            case EAST -> gc.drawImage(dinoImageEast, x, y, SIZE, SIZE);
            case NORTH -> gc.drawImage(dinoImageNorth, x, y, SIZE, SIZE);
            case WEST -> gc.drawImage(dinoImageWest, x, y, SIZE, SIZE);
            case SOUTH -> gc.drawImage(dinoImageSouth, x, y, SIZE, SIZE);
        }
    }
}