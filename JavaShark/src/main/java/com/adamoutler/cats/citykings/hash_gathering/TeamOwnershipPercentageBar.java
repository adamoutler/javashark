/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author adamo
 */
public class TeamOwnershipPercentageBar {

    final public static XYCoords STARTSCAN = new XYCoords(340, 140);
    final public static PicCoords BARLOCATION = new PicCoords(340, 120, 335, 35);
    final public static int BLUE = 0x90B3EB;
    final public static int RED = 0xFF6564;
    final public static int GREY = 0x5E6A83;
    private transient BufferedImage bi;
    private int numberWeOwn = 0;
    private int numberTheyOwn = 0;
    private int numberNobodyOwn = 0;
    private int totalSlots = 0;
    ArrayList<OwnedBy> owned = new ArrayList<>();

    public TeamOwnershipPercentageBar(byte[] screenshot) {
        bi = ByteImage.getImageFromBytes(screenshot);

        calculateNumberOfOurSlots();
    }

    public TeamOwnershipPercentageBar(BufferedImage screenshot) {
        bi = screenshot;
        calculateNumberOfOurSlots();
    }

    /**
     * The process starts by scanning for blue, red, or grey at the center
     * location of the bar. When the first location is found, the Y location is
     * scanned up, until the top of the bar is reached. From there, each time
     * the color does not match, we will add a position in the building.
     *
     * @param bi
     * @return
     */
    private int calculateNumberOfOurSlots() {

        owned = new ArrayList<>();
        Screenshottable.writePic(getBi().getSubimage(BARLOCATION.getX(), BARLOCATION.getY(), BARLOCATION.getW(), BARLOCATION.getH()), "PicUsedForTOSB");
        PicColors pc = new PicColors(getBi());
        int[] validColors = new int[]{RED, BLUE, GREY};
        int maxX = BARLOCATION.getW() + BARLOCATION.getX();

        int currentX = pc.scanRightXCoordinateForColorAtYCoordinate(validColors, STARTSCAN.getX(), STARTSCAN.getY(), maxX, 10);
        int topY = pc.scanUpYCoordinateForLackOfColorAtXCoordinate(validColors, currentX, STARTSCAN.getY(), BARLOCATION.getY(), 10) + 2;
        while (currentX < maxX && currentX != -1) {

            currentX = pc.scanRightXCoordinateForLackOfColorAtYCoordinate(validColors, currentX, topY, maxX, 10);
            owned.add(getOwnedBy(pc, currentX - 1, topY));
            currentX = pc.scanRightXCoordinateForColorAtYCoordinate(validColors, currentX, topY, maxX, 10);
        }
        setTotals(owned);
        return owned.size();
    }

    public OwnedBy buildingOwnedBy() {
        int halfTotal = Math.round(((float) owned.size()) / 2);
        int us = numberOwned(OwnedBy.US);
        int them = numberOwned(OwnedBy.THEM);

        if (us >= halfTotal) {
            return OwnedBy.US;
        } else if (them >= halfTotal) {
            return OwnedBy.THEM;
        } else {
            return OwnedBy.NOBODY;
        }

    }

    public int numberTheyOwn() {
        return numberOwned(OwnedBy.THEM);
    }

    public int numberWeOwn() {
        return numberOwned(OwnedBy.US);
    }

    public int numberUnowned() {
        return numberOwned(OwnedBy.NOBODY);
    }

    private int numberOwned(OwnedBy target) {
        int count = 0;
        for (OwnedBy owner : this.owned) {
            if (owner == target) {
                count++;
            }
        }
        return count;
    }

    private OwnedBy getOwnedBy(PicColors pc, int x, int y) {
        int color = pc.getColor(x, y);
        switch (color) {
            case RED:
                return OwnedBy.THEM;

            case BLUE:
                return OwnedBy.US;
        }
        return OwnedBy.NOBODY;
    }

    private void setTotals(ArrayList<OwnedBy> slotColor) {
        for (OwnedBy color : slotColor) {
            totalSlots++;
            switch (color) {
                case US:
                    numberWeOwn++;
                    break;
                case THEM:
                    numberTheyOwn++;
                    break;
                case NOBODY:
                    numberNobodyOwn++;
                    break;

            }
        }
    }

    /**
     * @return the numberWeOwn
     */
    public int getNumberWeOwn() {
        return numberWeOwn;
    }

    /**
     * @return the numberTheyOwn
     */
    public int getNumberTheyOwn() {
        return numberTheyOwn;
    }

    /**
     * @return the numberNobodyOwn
     */
    public int getNumberNobodyOwn() {
        return numberNobodyOwn;
    }

    /**
     * @return the numberTotal
     */
    public int getTotalSlots() {
        return totalSlots;
    }

    /**
     * @return the bi
     */
    public BufferedImage getBi() {
        return bi;
    }

    /**
     * @param bi the bi to set
     */
    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

}
