package de.ossenbeck.dinosimulator.view;
import de.ossenbeck.dinosimulator.model.DinoSimulatorGame;
import de.ossenbeck.dinosimulator.model.Orientation;
import de.ossenbeck.dinosimulator.util.DinoDragListener;
import de.ossenbeck.dinosimulator.util.TerritoryChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class DinoSimulatorPaneView extends StackPane implements TerritoryChangeListener, DinoDragListener {
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

    private final DinoSimulatorGame game;

    static{
        dinoImageEast = new Image("Trex.png");
        dinoImageSouth = new Image("Trex_south.png");
        dinoImageWest = new Image("Trex_west.png");
        dinoImageNorth = new Image("Trex_north.png");
        rockImage = new Image("Rock.png");
        boneImage = new Image("Bone.png");
    }

    public DinoSimulatorPaneView(DinoSimulatorGame game){
        this.game = game;
        canvas = new Canvas(calcWidth(), calcHeight());
        printBoard(false);
        this.getChildren().add(canvas);
        game.getTerritory().addListener(this);
    }

    private int calcWidth(){
        return (2*BORDER_SIZE + game.getTerritory().getNumberOfCols() * SIZE);
    }

    private int calcHeight(){
        return (2*BORDER_SIZE + game.getTerritory().getNumberOfRows() * SIZE);
    }

    private void printBoard(boolean draggingDino){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setWidth(calcWidth());
        canvas.setHeight(calcHeight());
        gc.setFill(TERRITORY_COLOR);

        for(int r=0; r<game.getTerritory().getNumberOfRows(); r++){
            for(int c=0; c<game.getTerritory().getNumberOfCols(); c++){
                gc.fillRect(BORDER_SIZE + (double)c* SIZE, BORDER_SIZE + (double)r* SIZE, SIZE, SIZE);
                if(game.getTerritory().isDinoAt(r, c) && !draggingDino){
                    drawDino(BORDER_SIZE+(double)c*SIZE, BORDER_SIZE+(double)r*SIZE, game.getTerritory().getDino().getOrientation());
                } else if (game.getTerritory().isRock(r,c)) {
                    gc.drawImage(rockImage,BORDER_SIZE+(double)c*SIZE, BORDER_SIZE+ (double)r*SIZE, SIZE, SIZE);
                } else if (game.getTerritory().getBones(r,c) > 0) {
                    int bones = game.getTerritory().getBones(r, c);
                    for(int i=0; i< bones; i++){
                        gc.drawImage(boneImage, BORDER_SIZE+c*SIZE + (double)i%3 * BONE_SIZE, BORDER_SIZE+r*SIZE + Math.floor((double)i/3) * BONE_SIZE, BONE_SIZE, BONE_SIZE);
                    }
                }
            }
        }
        gc.setLineWidth(2.0);

        for(int rows = 0; rows <= game.getTerritory().getNumberOfRows(); rows++){
            gc.strokeLine(BORDER_SIZE, BORDER_SIZE + (double) rows*SIZE, BORDER_SIZE+ (double) game.getTerritory().getNumberOfCols()*SIZE, BORDER_SIZE+ (double) rows*SIZE);
        }
        for(int cols = 0; cols <= game.getTerritory().getNumberOfCols(); cols++){
            gc.strokeLine(BORDER_SIZE + (double) cols*SIZE, BORDER_SIZE, BORDER_SIZE+ (double) cols*SIZE, BORDER_SIZE+ (double) game.getTerritory().getNumberOfRows()*SIZE);
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
        printBoard(false);
    }

    @Override
    public void onDinoDrag() {
        printBoard(true);
    }
}