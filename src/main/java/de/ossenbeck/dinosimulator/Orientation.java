package de.ossenbeck.dinosimulator;

public enum Orientation {
    NORTH, WEST, SOUTH, EAST;

    public Orientation turnLeft(){
        return switch (this){
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }
}
