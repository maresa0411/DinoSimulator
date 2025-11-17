package de.ossenbeck.dinosimulator.model;

public class MouthEmptyException extends DinoTerritoryException{
    public MouthEmptyException(){
        super("Maul leer!");
    }
}
