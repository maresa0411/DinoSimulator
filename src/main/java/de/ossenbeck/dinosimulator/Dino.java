package de.ossenbeck.dinosimulator;

public class Dino {
    private int row;
    private int col;
    private int amountOfBones;
    private Orientation orientation;
    private Territory territory;
    private final static int MAX_BONES = 100;

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
    public void moveForward(){
        int rows = territory.getNumberOfRows();
        int cols = territory.getNumberOfCols();

        // east
        if(orientation == Orientation.EAST){
            if(col+1 < cols){
                if(!territory.isRock(row, col+1)) {
                    setCol(col + 1);
                }else{
                    throw new RockInTheWayException();
                }
            }else{
                throw new EndOfTerritoryException();
            }
        }
        // north
        else if(orientation == Orientation.NORTH){
            if(row-1 >= 0 ){
                if(!territory.isRock(row-1, col)) {
                    setRow(row - 1);
                }else{
                    throw new RockInTheWayException();
                }
            }else{
                throw new EndOfTerritoryException();
            }
        }
        // west
        else if(orientation == Orientation.WEST){
            if(col-1 >= 0){
                if(!territory.isRock(row, col-1)) {
                    setCol(col - 1);
                }else{
                    throw new RockInTheWayException();
                }
            }else{
                throw new EndOfTerritoryException();
            }
        }
        // south
        else if(orientation == Orientation.SOUTH){
            if(row+1 < rows){
                if(!territory.isRock(row+1, col)) {
                    setRow(row + 1);
                }else{
                    throw new RockInTheWayException();
                }
            }else{
                throw new EndOfTerritoryException();
            }
        }
    }

    public void turnLeft(){
        orientation = orientation.turnLeft();
    }

    /**
     * Makes the dino pick up a bone, if possible.
     * @throws MouthFullException when the maximum amount of bones {@code MAX_BONES} the dino can carry is reached.
     * @throws NoBonesThereException when the selected tile does not contain any bones.
     */
    public void pickUpBone(){
        if(territory.getBones(row, col) > 0){
            if(amountOfBones+1 > MAX_BONES){
                amountOfBones++;
                territory.removeBone(row, col);
            }else{
                throw new MouthFullException();
            }
        }else{
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
        }else{
            throw new MouthEmptyException();
        }
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
    }

    /**
     * @param row Row (must be >= 0)
     * @throws IllegalArgumentException when {@code row} < 0
     */

    public void setRow(int row){
        if(row < 0){
            throw new IllegalArgumentException("Row must be >= 0");
        }
        this.row = row;
    }

    /**
     * @param col Column (must be >= 0)
     * @throws IllegalArgumentException when {@code col} < 0
     */

    public void setCol(int col){
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
}
