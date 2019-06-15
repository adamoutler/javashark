/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building.car;

import com.adamoutler.cats.battlefield.building.Building;
import com.adamoutler.cats.battlefield.building.CarLocationOffScreenException;
import com.adamoutler.cats.battlefield.building.Locations;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CarFactory {

    transient public final static XYCoords NEXTBUTTON = new XYCoords(302, 576);
    transient public final static XYCoords PREVIOUSBUTTON = new XYCoords(132, 576);
    transient public final static PicCoords NAMECOORDS = new PicCoords(6, 6, 300, 38);
    transient public final static PicCoords UNIQUECOORDS = new PicCoords(6, 230, 300, 24);
    transient final private static int SWORDCOLOR = 0x48a1b8;
    transient private final static PicCoords SMALL = new PicCoords(0, 0, 100, 300);
    transient final static Font FONT = new Font("Arial", Font.PLAIN, 35);
    final transient private VirtualBoxMachine vm;
    final transient private Building building;

    public CarFactory(VirtualBoxMachine vm, Building building) {
        this.vm = vm;
        this.building = building;
    }

    public boolean scan() {
        int offset = 0;
        for (Car c : building.getCars()) {
            if(vm.isKillThread())break;
            BufferedImage screenshot = vm.screenshot();
            screenshot = correctCursorLocationIfRequired(c, offset, screenshot);
            c.setCarScreenshot(screenshot);
            selectNextCar();

        }

        checkFinalCarNavigation();

        return !building.isSuspectProblem();
    }

    private void checkFinalCarNavigation() {

        sleep(3000);
        BufferedImage screenshot = vm.screenshot();
        Car currentCar = new Car().setCarScreenshot(screenshot);
        if (building.getCars().isEmpty()) return;
        Car lastCar = building.getCars().get(building.getCars().size() - 1);
        if (currentCar.equals(lastCar)) { //missed last tap
            vm.longTap(NEXTBUTTON);
            sleep(4000);
            screenshot = vm.screenshot();
        }
        if (building.isFriendlyScan()) {
            try {
                if (!Locations.cursorInPosition(Locations.FRIENDLYCAR1, screenshot)) {
                    building.setSuspsectProblemReason("cursor was not in start position after scan");
                }
            } catch (CarLocationOffScreenException ex) {
                building.setSuspsectProblemReason("CarLocationOffScreenException while checking final position");
            }
        }
    }

    private void selectNextCar() {
        vm.tap(NEXTBUTTON);
        sleep(1500);
    }

    private BufferedImage correctCursorLocationIfRequired(Car c, int offset, BufferedImage screenshot) {
        if (c.getLocation() + offset < 10) {
            screenshot = doCursorBasedCorrection(c, offset, screenshot);
        } else {
            screenshot = doCursorOffScreenLocationBasedCorrection(screenshot);

        }
        return screenshot;
    }

    private BufferedImage doCursorOffScreenLocationBasedCorrection(BufferedImage screenshot) {
        sleep(2000);
        screenshot = vm.screenshot();
        Car testCar = new Car().setCarScreenshot(screenshot);
        for (int i = 0; i < 5; i++) {
            boolean duplicateCar = false;
            for (Car otherCar : building.getCars()) {
                if (testCar.equals(otherCar)) {
                    duplicateCar = true;
                }
            }
            if (duplicateCar) {
                System.out.println(vm.getUsername() + ": Cursor position not moved. Adjusting position.");
                vm.longTap(NEXTBUTTON);
                sleep(4000);
                screenshot = vm.screenshot();

            } else {
                System.out.println(vm.getUsername() + ": building"+building.getBuildingNumber()+". I cant see the car in the map.");
                break;
            }
            sleep(2000);
        }
        return screenshot;
    }

    private BufferedImage doCursorBasedCorrection(Car c, int offset, BufferedImage screenshot) {
        //tap the first 9 cars. the rest are out of luck.
        int carLocation = c.getLocation() + offset;
        Locations location = getCarLocationData(c);
        boolean retry = true;
        building.setSuspectProblem(false);
        for (int positionRetry = 0; positionRetry < 4; positionRetry++) {
            if (screenshot.getWidth()<location.getVerifyLocation().getX()||screenshot.getHeight()<location.getVerifyLocation().getY()){
                screenshot=vm.screenshot();
            }
            if (location.getVerifyLocation().approximateColorMatch(screenshot)) {
                System.out.println(vm.getUsername() + ": building"+building.getBuildingNumber()+" car " + carLocation + " scanned.");
                retry = false;
                break;
            }
            System.out.println(vm.getUsername() + ": We are not in position. changing postion");
            if (positionRetry == 2) {
                System.out.println(vm.getUsername() + ": Backswipe correction.");
                Locations.doBackSwipe(vm);
                sleep(4000);
            }
            vm.longTap(location.getTapLocation());
            sleep(2000 + (1000 * positionRetry));
            screenshot = vm.screenshot();

        }
        if (retry) {
            building.setSuspectProblem(true);
            building.setSuspsectProblemReason("Maximum attempts at repositioning cursor");
        }
        return screenshot;
    }

    private Locations getCarLocationData(Car c) {
        Locations location = Locations.
                valueOf((building.isFriendlyScan() ? "FRIENDLY" : "FOE")
                        + "CAR" + c.getLocation());
        return location;
    }

    public static LinkedList<Car> createBlanks(Building b, int number) {
        LinkedList<Car> al = new LinkedList<>();
        for (int i = 1; i < number+1; i++) {
            Car car = new Car().setLocation(i).setBuilding(b.getBuildingNumber());
            al.add(car);
        }
        return al;
    }

    private void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
