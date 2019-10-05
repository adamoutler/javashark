/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building;

import com.adamoutler.imagerecognition.ColoredXYCoords;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public enum Locations {

    FRIENDLYCAR1(new XYCoords(820, 350), new ColoredXYCoords(910, 280, 0xa9cafa, 10)),
    FRIENDLYCAR2(new XYCoords(820, 500), new ColoredXYCoords(910, 440, 0xa9cafa, 10)),
    FRIENDLYCAR3(new XYCoords(820, 650), new ColoredXYCoords(910, 600, 0xa9cafa, 10)),
    FRIENDLYCAR4(new XYCoords(630, 350), new ColoredXYCoords(710, 280, 0xa9cafa, 10)),
    FRIENDLYCAR5(new XYCoords(630, 500), new ColoredXYCoords(710, 440, 0xa9cafa, 10)),
    FRIENDLYCAR6(new XYCoords(630, 650), new ColoredXYCoords(710, 600, 0xa9cafa, 10)),
    FRIENDLYCAR7(new XYCoords(430, 350), new ColoredXYCoords(510, 280, 0xa9cafa, 10)),
    FRIENDLYCAR8(new XYCoords(430, 500), new ColoredXYCoords(510, 440, 0xa9cafa, 10)),
    FRIENDLYCAR9(new XYCoords(430, 650), new ColoredXYCoords(510, 600, 0xa9cafa, 10)),
    FOECAR1(new XYCoords(575, 350), new ColoredXYCoords(484, 282, 0xff615b, 10)),
    FOECAR2(new XYCoords(575, 500), new ColoredXYCoords(484, 442, 0xff615b, 10)),
    FOECAR3(new XYCoords(575, 650), new ColoredXYCoords(484, 600, 0xff615b, 10)),
    FOECAR4(new XYCoords(775, 350), new ColoredXYCoords(688, 282, 0xff615b, 10)),
    FOECAR5(new XYCoords(775, 500), new ColoredXYCoords(688, 442, 0xff615b, 10)),
    FOECAR6(new XYCoords(775, 650), new ColoredXYCoords(668, 600, 0xff615b, 10)),
    FOECAR7(new XYCoords(975, 350), new ColoredXYCoords(889, 282, 0xff615b, 10)),
    FOECAR8(new XYCoords(975, 500), new ColoredXYCoords(889, 442, 0xff615b, 10)),
    FOECAR9(new XYCoords(975, 650), new ColoredXYCoords(899, 600, 0xff615b, 10));
    private XYCoords tapLocation;
    private ColoredXYCoords verifyLocation;

    Locations(XYCoords tapLocation, ColoredXYCoords colorLocation) {
        this.tapLocation = tapLocation;
        this.verifyLocation = colorLocation;
    }
    final public static int BLUE = 0xa9cafa;

    /**
     * @return the tapLocation
     */
    public XYCoords getTapLocation() {
        return tapLocation;
    }

    /**
     * @return the colorLocation
     */
    public ColoredXYCoords getVerifyLocation() {
        return verifyLocation;
    }

    public static String[] getBackSwipe(int duration) {
        return new String[]{"940", "443", "375", "443", Integer.toString(duration)};
    }

    public static void doBackSwipe(VirtualBoxMachine vm) {
        String[] bs = getBackSwipe(600);
        vm.adbShell("input", "swipe", bs[0], bs[1], bs[2], bs[3], bs[4]);

    }

    public static Locations getLocation(boolean friendly, int carPosition) {
        if (carPosition < 1 || carPosition > 9) {
            return null;
        }
        return Locations.valueOf((friendly ? "FRIENDLY" : "FOE") + "CAR" + carPosition);
    }

    public static boolean cursorInPosition(boolean friendly, int carPosition, BufferedImage screenshot) throws CarLocationOffScreenException {
        Locations loc = getLocation(friendly, carPosition);
        throwExceptionIfLocationIsNull(loc);
        return loc.getVerifyLocation().approximateColorMatch(screenshot);
    }

    private static void throwExceptionIfLocationIsNull(Locations loc) throws CarLocationOffScreenException {
        if (loc == null) {
            throw new CarLocationOffScreenException("Cannot check this car, it is off-screen");
        }
    }

    public static boolean cursorInPosition(Locations location, BufferedImage screenshot) throws CarLocationOffScreenException {
        throwExceptionIfLocationIsNull(location);
        return location.getVerifyLocation().approximateColorMatch(screenshot);
    }

    public static boolean setCursorPosition(boolean friendly, int carPosition, BufferedImage screenshot, VirtualBoxMachine vm) throws CarLocationOffScreenException {
        Locations expectedLocation = getLocation(friendly, carPosition);
        if (!cursorInPosition(expectedLocation, screenshot)) {
            tapLocation(vm, expectedLocation);
            return cursorInPosition(expectedLocation, vm.screenshot());
        } else {
            return true;
        }

    }

    private static void tapLocation(VirtualBoxMachine vm, Locations expectedLocation) {
        vm.longTap(expectedLocation.getTapLocation());
        sleep(1000);
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
