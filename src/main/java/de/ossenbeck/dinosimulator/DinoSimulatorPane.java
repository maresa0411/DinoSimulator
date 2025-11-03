package de.ossenbeck.dinosimulator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
        Canvas canvas = new Canvas(calcWidth(), calcHeight());
        printBoard(canvas);
        this.getChildren().add(canvas);
    }

    private int calcWidth(){
        return (2*BORDER_SIZE + territory.getNumberOfCols() * SIZE);
    }

    private int calcHeight(){
        return (2*BORDER_SIZE + territory.getNumberOfRows() * SIZE);
    }

    private void printBoard(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
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
}
