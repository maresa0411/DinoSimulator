package de.ossenbeck.dinosimulator.model;

public class EndOfTerritoryException extends DinoTerritoryException{
    public EndOfTerritoryException(){
        super("Ende des Territoriums erreicht!");
    }
}
