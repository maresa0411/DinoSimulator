package de.ossenbeck.dinosimulator.util;

import de.ossenbeck.dinosimulator.model.Orientation;

public interface ChangeListener {
    public void onTerritoryChanged();

    public void onActorChanged(double x, double y, Orientation orientation);
}
