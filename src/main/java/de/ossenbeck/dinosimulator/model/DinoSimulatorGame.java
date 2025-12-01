package de.ossenbeck.dinosimulator.model;

public class DinoSimulatorGame {
    private Territory territory;

    public DinoSimulatorGame(){
        territory = new Territory();
    }

    public void reset(){
        territory.reset();
    }

    public Territory getTerritory(){
        return territory;
    }

    public void changeTerritory(final Territory newTerritory){
        final Territory oldTerritory = territory;
        territory = newTerritory;
        oldTerritory.transferTerritoryChangeListener(newTerritory);
    }
}
