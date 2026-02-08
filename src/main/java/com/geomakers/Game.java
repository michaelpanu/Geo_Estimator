/**
 * @author Michael Panuganti, Tristan Lim, Avaneeth Kumar
 * @version 2023-06-02
 */
package com.geomakers;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
/**
 * The main class which controls the game with
 * the GUI. 
 */
public class Game {
    static final GeoPosition BOTTOM_LEFT = new GeoPosition(Utilities.MIN_LAT, Utilities.MIN_LON);
    static final GeoPosition TOP_RIGHT = new GeoPosition(Utilities.MAX_LAT, Utilities.MAX_LON);
    static final Map map = new Map();
    static final Panorama pano = new Panorama();
    static final JFrame frame = new JFrame("GeoEstimator");
    static final JXMapViewer viewer = map.getViewer();

    static LinkedList<Round> rounds = new LinkedList<Round>();
    static ListIterator<Round> it;
    static boolean nextWasCalled = true;
    static int curr = 0;
    static GeoPosition p;

    public static void main(String[] args) throws IOException, URISyntaxException {
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        viewer.setBounds(0, 0, 800, 513);
        frame.add(viewer);

        final JLabel info = new JLabel("", null, SwingConstants.LEFT);
        info.setBounds(15, 513, 785, 60);
        info.setFont(new Font(null, 0, 30));
        frame.add(info);

        final JButton guessButton = new JButton("GUESS");
        guessButton.setBounds(190, 513, 550, 60);
        guessButton.setFont(new Font(null, 0, 20));
        frame.add(guessButton);

        final JButton relaunchButton = new JButton("⟳");
        relaunchButton.setBounds(740, 513, 60, 60);
        relaunchButton.setFont(new Font(null, 0, 30));
        frame.add(relaunchButton);

        final JButton newButton = new JButton("NEW ROUND");
        newButton.setBounds(190, 513, 305, 60);
        newButton.setFont(new Font(null, 0, 20));
        frame.add(newButton);

        final JButton finishButton = new JButton("FINISH GAME");
        finishButton.setBounds(495, 513, 305, 60);
        finishButton.setFont(new Font(null, 0, 20));
        frame.add(finishButton);

        final JButton prevButton = new JButton("PREVIOUS");
        prevButton.setBounds(430, 513, 185, 60);
        prevButton.setFont(new Font(null, 0, 20));
        prevButton.setVisible(false);
        frame.add(prevButton);

        final JButton nextButton = new JButton("NEXT");
        nextButton.setBounds(615, 513, 185, 60);
        nextButton.setFont(new Font(null, 0, 20));
        nextButton.setVisible(false);
        frame.add(nextButton);

        guessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GeoPosition guess = map.getBluePinPosition();
                if (guess == null)
                    return;

                double distance = Utilities.calculateDistance(guess, p);
                rounds.addLast(new Round(guess, p, distance));
                info.setText(String.format("%.1f KM", distance));
                guessButton.setVisible(false);
                relaunchButton.setVisible(false);
                newButton.setVisible(true);
                finishButton.setVisible(true);
                map.disablePinPlacing();
                displayOverlay(guess, p);
                pano.terminatePano();
            }
        });

        relaunchButton.addActionListener(new ActionListener() {
            /**
             * refreshes the panorama to the original location
             * @param e the event linked to the refresh button
             */
            public void actionPerformed(ActionEvent e) {
                pano.terminatePano();
                pano.launchPano(p);
            }
        });

        newButton.addActionListener(new ActionListener() {
            /**
             * Begins the next round by opening a new panorama
             * and clearing the pins on the map. 
             * @param e the action linked to clicking the NEXT button
             */
            public void actionPerformed(ActionEvent e) {
                info.setText("ROUND " + (rounds.size() + 1));
                newButton.setVisible(false);
                finishButton.setVisible(false);
                guessButton.setVisible(true);
                relaunchButton.setVisible(true);
                map.enablePinPlacing();
                map.clearOverlay();
                viewer.zoomToBestFit(new HashSet<GeoPosition>(Arrays.asList(TOP_RIGHT, BOTTOM_LEFT)), 1);
                p = Utilities.generatePosition();
                pano.launchPano(p);
            }
        });

        finishButton.addActionListener(new ActionListener() {
            /**
             * Brings a new GUI where players can view 
             * all past rounds
             * @param e The button press of the FINISH button
             */
            public void actionPerformed(ActionEvent e) {
                newButton.setVisible(false);
                finishButton.setVisible(false);
                prevButton.setVisible(true);
                nextButton.setVisible(true);
                it = rounds.listIterator();
                nextButton.getActionListeners()[0].actionPerformed(null);
            }
        });

        prevButton.addActionListener(new ActionListener() {
            /**
             * Brings the player to the previous round in the
             * finish screen.
             * @param e The button press of the previous button 
             * on the finish screen
             */
            public void actionPerformed(ActionEvent e) {
                if (curr == 1)
                    return;

                if (nextWasCalled) {
                    nextWasCalled = false;
                    it.previous();
                }
                Round r = it.previous();
                --curr;
                info.setText(String.format("ROUND %d / %d: %.1f KM",
                        curr, rounds.size(), r.getDistance()));
                displayOverlay(r.getGuess(), r.getLocation());
            }
        });

        nextButton.addActionListener(new ActionListener() {
            /**
             * Brings the player to the next round in the
             * finish screen.
             * @param e The button press of the next button 
             * on the finish screen
             */
            public void actionPerformed(ActionEvent e) {
                if (curr == rounds.size())
                    return;

                if (!nextWasCalled) {
                    nextWasCalled = true;
                    it.next();
                }
                Round r = it.next();
                ++curr;
                info.setText(String.format("ROUND %d / %d: %.1f KM",
                        curr, rounds.size(), r.getDistance()));
                displayOverlay(r.getGuess(), r.getLocation());
            }
        });

        newButton.getActionListeners()[0].actionPerformed(null);
        frame.setVisible(true);
    }
    /**
     * Draws a path between the player's guess and the actual
     * location
     * @param blue Player guess's pin
     * @param red Actual location's pin
     */
    static void displayOverlay(GeoPosition blue, GeoPosition red) {
        map.clearOverlay();
        map.placeBluePin(blue);
        map.placeRedPin(red);
        map.drawPath(blue, red);
        viewer.setZoom(0);
        viewer.zoomToBestFit(new HashSet<GeoPosition>(Arrays.asList(blue, red)), 0.7);
    }
}
