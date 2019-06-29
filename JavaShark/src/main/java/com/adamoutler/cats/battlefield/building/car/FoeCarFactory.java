/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building.car;

import com.adamoutler.cats.battlefield.BuildingUnscouttedException;
import com.adamoutler.cats.battlefield.building.Building;
import com.adamoutler.cats.battlefield.building.CarLocationOffScreenException;
import com.adamoutler.cats.battlefield.building.Locations;
import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class FoeCarFactory implements CarFactory {

    transient public final static XYCoords NEXTBUTTON = new XYCoords(302, 576);
    transient public final static XYCoords PREVIOUSBUTTON = new XYCoords(132, 576);
    transient public final static PicCoords NAMECOORDS = new PicCoords(6, 6, 300, 38);
    transient public final static PicCoords UNIQUECOORDS = new PicCoords(6, 230, 300, 24);
    transient final private static int SWORDCOLOR = 0x48a1b8;
    transient private final static PicCoords SMALL = new PicCoords(0, 0, 100, 300);
    transient final static Font FONT = new Font("Arial", Font.PLAIN, 35);
    final transient private VirtualBoxMachine vm;
    final transient private Building building;

    public FoeCarFactory(VirtualBoxMachine vm, Building building) {
        this.vm = vm;
        this.building = building;
        total = building.getSlotsTheirs() + building.getSlotsUnowned();
    }
    int passes = 0;  //total passes through buiding
    int scans = 0;  //number of cars scanned
    int total;

    public boolean scan() {

        LinkedHashMap<String, Car> foundCars = new LinkedHashMap<>();
        try {
            while (passes < 2) {  //do two passes;
                BufferedImage screenshot = vm.screenshot();

                selectNextCar();

                building.setTosb(new TeamOwnershipPercentageBar(screenshot));
                total = building.getSlotsTheirs() + building.getSlotsUnowned();
                if (exitCriteria(total, scans, passes)) {
                    break;
                }

                if (cursorIsAtStartPosition(screenshot) && scans > 1) {
                    passes++;
                }

                Car c = new Car().setCarScreenshot(screenshot, false);
                if (!c.isParked()) {
                    if (c.isUnscoutted()) {
                        removeBlankCars(building);
                        throw new BuildingUnscouttedException(vm.getUsername() + ": Building " + building.getBuildingNumber() + " is unscoutted");
                    }
                    foundCars.put(c.getNameHash() + c.getUniqueHash(), c);
                }
                scans++;

            }

            removeBlankCars(building);
            building.getCars().addAll(foundCars.values());
            if (foundCars.size() < building.getTosb().getNumberTheyOwn()) {
                building.setSuspsectProblemReason("Less cars than expected were found to be unique");
            }
            if (foundCars.size() > building.getTosb().getNumberTheyOwn()) {
                building.setSuspsectProblemReason("More cars than expected were found to be unique. Not too much of a problem.");
                building.setSuspectProblem(false);
            }
        } catch (BuildingUnscouttedException ex) {
            System.out.println(ex.getMessage());
        }
        return !building.isSuspectProblem();
    }

    private boolean exitCriteria(int total, int scans, int passes) {
        if (total < 1) {
            return true;
        }
        if (scans >= (total * 2) + 5) {
            // scan the building twice and add some slop.
            return true;
        }
        if (passes > 1) {
            return true;
        }
        return false;
    }

    private void selectNextCar() {
        vm.tap(NEXTBUTTON);
        sleep(700);
    }

    public static LinkedList<Car> createBlanks(Building b, int number) {
        LinkedList<Car> al = new LinkedList<>();
        for (int i = 1; i < number + 1; i++) {
            Car car = new Car().setLocation(i).setBuilding(b.getBuildingNumber());
            al.add(car);
        }
        return al;
    }

    private void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(FoeCarFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean cursorIsAtStartPosition(BufferedImage screenshot) {
        try {
            boolean b = Locations.cursorInPosition(Locations.FOECAR1, screenshot);
            if (b) {
                return true;
            } else {
                return false;
            }
        } catch (CarLocationOffScreenException ex) {
            return false;
        }
    }

    private void removeBlankCars(Building building) {
        ArrayList<Car> al = new ArrayList<>();
        for (Car c : building.getCars()) {
            if (c.getUniqueHash() == null || c.getUniqueHash().isEmpty()) {
                al.add(c);
            }
        }
        building.getCars().removeAll(al);
    }

}
