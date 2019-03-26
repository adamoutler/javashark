/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class ColoredXYCoords extends XYCoords {

    int color = 0;
    int tollerance;
    private static boolean debug;

    public ColoredXYCoords(int x, int y, int tollerance) {
        super(x, y);
    }

    public ColoredXYCoords(int x, int y, int color, int tollerance) {
        super(x, y);
        this.color = color;
        this.tollerance = tollerance;
    }

    public int getColor(BufferedImage bi) {
        return new PicColors(bi).getColor(this);
    }

    public boolean approximateColorMatch(BufferedImage bi) {
        PicColors pc = new PicColors(bi);
        if (debug) {
            System.out.println("" + x + "," + y + "+" + pc.getColor(x, y) + "," + "0");
        }
        return new PicColors(bi).colorMatchesAtXY(x, y, color, tollerance);

    }

    /**
     * @param aDebug the debug to set
     */
    public static void setDebug(boolean aDebug) {
        debug = aDebug;
    }
}
