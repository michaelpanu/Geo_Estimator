/**
 * @author Tristan Lim, Michael Panuganti, Avaneeth Kumar
 * @version 2023-06-02
 */
package com.geomakers;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.jxmapviewer.viewer.GeoPosition;
/**
 * A class which holds many methods needed for each round.
 * This class includes methods for calculating distance, generating a location, 
 * and verifying whether the location is valid on google maps StreetView. 
 */
public class Utilities {
    /**
     * Key for accessing Google Street View API
     */
    public static final String API_KEY = "YOUR_API_KEY";
    /**
     * Least possible latitude for a coordinate that can be generated
     */
    // 24.396308
    public static final double MIN_LAT = 37.216458;


    /**
     * Greatest possible latitude for a coordinate that can be generated
     */
    // 49.384358
    public static final double MAX_LAT = 37.403155;
    /**
     * Least possible longitude for a coordinate that can be generated
     */
    //-124.848974
    public static final double MIN_LON = -122.046610;
    /**
     * Greatest possible longitude for a coordinate that can be generated
     */
    //-66.885444;
    public static final double MAX_LON = -121.803114;
    /**
     * Calculates the distance between two GeoPositions, which
     * are intended to be the user's guess and actual locations.
     * The calculation uses the Haversine formula to calculate the 
     * distance between two points on a sphere. However, because the earth
     * isn't perfectly spherical, the error bound is approximately 10 km.
     * @param p1 First GeoPosition
     * @param p2 Second GeoPosition
     * @return Returns a double in kilometers of the distance between two points.
     */
    public static double calculateDistance(GeoPosition p1, GeoPosition p2) {
        final double RADIUS = 6368.0;
        double x1 = Math.toRadians(p1.getLatitude());
        double y1 = Math.toRadians(p1.getLongitude());
        double x2 = Math.toRadians(p2.getLatitude());
        double y2 = Math.toRadians(p2.getLongitude());
        return 2 * RADIUS * Math.asin(Math.sqrt(Math.pow(Math.sin((x2 - x1) / 2), 2)
                + Math.cos(x2) * Math.cos(x1) * Math.pow(Math.sin((y2 - y1) / 2), 2)));
    }
    /**
     * Generates a GeoPosition in the U.S. which is checked to see 
     * whether or not it is valid in google maps. If not, it generates again. 
     * @return Returns the first valid GeoPosition.
     */
    public static GeoPosition generatePosition() {
        double lat, lon;
        do {
            lat = myRandom(MIN_LAT, MAX_LAT);
            lon = myRandom(MIN_LON, MAX_LON);
        } while (!validateCoordinates(lat, lon));
        return new GeoPosition(lat, lon);
    }
    /**
     * Uses the values in low and high to generate a random number 
     * between the two.
     * @param low Lower bound of the generation.
     * @param high Higher bound of the generation.
     * @return Returns a double between high and low. 
     */
    private static double myRandom(double low, double high) {
        return Math.random() * (high - low) + low;
    }
    /**
     * Checks to see whether or not the coordinates of the GeoPosition
     * generated in generatePosition() is valid or not. Uses google maps 
     * streetview api to see if the streetview of the coordinate exists. 
     * @param lat Latitude of the GeoPosition.
     * @param lon Longitude of the GeoPosition. 
     * @return True if it is valid, False if not.
     */
    private static boolean validateCoordinates(double lat, double lon) {
        try {
            URL url = new URI(String.format(
                    "https://maps.googleapis.com/maps/api/streetview/metadata?key=%s&location=%.6f,%.6f",
                    API_KEY, lat, lon)).toURL();
            Scanner s = new Scanner(url.openStream());
            while (s.hasNext()) {
                if (s.next().contains("OK")) {
                    s.close();
                    return true;
                }
            }
            s.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
