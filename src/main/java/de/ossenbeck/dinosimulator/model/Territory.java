package de.ossenbeck.dinosimulator.model;

import de.ossenbeck.dinosimulator.util.TerritoryChangeListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Territory {
    private int[][] territoryField;
    private Dino dino;
    private static final int DEFAULT_SIZE = 10;
    private static final int ROCK = -1;
    private static final int EMPTY = 0;
    private static final int MAX_BONES = 9;
    private transient List<TerritoryChangeListener> listener;

    public Territory(){
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    /**
     * @param rows Number of rows (must be > 0).
     * @param cols Number of columns (must be > 0).
     * @throws IllegalArgumentException when {@code rows} or {@code cols} < 0.
     */

    public Territory(int rows, int cols){
        listener = new CopyOnWriteArrayList<>();
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException("Rows and columns must be > 0");
        }else {
            territoryField = new int[rows][cols];
            reset();
        }
    }

    public void setDino(Dino dino){
        synchronized (this){
            this.dino = dino;
            dino.setTerritory(this);
        }
        onTerritoryChange();
    }

    public void reset(){
        synchronized (this) {
            for (int r = 0; r < getNumberOfRows(); r++) {
                for (int c = 0; c < getNumberOfCols(); c++) {
                    territoryField[r][c] = EMPTY;
                }
            }
            dino = new Dino(this, 0, 0);
        }
        onTerritoryChange();
    }

    /**
     *
     * @param newRows Number of rows (must be greater than zero).
     * @param newCols Number of columns (must be greater than zero).
     * @throws IllegalArgumentException when {@code newRows} or {@code newCols} is <=0.
     */
    public void resize(int newRows, int newCols){
        synchronized (this) {
            if (newRows <= 0 || newCols <= 0) {
                throw new IllegalArgumentException("Number of rows and columns must be greater than zero!");
            }
            int[][] newTerritory = new int[newRows][newCols];
            for (int i = 0; i < newRows; i++) {
                for (int j = 0; j < newCols; j++) {
                    if (i < getNumberOfRows() && j < getNumberOfCols()) {
                        newTerritory[i][j] = territoryField[i][j];
                    } else {
                        newTerritory[i][j] = 0;
                    }
                }
            }
            if (dino.getRow() >= newRows || dino.getCol() >= newCols) {
                if (newTerritory[0][0] == ROCK) {
                    newTerritory[0][0] = EMPTY;
                }
                dino.setPosition(0, 0);
            }
            territoryField = newTerritory;
        }
        onTerritoryChange();
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public void placeRock(int row, int col){
        synchronized (this) {
            checkRowAndCol(row, col);
            if (dino.getRow() != row || dino.getCol() != col) {
                territoryField[row][col] = ROCK;
            } else {
                throw new DinoInTheWayException();
            }
        }
        onTerritoryChange();
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     * @throws TooManyBonesException when the maximal amount {@code MAX_BONES} of bones on a tile is reached.
     */
    public void placeBone(int row, int col){
        synchronized (this) {
            checkRowAndCol(row, col);
            if (territoryField[row][col] == ROCK) {
                territoryField[row][col] = EMPTY;
            }
            if (territoryField[row][col] < MAX_BONES) {
                territoryField[row][col]++;
            } else {
                throw new TooManyBonesException();
            }
        }
        onTerritoryChange();
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     * @throws RockInTheWayException when there is a rock on the desired tile.
     */
    public void placeDino(int row, int col) {
        synchronized (this){
            checkRowAndCol(row, col);
            if (territoryField[row][col] != ROCK) {
                dino.setPosition(row, col);
            } else {
                throw new RockInTheWayException();
            }
        }
        onTerritoryChange();
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public void removeItem(int row, int col){
        synchronized (this) {
            checkRowAndCol(row, col);
            territoryField[row][col] = EMPTY;
        }
        onTerritoryChange();
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     * @throws NoBonesThereException when there are no bones on the selected tile.
     */
    public void removeBone(int row, int col) {
        synchronized (this) {
            checkRowAndCol(row, col);
            if (territoryField[row][col] > EMPTY) {
                territoryField[row][col]--;
            } else {
                throw new NoBonesThereException();
            }
        }
        onTerritoryChange();
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public boolean isRock(int row, int col){
        synchronized (this) {
            checkRowAndCol(row, col);
            return territoryField[row][col] == ROCK;
        }
    }

    public synchronized boolean isDinoAt(int row, int col){
        return (dino.getRow() == row && dino.getCol() == col);
    }

    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public synchronized boolean isEmpty(int row, int col){
        checkRowAndCol(row, col);
        return territoryField[row][col] == EMPTY;
    }
    /**
     *
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    public synchronized int getBones(int row, int col){
        checkRowAndCol(row, col);
        return territoryField[row][col];
    }

    public synchronized Dino getDino(){
        return dino;
    }

    public synchronized int getNumberOfRows(){
        return territoryField.length;
    }

    public synchronized int getNumberOfCols(){
        return territoryField[0].length;
    }

    /**
     * @param row Row (must be >= 0 and < numberOfRows).
     * @param col Column (must be >= 0 and < numberOfCols).
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0 or >= number of rows/columns.
     */
    private synchronized void checkRowAndCol(int row, int col){
        if(territoryField == null){
            return;
        }
        if (row < 0 || row >= territoryField.length || col < 0 || col >= territoryField[0].length) {
            throw new IllegalArgumentException("Invalid position");
        }
    }
    public void addListener(TerritoryChangeListener listener){
        this.listener.add(listener);
    }

    /**
     * Transfers the territory change listeners from the current to the new territory.
     * @param newTerritory the new territory the listeners will be transferred to
     */
    public void transferTerritoryChangeListener(Territory newTerritory){
        for(TerritoryChangeListener l:listener){
            newTerritory.addListener(l);
        }
        listener.clear();
        newTerritory.onTerritoryChange();
    }

    /**
     * Called to notify all territory change listeners about a change of the territory.
     */
    public void onTerritoryChange(){
        for(TerritoryChangeListener l:listener){
            l.onTerritoryChanged();
        }
    }

    public int getMaxBones(){
        return MAX_BONES;
    }

    /**
     * Replaces the current dino with the new Dino and transfers all attributes.
     * @param newDino the new dino
     */
    public synchronized void replaceDino(Dino newDino){
        if(newDino == null){
            newDino = new Dino();
        }
        newDino.setPosition(dino.getRow(), dino.getCol());
        newDino.setAmountOfBones(dino.getAmountOfBones());
        newDino.setOrientation(dino.getOrientation());
        newDino.setTerritory(this);
        dino = newDino;
    }

    public void deleteListener(TerritoryChangeListener l) {
        listener.remove(l);
    }

    /**
     * Returns the data structure the territory is saved as.
     * @return a two-dimensional int array
     */
    public synchronized int[][] getTerritoryField(){return territoryField;}

    public void setTerritoryField(int[][] newTerritoryField){
        synchronized (this) {
            this.territoryField = newTerritoryField;
        }
        onTerritoryChange();
    }

    /**
     * Replaces the current values of the territory with the values of the new territory.
     * @param newTerritory the new territory
     */
    public synchronized void replaceTerritory(Territory newTerritory) {
        this.territoryField = newTerritory.getTerritoryField();
        this.dino = newTerritory.getDino();
        this.dino.setTerritory(this);
    }
}