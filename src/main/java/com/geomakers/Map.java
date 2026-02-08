package com.geomakers;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.painter.CompoundPainter;

/**
 * The main purpose of this class is to set pins and operate the GUI of the guessing part of the game.
 * @author Avaneeth Kumar, Michael Panuganti, Tristan Lim
 * @version 2023-06-02
 */
public class Map {
    private JXMapViewer viewer;

    private CompoundPainter<JXMapViewer> painter;
    private WaypointPainter<Waypoint> bluePins;
    private WaypointPainter<Waypoint> redPins;
    private PathPainter paths;

    private WaypointListener wl;
    /**
     * Creates new screen for the guessing map and enables you to add pins using the mouse
     */
    public Map() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));
        viewer = new JXMapViewer();
        viewer.setTileFactory(tileFactory);

        bluePins = new WaypointPainter<Waypoint>();
        bluePins.setRenderer(new MyWaypointRenderer(false));
        redPins = new WaypointPainter<Waypoint>();
        redPins.setRenderer(new MyWaypointRenderer(true));
        painter = new CompoundPainter<JXMapViewer>();
        painter.addPainter(bluePins);
        painter.addPainter(redPins);
        viewer.setOverlayPainter(painter);

        MouseInputListener mil = new PanMouseInputListener(viewer);
        viewer.addMouseListener(mil);
        viewer.addMouseMotionListener(mil);
        viewer.addMouseListener(new CenterMapListener(viewer));
        viewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(viewer));
        wl = new WaypointListener(viewer, bluePins);
    }
    
    /**
     * Getter method for the viewer component of this map
     * @return the viewer component
     */
    public JXMapViewer getViewer() {
        return viewer;
    }
    /**
     * Allows user to place pins only on first try
     */
    public void enablePinPlacing() {
        viewer.addMouseListener(wl);
    }
    /**
     * Does not allow the user to change the location of the pin after the guess has been made
     */
    public void disablePinPlacing() {
        viewer.removeMouseListener(wl);
    }
    /**
     * Returns the location blue pin
     * @return the location of the pin.
     */
    public GeoPosition getBluePinPosition() {
        if (bluePins.getWaypoints().isEmpty())
            return null;

        return bluePins.getWaypoints().iterator().next().getPosition();
    }
    /**
     * Places a blue pin on the map
     * @param p the location where the user places the pin
     */
    public void placeBluePin(GeoPosition p) {
        bluePins.setWaypoints(new HashSet<>(Arrays.asList(new DefaultWaypoint(p))));
    }
    /**
     * Places a red pin on the map
     * @param p the location where the user places the pin
     */
    public void placeRedPin(GeoPosition p) {
        redPins.setWaypoints(new HashSet<>(Arrays.asList(new DefaultWaypoint(p))));
    }
    /**
     * Draws a line between the points of guessing so its easier for the user to see
     * @param p1 the guess
     * @param p2 the real location
     */
    public void drawPath(GeoPosition p1, GeoPosition p2) {
        if (paths != null)
            return;

        paths = new PathPainter(p1, p2);
        painter.removePainter(bluePins);
        painter.removePainter(redPins);
        painter.addPainter(paths);
        painter.addPainter(bluePins);
        painter.addPainter(redPins);
    }
    /**
     * Clears the pins
     */
    public void clearOverlay() {
        bluePins.setWaypoints(new HashSet<Waypoint>());
        redPins.setWaypoints(new HashSet<Waypoint>());
        if (paths != null) {
            painter.removePainter(paths);
            paths = null;
        }
    }
}
