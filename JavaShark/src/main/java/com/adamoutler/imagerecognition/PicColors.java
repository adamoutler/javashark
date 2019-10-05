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
public class PicColors {

    private final BufferedImage image;

    public PicColors(BufferedImage image) {
        this.image = image;
    }

    public boolean areRGBValuesHigher(XYCoords xy, int i) {
        return (getRed(xy) >= getRed(i) && getBlue(xy) >= getBlue(i) && getGreen(xy) >= getGreen(i));
    }

    public XYCoords scanUpRightForLackOfColor(int[] colors, int startingAtX, int startingAtY, int maximumDifference) {
        XYCoords xy = new XYCoords(startingAtX, startingAtY);

        boolean colorFoundMatching = false;
        while (!colorFoundMatching && (xy.x < image.getWidth() - 2 && xy.y < image.getHeight() - 2)) {
            for (int color : colors) {
                if (!colorMatchesAtXY(xy.x, xy.y, color, maximumDifference)) {
                    colorFoundMatching = true;
                }
            }
            xy.x++;
            xy.y++;

        }
        return xy;

    }

    public int scanDownYCoordinateForColorAtXCoordinate(int color, int x, int startingAtY, int endingAtY) {
        for (int y = startingAtY; y <= endingAtY; y++) {
            if (colorMatchesAtXY(x, y, color)) {
                return y;
            }
        }
        return -1;
    }

    public int scanUpYCoordinateForColorAtXCoordinate(int color, int x, int startingAtY, int endingAtY) {
        for (int y = startingAtY; y <= endingAtY; y--) {
            if (colorMatchesAtXY(x, y, color)) {
                return y;
            }
        }
        return -1;
    }

    public int scanUpYCoordinateForLackOfColorAtXCoordinate(int[] colors, int x, int startingAtY, int endingAtY) {
        return scanUpYCoordinateForLackOfColorAtXCoordinate(colors, x, startingAtY, endingAtY, 0);
    }

    public int scanUpYCoordinateForLackOfColorAtXCoordinate(int[] colors, int x, int startingAtY, int endingAtY, int maximumDifference) {
        for (int y = startingAtY; y >= endingAtY; y--) {
            boolean colorFoundMatching = false;
            for (int color : colors) {
                if (colorMatchesAtXY(x, y, color, maximumDifference)) {
                    colorFoundMatching = true;
                }
            }
            if (!colorFoundMatching) {
                return y;
            }
        }
        return -1;
    }

    public int scanRightXCoordinateForColorAtYCoordinate(int color, int startingAtX, int y, int endingAtX, int maximumDifference) {
        for (int x = startingAtX; x <= endingAtX; x++) {
            if (colorMatchesAtXY(x, y, color, maximumDifference)) {
                return x;
            }
        }
        return -1;
    }

    public int scanRightXCoordinateForColorAtYCoordinate(int color, int startingAtX, int y, int endingAtX) {
        for (int x = startingAtX; x < endingAtX; x++) {
            if (colorMatchesAtXY(x, y, color)) {
                return x;
            }
        }
        return -1;
    }

    public int scanLeftXCoordinateForColorAtYCoordinate(int color, int startingAtX, int y, int endingAtX, int maximumDifference) {
        for (int x = startingAtX; x <= endingAtX; x--) {
            if (colorMatchesAtXY(x, y, color, maximumDifference)) {
                return x;
            }
        }
        return -1;
    }

    public int scanLeftXCoordinateForColorAtYCoordinate(int color, int startingAtX, int y, int endingAtX) {
        for (int x = startingAtX; x <= endingAtX; x--) {
            if (colorMatchesAtXY(x, y, color)) {
                return x;
            }
        }
        return -1;
    }

    public int scanRightXCoordinateForColorAtYCoordinate(int[] colors, int startingAtX, int y, int endingAtX) {
        return scanRightXCoordinateForColorAtYCoordinate(colors, startingAtX, y, endingAtX, 0);
    }

    public int scanRightXCoordinateForColorAtYCoordinate(int[] colors, int startingAtX, int y, int endingAtX, int maximumDifference) {
        for (int x = startingAtX; x <= endingAtX; x++) {
            for (int color : colors) {
                if (colorMatchesAtXY(x, y, color, maximumDifference)) {
                    return x;
                }
            }
        }
        return -1;
    }

