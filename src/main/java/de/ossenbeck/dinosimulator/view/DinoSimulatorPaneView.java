package de.ossenbeck.dinosimulator.view;

import de.ossenbeck.dinosimulator.controller.DinoSimulatorPaneController;
import de.ossenbeck.dinosimulator.model.Orientation;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class DinoSimulatorPaneView extends StackPane implements ChangeListener {
    private static final int BORDER_SIZE = 50;
    private static final int SIZE = 50;
    private static final int BONE_SIZE = SIZE/3;
    private static final Color TERRITORY_COLOR = Color.rgb(5, 87, 34);

    private final Canvas canvas;
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

    public DinoSimulatorPaneView(){
        canvas = new Canvas(calcWidth(), calcHeight());
        printBoard();
        this.getChildren().add(canvas);
    }

    private int calcWidth(){
        return (2*BORDER_SIZE + controller.getTerritory().getNumberOfCols() * SIZE);
    }

    private int calcHeight(){
        return (2*BORDER_SIZE + controller.getTerritory().getNumberOfRows() * SIZE);
    }

    public void printBoard(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setWidth(calcWidth());
        canvas.setHeight(calcHeight());
        gc.setFill(TERRITORY_COLOR);

        for(int r=0; r<controller.getTerritory().getNumberOfRows(); r++){
            for(int c=0; c<controller.getTerritory().getNumberOfCols(); c++){
                gc.fillRect(BORDER_SIZE + c* SIZE, BORDER_SIZE + r* SIZE, SIZE, SIZE);
                if(controller.isDinoAt(r, c) && !controller.getDraggingDino()){
                    drawDino(gc, BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE);
                } else if (controller.isRockAt(r, c)) {
                    gc.drawImage(rockImage,BORDER_SIZE+c*SIZE, BORDER_SIZE+r*SIZE, SIZE, SIZE);
                } else if (controller.getBonesAt(r, c) > 0) {
                    int bones = controller.getBonesAt(r, c);
                    for(int i=0; i< bones; i++){
                        gc.drawImage(boneImage, BORDER_SIZE+c*SIZE + i%3 * BONE_SIZE, BORDER_SIZE+r*SIZE + i/3 * BONE_SIZE, BONE_SIZE, BONE_SIZE);
                    }
                }
            }
        }
        gc.setLineWidth(2.0);

        for(int rows = 0; rows <= controller.getTerritory().getNumberOfRows(); rows++){
            gc.strokeLine(BORDER_SIZE, BORDER_SIZE + rows*SIZE, BORDER_SIZE+controller.getTerritory().getNumberOfCols()*SIZE, BORDER_SIZE+rows*SIZE);
        }
        for(int cols = 0; cols <= controller.getTerritory().getNumberOfCols(); cols++){
            gc.strokeLine(BORDER_SIZE + cols*SIZE, BORDER_SIZE, BORDER_SIZE+cols*SIZE, BORDER_SIZE+controller.getTerritory().getNumberOfRows()*SIZE);
        }

    }

    public void drawDino(double x, double y, Orientation orientation){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch(orientation){
            case EAST -> gc.drawImage(dinoImageEast, x, y, SIZE, SIZE);
            case NORTH -> gc.drawImage(dinoImageNorth, x, y, SIZE, SIZE);
            case WEST -> gc.drawImage(dinoImageWest, x, y, SIZE, SIZE);
            case SOUTH -> gc.drawImage(dinoImageSouth, x, y, SIZE, SIZE);
        }
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public int getBorderSize(){
        return BORDER_SIZE;
    }

    public int getSize(){
        return SIZE;
    }

    @Override
    public void onTerritoryChanged() {
        printBoard();
    }

    @Override
    public void onActeurChanged(double x, double y, Orientation orientation) {
        drawDino(x, y, orientation);
    }
}