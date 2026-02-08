package com.geomakers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Implements a painter that renders a dotted-line path between two positions on a JXMapViewer.
 * @author Avaneeth Kumar, Michael Panuganti, Tristan Lim
 * @version 2023-06-02
 */
public class PathPainter implements Painter<JXMapViewer> {
    private static final Color color = Color.DARK_GRAY;

    private GeoPosition p1;
    private GeoPosition p2;

    /**
     * Instantiates a path painter that contains a path between two positions
     * @param p1 the first position
     * @param p2 the second position
     */
    public PathPainter(GeoPosition p1, GeoPosition p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Implements the paint method for the Painter interface
     */
    public void paint(Graphics2D g, JXMapViewer viewer, int w, int h) {
        g = (Graphics2D) g.create();
        g.translate(-viewer.getViewportBounds().x, -viewer.getViewportBounds().y);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));

        Point2D pt1 = viewer.getTileFactory().geoToPixel(p1, viewer.getZoom());
        Point2D pt2 = viewer.getTileFactory().geoToPixel(p2, viewer.getZoom());
        g.drawLine((int) pt1.getX(), (int) pt1.getY(), (int) pt2.getX(), (int) pt2.getY());
        g.dispose();
    }
}
