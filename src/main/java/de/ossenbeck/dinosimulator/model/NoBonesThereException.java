package de.ossenbeck.dinosimulator.model;

public class NoBonesThereException extends DinoTerritoryException{
    public NoBonesThereException(){
        super("Keine Knochen da!");
    }
}
