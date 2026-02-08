/**
 * @author Michael Panuganti, Avaneeth Kumar, Tristan Lim
 * @version 2023-06-02
 */
package com.geomakers;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
/**
 * This class uses mouse input to place down pins on our map. 
 */
public class WaypointListener extends MouseAdapter {
    private JXMapViewer viewer;
    private WaypointPainter<Waypoint> painter;
    /**
     * Creates a WaypointListener which uses mouse input to place a pin.
     * @param viewer The GUI for the map.
     * @param painter The waypoint creator for the pins.
     */
    public WaypointListener(JXMapViewer viewer, WaypointPainter<Waypoint> painter) {
        this.viewer = viewer;
        this.painter = painter;
    }
    /**
     * Checks to see if the MouseEvent was a right click, 
     * if it was it places a pin down and replaces the old one
     * if applicable. 
     * @param e Any MouseEvent or click on the map. 
     */
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3)
            return;

        viewer.getMousePosition();
        painter.setWaypoints(new HashSet<Waypoint>(
                Arrays.asList(new DefaultWaypoint(viewer.convertPointToGeoPosition(e.getPoint())))));
        viewer.repaint();
    }
}
