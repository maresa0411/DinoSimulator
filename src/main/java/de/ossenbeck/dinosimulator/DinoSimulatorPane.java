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
        canvas = new Canvas(calcWidth(), calcHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(TERRITORY_COLOR);

        for(int r=0; r<territory.getNumberOfRows(); r++){
            for(int c=0; c<territory.getNumberOfCols(); c++){
                gc.fillRect(BORDER_SIZE + c* SIZE, BORDER_SIZE + r* SIZE, SIZE, SIZE);
                if(territory.getDino().getRow() == r && territory.getDino().getCol() == c){
                    switch(territory.getDino().getOrientation()){
                        case EAST -> gc.drawImage(dinoImageEast, BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE, SIZE, SIZE);
                        case NORTH -> gc.drawImage(dinoImageNorth, BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE, SIZE, SIZE);
                        case WEST -> gc.drawImage(dinoImageWest, BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE, SIZE, SIZE);
                        case SOUTH -> gc.drawImage(dinoImageSouth, BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE, SIZE, SIZE);
                    }
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

        if(x<BORDER_SIZE || y < BORDER_SIZE || x > BORDER_SIZE + territory.getNumberOfCols()*SIZE || y > BORDER_SIZE + territory.getNumberOfRows()*SIZE){
            return;
        }
        int row = (int) (y-BORDER_SIZE)/SIZE;
        int col = (int) (x-BORDER_SIZE)/SIZE;

        switch (selectedAction.get()){
            case PLACE_DINO -> territory.placeDino(row, col);
            case PLACE_BONE -> territory.placeBone(row, col);
            case PLACE_ROCK -> territory.placeRock(row, col);
            case DELETE -> territory.removeItem(row, col);
        }
        printBoard();
    }
}
