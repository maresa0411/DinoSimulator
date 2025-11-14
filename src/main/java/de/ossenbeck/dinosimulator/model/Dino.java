package de.ossenbeck.dinosimulator.model;

import de.ossenbeck.dinosimulator.util.Notifier;

public class Dino {
    private int row;
    private int col;
    private int amountOfBones;
    private Orientation orientation;
    private Territory territory;
    private static final int MAX_BONES = 100;
    private Notifier notifier;

    /**
     * @param territory Territory
     * @param row Row (must be >= 0 and < {@code territoryRows})
     * @param col Column (must be >= 0) < {@code territoryCols})
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0
     */

    public Dino(Territory territory, int row, int col, Notifier notifier){
        this.territory = territory;
        this.notifier = notifier;
        setPosition(row, col);
        resetAmountOfBones();
        orientation = Orientation.EAST;
    }

    /**
     * Moves the dino one tile forward, based on the current orientation.
     * @throws RockInTheWayException when there is a rock on the following tile.
     * @throws EndOfTerritoryException when the end of the territory is reached.
     */
    public void moveForward(){
        // east
        if(orientation == Orientation.EAST){
            moveForwardHelp(row, col+1);
        }
        // north
        else if(orientation == Orientation.NORTH){
            moveForwardHelp(row-1, col);
        }
        // west
        else if(orientation == Orientation.WEST){
            moveForwardHelp(row, col-1);
        }
        // south
        else if(orientation == Orientation.SOUTH){
            moveForwardHelp(row+1, col);

        }
        territory.onTerritoryChange();
        notifier.post("Dino vorwärts bewegt.");
    }

    private void moveForwardHelp(int nextRow, int nextCol){
        if(nextRow < territory.getNumberOfRows() && nextCol < territory.getNumberOfCols()){
            if(!territory.isRock(row+1, col)) {
                setPosition(nextRow, nextCol);
            }else{
                notifier.post("Felsen im Weg!");
                throw new RockInTheWayException();
            }
        }else{
            notifier.post("Ende des Territoriums erreicht!");
            throw new EndOfTerritoryException();
        }
    }

    public void turnLeft(){
        orientation = orientation.turnLeft();
        territory.onTerritoryChange();
        notifier.post("Dino nach links gedreht.");
    }

    /**
     * Makes the dino pick up a bone, if possible.
     * @throws MouthFullException when the maximum amount of bones {@code MAX_BONES} the dino can carry is reached.
     * @throws NoBonesThereException when the selected tile does not contain any bones.
     */
    public void pickUpBone(){
        if(territory.getBones(row, col) > 0){
            if(amountOfBones+1 < MAX_BONES){
                amountOfBones++;
                territory.removeBone(row, col);
                notifier.post("Der Dino hat einen Knochen aufgesammelt.");
                territory.onTerritoryChange();
            }else{
                notifier.post("Der Dino kann keine weitere Knochen mehr aufsammeln!");
                throw new MouthFullException();
            }
        }else{
            notifier.post("Keine Knochen zum Aufheben verfügbar!");
            throw new NoBonesThereException();
        }
    }

    /**
     * Maked the dino put down a bone.
     * @throws MouthEmptyException when the dino does not have any bones in its mouth.
     */
    public void putDownBone(){
        if (amountOfBones > 0) {
            territory.placeBone(row, col);
            amountOfBones--;
            notifier.post("Der Dino hat einen Knochen abgelegt!");
        }else{
            notifier.post("Der Dino hat keine Knochen im Maul, die er ablegen kann!");
            throw new MouthEmptyException();
        }
        territory.onTerritoryChange();
    }

    // getter and setter

    /**
     * @param row Row (must be >= 0)
     * @param col Column (must be >= 0)
     * @throws IllegalArgumentException when {@code row} or {@code col} < 0
     */

    public void setPosition(int row, int col){
        setRow(row);
        setCol(col);
        territory.onTerritoryChange();
    }

    /**
     * @param row Row (must be >= 0)
     * @throws IllegalArgumentException when {@code row} < 0
     */

    private void setRow(int row){
        if(row < 0){
            throw new IllegalArgumentException("Row must be >= 0");
        }
        this.row = row;
    }

    /**
     * @param col Column (must be >= 0)
     * @throws IllegalArgumentException when {@code col} < 0
     */

    private void setCol(int col){
        if(col < 0){
            throw new IllegalArgumentException("Column must be >= 0");
        }
        this.col = col;
    }

    public void resetAmountOfBones(){
        amountOfBones = 0;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Orientation getOrientation(){
        return orientation;
    }

    public void setAmountOfBones(int amountOfBones){
        this.amountOfBones = amountOfBones;
    }

    public static int getMaxBones() {
        return MAX_BONES;
    }

    public int getAmountOfBones(){
        return amountOfBones;
    }

    public void setNotifier(Notifier notifier){this.notifier = notifier;}
}