    public int scanRightXCoordinateForLackOfColorAtYCoordinate(int[] colors, int startingAtX, int y, int endingAtX) {
        return scanRightXCoordinateForLackOfColorAtYCoordinate(colors, startingAtX, y, endingAtX, 0);

    }

    public int scanRightXCoordinateForLackOfColorAtYCoordinate(int[] colors, int startingAtX, int y, int endingAtX, int maximumDifference) {
        for (int x = startingAtX; x <= endingAtX; x++) {
            boolean matching = false;
            for (int color : colors) {
                if (colorMatchesAtXY(x, y, color, maximumDifference)) {
                    matching = true;
                }
            }
            if (!matching) {
                return x;
            }
        }
        return -1;
    }

    public boolean colorMatchesAtXY(int x, int y, int referenceColor, int maximumDifference) {
        return this.checkColorPlusOrMinus(x, y, referenceColor, maximumDifference);
    }

    public boolean colorMatchesAtXY(int x, int y, int referenceColor) {
        try {
            return (getColor(new XYCoords(x, y)) == referenceColor);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public static byte getRed(int i) {
        return (byte) (i & 0xff);
    }

    public static byte getBlue(int i) {
        return (byte) ((i & 0xff0000) >> 16);
    }

    public static byte getGreen(int i) {
        return (byte) (byte) ((i & 0xff00) >> 8);
    }

    public int getColor(XYCoords xy) {
        if (xy.getX() < 0 || xy.getY() < 0 || xy.getX() > this.image.getWidth() || xy.getY() > this.image.getHeight()) {
            return -1;
        }
        return (getColor(xy.getX(), xy.getY()));
    }

    public int getColor(int x, int y) {
        if (x < 0 || y < 0 || x > this.image.getWidth() || y > this.image.getHeight()) {
            return -1;
        }
        return (image.getRGB(x, y) & 0x00FFFFFF);
    }

    public boolean checkColorPlusOrMinus(XYCoords c, int referenceColor, int maxDifference) {
        return checkColorPlusOrMinus(c.getX(), c.getY(), referenceColor, maxDifference);
    }

    public boolean checkColorPlusOrMinus(int x, int y, int referenceColor, int maxDifference) {
        XYCoords xy = new XYCoords(x, y);
        return (redWithinRange(xy, referenceColor, maxDifference)
                && blueWithinRange(xy, referenceColor, maxDifference)
                && greenWithinRange(xy, referenceColor, maxDifference));
    }

    public boolean redWithinRange(XYCoords xy, int compareToColor, int maxDifference) {
        byte referenceRed = getRed(compareToColor);
        byte red = getRed(xy);
        return Math.abs(referenceRed - red) <= maxDifference;
    }

    public boolean blueWithinRange(XYCoords xy, int compareToColor, int maxDifference) {
        byte referenceRed = getBlue(compareToColor);
        byte blue = getBlue(xy);
        return Math.abs(referenceRed - blue) <= maxDifference;
    }

    public boolean greenWithinRange(XYCoords xy, int compareToColor, int maxDifference) {
        byte referenceGreen = getBlue(compareToColor);
        byte green = getBlue(xy);
        return Math.abs(referenceGreen - green) <= maxDifference;
    }

    public byte getRed(XYCoords xy) {
        if (xy.getX() < 0 || xy.getY() < 0 || xy.getX() > this.image.getWidth() || xy.getY() > this.image.getHeight()) {
            return -1;
        }
        byte blue = (byte) (getColor(xy) & 0xff);
        return blue;
    }

    public byte getBlue(XYCoords xy) {
        if (xy.getX() < 0 || xy.getY() < 0 || xy.getX() > this.image.getWidth() || xy.getY() > this.image.getHeight()) {
            return -1;
        }
        byte red = (byte) ((getColor(xy) & 0xff0000) >> 16);
        return red;
    }

    public byte getGreen(XYCoords xy) {
        if (xy.getX() < 0 || xy.getY() < 0 || xy.getX() > this.image.getWidth() || xy.getY() > this.image.getHeight()) {
            return -1;
        }
        byte green = (byte) ((getColor(xy) & 0xff00) >> 8);
        return green;
    }

}
