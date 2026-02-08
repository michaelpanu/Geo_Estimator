/**
 * @author Michael Panuganti, Avaneeth Kumar, Tristan Lim
 * @version 2023-06-02
 */
package com.geomakers;
import org.jxmapviewer.viewer.GeoPosition;
/**
 * Represents an individual game round.
 */
public class Round {
    private GeoPosition guess;
    private GeoPosition location;
    private double distance;
    /**
     * Creates a Round object with parameters for user-input 
     * guess, and the actual location + distance away.
     * @param guess User-inputted guess. Compared to the actual location to determine distance.
     * @param location Actual location in google maps. Generated before each round.
     * @param distance Distance between the guess and location.
     */
    public Round(GeoPosition guess, GeoPosition location, double distance) {
        this.guess = guess;
        this.location = location;
        this.distance = distance;
    }
    /**
     * Getter method for the user guess. 
     * @return Returns user's guess for the round.
     */
    public GeoPosition getGuess() {
        return guess;
    }
    /**
     * Getter method for the real location.
     * @return Returns the real location for the round. 
     */
    public GeoPosition getLocation() {
        return location;
    }
    /**
     * Getter method for the distance between guess and location.
     * @return Returns distance between the two pins. 
     */
    public double getDistance() {
        return distance;
    }
}
