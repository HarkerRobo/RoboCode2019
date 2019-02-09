package frc.robot.util;

import frc.robot.commands.groups.SetScoringPosition.Location;

/**
 * Represents the action specific to manuvering between the given starting location and the given ending location
 */
public class Action {
    private Location startLocation;
    private Location endLocation;

    public Action (Location startLocation, Location endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Action) {
            Action a = (Action) o;
            return startLocation == a.startLocation && endLocation == a.endLocation;
        }
        return false;
    }
}