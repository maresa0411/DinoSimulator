package de.ossenbeck.dinosimulator.model;

import java.util.Random;

public class Territory {
    private int[][] territory;
    private Dino dino;
    private final static int DEFAULT_SIZE = 10;
    private final static int ROCK = -1;
    private final static int EMPTY = 0;
    private final static int MAX_BONES = 9;

    public Territory(){
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    /**
     * @param rows Number of rows (must be > 0).
     * @param cols Number of columns (must be > 0).
     * @throws IllegalArgumentException when {@code rows} or {@code cols} < 0.
     */

    public Territory(int rows, int cols){
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException("Rows and columns must be > 0");
        }else {
            territory = new int[rows][cols];
            reset();
        }
    }

    public void reset(){
        for(int r=0; r < getNumberOfRows(); r++){
            for(int c=0; c < getNumberOfCols(); c++){
                territory[r][c] = EMPTY;
            }
        }
        dino = new Dino(this, 0,0);
    }

    /**
     *
     * @param newRows Number of rows (must be greater than zero).
     * @param newCols Number of columns (must be greater than zero).
     * @throws IllegalArgumentException when {@code newRows} or {@code newCols} is <=0.
     */
    public void resize(int newRows, int newCols){
        if(newRows <= 0 || newCols <= 0){
            throw new IllegalArgumentException("Number of rows and columns must be greater than zero!");
        }
        int[][] newTerritory = new int[newRows][newCols];
        for(int i=0; i<newRows; i++){
            for(int j=0; j<newCols; j++){
                if(i < getNumberOfRows() && j < getNumberOfCols()) {
                    newTerritory[i][j] = territory[i][j];
                }else{
                    newTerritory[i][j] = 0;
                }
            }
        }
        if(dino.getRow() >= newRows || dino.getCol() >= newCols){
            if(newTerritory[0][0] == ROCK){
                newTerritory[0][0] = EMPTY;
            }
            dino.setPosition(0,0);
        }
        territory = newTerritory;
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public void placeRock(int row, int col){
        checkRowAndCol(row, col);
        if(dino.getRow() != row && dino.getCol() != col) {
            territory[row][col] = ROCK;
        }
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     * @throws TooManyBonesException when the maximal amount {@code MAX_BONES} of bones on a tile is reached.
     */
    public void placeBone(int row, int col){
        checkRowAndCol(row, col);
        if(territory[row][col] == ROCK){
            territory[row][col] = EMPTY;
        }
        if(territory[row][col] < MAX_BONES) {
            territory[row][col]++;
        }else{
            throw new TooManyBonesException();
        }
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     * @throws RockInTheWayException when there is a rock on the desired tile.
     */
    public void placeDino(int row, int col){
        checkRowAndCol(row, col);
        if(territory[row][col] != ROCK) {
            dino.setPosition(row, col);
        }else{
            throw new RockInTheWayException();
        }
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public void removeItem(int row, int col){
        checkRowAndCol(row, col);
        territory[row][col] = EMPTY;
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     * @throws NoBonesThereException when there are no bones on the selected tile.
     */
    public void removeBone(int row, int col){
        checkRowAndCol(row, col);
        if(territory[row][col] > EMPTY){
            territory[row][col]--;
        }else{
            throw new NoBonesThereException();
        }
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public boolean isRock(int row, int col){
        checkRowAndCol(row, col);
        return territory[row][col] == ROCK;
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public boolean isEmpty(int row, int col){
        checkRowAndCol(row, col);
        return territory[row][col] == EMPTY;
    }
    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public int getBones(int row, int col){
        checkRowAndCol(row, col);
        return territory[row][col];
    }

    public Dino getDino(){
        return dino;
    }

    public int getNumberOfRows(){
        return territory.length;
    }

    public int getNumberOfCols(){
        return territory[0].length;
    }

    /**
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    private void checkRowAndCol(int row, int col){
        if (row < 0 || row >= getNumberOfRows() || col < 0 || col >= getNumberOfCols()) {
            throw new IllegalArgumentException("Invalid position");
        }
    }

    public void initTest(){
        Random random = new Random();
        for(int i=0; i<8; i++) {
            placeRock(random.nextInt(getNumberOfRows()), random.nextInt(getNumberOfRows()));
        }
        for(int i=0; i<30; i++) {
            placeBone(random.nextInt(getNumberOfRows()), random.nextInt(getNumberOfRows()));
        }
        for(int i=0; i<random.nextInt(3); i++){
            dino.turnLeft();
        }
    }
}