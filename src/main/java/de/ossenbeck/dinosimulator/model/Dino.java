package de.ossenbeck.dinosimulator.model;

import de.ossenbeck.dinosimulator.util.Invisible;

public class Dino {
    private int row;
    private int col;
    private int amountOfBones;
    private Orientation orientation;
    private transient Territory territory;
    private static final int MAX_BONES = 100;

    public Dino(){
        territory = new Territory();
        setPosition(0, 0);
        resetAmountOfBones();
        orientation = Orientation.EAST;
    }

    /**
     * @param territory Territory
     * @param row Row (must be >= 0 and < {@code territoryRows})
     * @param col Column (must be >= 0) < {@code territoryCols})
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0
     */

    public Dino(Territory territory, int row, int col){
        this.territory = territory;
        setPosition(row, col);
        resetAmountOfBones();
        orientation = Orientation.EAST;
    }

    /**
     * Moves the dino one tile forward, based on the current orientation.
     * @throws RockInTheWayException when there is a rock on the following tile.
     * @throws EndOfTerritoryException when the end of the territory is reached.
     */
    public void moveForward() throws RockInTheWayException, EndOfTerritoryException{
        synchronized (territory) {
            switch (orientation) {
                case EAST -> moveForwardHelp(row, col + 1);
                case SOUTH -> moveForwardHelp(row + 1, col);
                case WEST -> moveForwardHelp(row, col - 1);
                case NORTH -> moveForwardHelp(row - 1, col);
            }
        }
        territory.onTerritoryChange();
    }

    private void moveForwardHelp(int nextRow, int nextCol) throws RockInTheWayException, EndOfTerritoryException{
        if(nextRow < territory.getNumberOfRows() && nextCol < territory.getNumberOfCols() && nextCol >= 0 && nextRow >=0){
            if(!territory.isRock(nextRow, nextCol)) {
                setPosition(nextRow, nextCol);
            }else{
                throw new RockInTheWayException();
            }
        }else{
            throw new EndOfTerritoryException();
        }
    }

    public void turnLeft(){
        synchronized (territory) {
            orientation = orientation.turnLeft();
        }
        territory.onTerritoryChange();
    }

    /**
     * Checks if the dino can move one tile forward
     * @return if the dino can move forward
     */
    protected boolean canMoveForward(){
        synchronized (territory) {
            int nextRow = getRow();
            int nextCol = getCol();
            switch (orientation) {
                case EAST -> nextCol++;
                case SOUTH -> nextRow++;
                case WEST -> nextCol--;
                case NORTH -> nextRow--;
            }
            return nextRow < territory.getNumberOfRows() && nextCol < territory.getNumberOfCols() && nextCol >= 0 && nextRow >= 0 && !territory.isRock(nextRow, nextCol);
        }

    }

    protected synchronized boolean isMouthEmpty(){
        return amountOfBones == 0;
    }

    protected boolean boneThere(){
        synchronized (territory){
            return territory.getBones(getRow(), getCol()) > 0;
        }
    }

    /**
     * Makes the dino pick up a bone, if possible.
     * @throws MouthFullException when the maximum amount of bones {@code MAX_BONES} the dino can carry is reached.
     * @throws NoBonesThereException when the selected tile does not contain any bones.
     */
    public void pickUpBone() throws MouthEmptyException, NoBonesThereException{
        synchronized (territory) {
            if (territory.getBones(row, col) > 0) {
                if (amountOfBones + 1 < MAX_BONES) {
                    amountOfBones++;
                    territory.removeBone(row, col);
                } else {
                    throw new MouthFullException();
                }
            } else {
                throw new NoBonesThereException();
            }
        }
        territory.onTerritoryChange();
    }

    /**
     * Maked the dino put down a bone.
     * @throws TooManyBonesException if there is already the maximum amount of bones on the current tile
     * @throws MouthEmptyException if the dino does not have any bones in its mouth.
     */
    public void putDownBone() throws TooManyBonesException, MouthEmptyException{
        synchronized (territory) {
            if (amountOfBones > 0) {
                if (territory.getBones(row, col) < territory.getMaxBones()) {
                    territory.placeBone(row, col);
                    amountOfBones--;
                } else {
                    throw new TooManyBonesException();
                }
            } else {
                throw new MouthEmptyException();
            }
        }
        territory.onTerritoryChange();
    }

    // getter and setter

    /**
     * @param row Row (must be >= 0)
     * @param col Column (must be >= 0)
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0
     */
    @Invisible
    public void setPosition(int row, int col) throws IllegalArgumentException{
        synchronized (territory) {
            setRow(row);
            setCol(col);
        }
        territory.onTerritoryChange();
    }

    /**
     * @param row Row (must be >= 0)
     * @throws IllegalArgumentException when {@code row} < 0
     */

    private synchronized void setRow(int row) throws IllegalArgumentException {
        if (row < 0) {
            throw new IllegalArgumentException("Row must be >= 0");
        }
        this.row = row;
    }

    /**
     * @param col Column (must be >= 0)
     * @throws IllegalArgumentException when {@code col} < 0
     */

    private synchronized void setCol(int col) throws IllegalArgumentException{
        if (col < 0) {
            throw new IllegalArgumentException("Column must be >= 0");
        }
        this.col = col;
    }
    @Invisible
    public synchronized void resetAmountOfBones(){
        amountOfBones = 0;
    }
    @Invisible
    public synchronized int getRow(){
        return row;
    }
    @Invisible
    public synchronized int getCol(){
        return col;
    }
    @Invisible
    public synchronized Orientation getOrientation(){
        return orientation;
    }
    @Invisible
    public synchronized void setAmountOfBones(int amountOfBones){
        this.amountOfBones = amountOfBones;
    }
    @Invisible
    public static int getMaxBones() {
        return MAX_BONES;
    }
    @Invisible
    public synchronized int getAmountOfBones(){
        return amountOfBones;
    }
    @Invisible
    public synchronized void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }
    @Invisible
    public synchronized void setTerritory(Territory territory){
        this.territory = territory;
    }
    @Invisible
    public void main() {}
}
