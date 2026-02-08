package com.geomakers;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

/**
 * Implements a painter that can render red or blue waypoints on a JXMapViewer.
 * @author Avaneeth Kumar, Michael Panuganti, Tristan Lim
 * @version 2023-06-02
 */
public class MyWaypointRenderer implements WaypointRenderer<Waypoint> {
    private static final String WAYPOINT_RED_PATH = "/resources/waypoint_red.png";
    private static final String WAYPOINT_BLUE_PATH = "/resources/waypoint_blue.png";

    private BufferedImage img;

    /**
     * Instantiates a new custom waypoint renderer
     * @param isRed true - red waypoints; false - blue waypoints
     */
    public MyWaypointRenderer(boolean isRed) {
        try {
            img = ImageIO
                    .read(new File(System.getProperty("user.dir") + (isRed ? WAYPOINT_RED_PATH : WAYPOINT_BLUE_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implements the paintWaypoint method for the WaypointRenderer interface
     */
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint w) {
        Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
        int x = (int) point.getX() - img.getWidth() / 2;
        int y = (int) point.getY() - img.getHeight();
        g.drawImage(img, x, y, null);
    }
}
