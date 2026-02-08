package com.geomakers;

import java.io.IOException;
import java.util.Scanner;

import org.jxmapviewer.viewer.GeoPosition;

/**
 * Contains methods to launch and terminate a panorama window using Google
 * Chrome.
 * 
 * @author Avaneeth Kumar, Michael Panuganti, Tristan Lim
 * @version 2023-06-02
 */
public class Panorama
{
    private static final String PANO_PATH = "/resources/panorama.sh";

    private Integer             procID;

    /**
     * Launches a panorama window
     * 
     * @param p
     *            the coordinates to be shown in the panorama
     */
    public void launchPano(GeoPosition p)
    {
        if (procID != null)
            terminatePano();

        // String[] arguments = new String[] { "sh",
        // System.getProperty("user.dir") + PANO_PATH,
        // String.valueOf(p.getLatitude()), String.valueOf(p.getLongitude()) };
        String script = System.getProperty("user.dir") + PANO_PATH;
        String[] arguments = new String[] { "sh", script, Double.toString(p.getLatitude()),
            Double.toString(p.getLongitude()) };
        try
        {
            ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.redirectErrorStream(true); // <-- ADD THIS
            Process proc = pb.start();

            Scanner s = new Scanner(proc.getInputStream());
            Integer lastInt = null;

            while (s.hasNext())
            {
                if (s.hasNextInt())
                    lastInt = s.nextInt();
            }

            s.close();
            procID = lastInt;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Terminates an open panorama window
     */
    public void terminatePano()
    {
        try
        {
            new ProcessBuilder(new String[] { "kill", String.valueOf(procID) }).start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
