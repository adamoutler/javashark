/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building;

import com.adamoutler.cats.battlefield.building.car.Car;
import com.adamoutler.cats.battlefield.building.car.CarFactory;
import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class BuildingFactory {

    transient final public static XYCoords DEFENDBUTTON = new XYCoords(349, 390);
    transient final public static XYCoords ATTACKBUTTON = new XYCoords(670, 390);
    private final VirtualBoxMachine vm;
    private final Building building;

    public BuildingFactory(VirtualBoxMachine vm, Building building) {
        this.vm = vm;
        this.building = building;
        building.setVm(vm);
    }

    public boolean scan() {
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(vm.screenshot())));
        ScreenData sd = ScreenData.identifyScreen(building.getTosb().getBi());
        if (sd.equals(ScreenData.CITYKINGSBUILDINGSCREEN)&& isValidBuilding()) {
            tapAttackOrDefend();
            for (int retries = 0; retries < 5; retries++) {
                if(vm.isKillThread())break;
                setUpCarData();
                boolean b = new CarFactory(vm, building).scan();
                if (checkCarData() && b) {
                    break;
                }
                if (retries == 5) {
                    return false;
                }
            }

        }
        return !building.isSuspectProblem();
    }

    private void setUpCarData() {
        setCarsInBuilding();
        building.setSuspectProblem(false);
    }

    private void setCarsInBuilding() {
        if (meetsFriendlyScanCriteria()) {
            building.setCars(CarFactory.createBlanks(building, building.getTosb().numberWeOwn()));
            sleep(2000);
        } else if (meetsFoeScanCriteria()) {
            building.setCars(CarFactory.createBlanks(building, building.getTosb().numberTheyOwn()));
            sleep(2000);
        }

    }

    private boolean meetsFoeScanCriteria() {
        return !building.isFriendlyScan() && building.isFriendlyScan() && building.getSlotsTheirs() > 0;
    }

    private boolean meetsFriendlyScanCriteria() {
        return building.isFriendlyScan() && building.getSlotsOurs() > 0;
    }

    private void tapAttackOrDefend() {
        BufferedImage screen = vm.screenshot();
        int i = 0;
        ScreenData sd=vm.getScreen();
        while (sd!=ScreenData.CITYKINGSBUILDINGDEFEND && sd!=ScreenData.CITYKINGSBUILDINGATTACK){
            if (i++ == 4) {
                vm.logUnidentifiedScreen("Expecting defend or attack screen");
                vm.writeBabySharksDebug(vm, "Attempting to enter building but couldn't recognize screen");
                break;
            }
            System.out.println(vm.getUsername() + ": Attempting to enter building " + building.getBuildingNumber() + " cars screen");
            if (meetsFriendlyScanCriteria() || meetsFoeScanCriteria()) {
                vm.longTap(building.isFriendlyScan() ? DEFENDBUTTON : ATTACKBUTTON);
            }
            sleep(2000);
            sd=vm.getScreen();
        }
    }

    private boolean checkCarData() {
        for (Car testCar : building.getCars()) {
            for (Car otherCar : building.getCars()) {
                if (testCar.equals(otherCar) && testCar.getLocation() != otherCar.getLocation()) {
                    System.out.println("duplicate cars:" + testCar);
                    System.out.println("duplicate cars:" + otherCar);
                    building.setSuspsectProblemReason("Duplicate car detected");
                }
            }
            if (testCar.isParked()) {
                building.setSuspsectProblemReason("Parked car detected");
            }
            if (testCar.hashCode() == 14065) {
                building.setSuspsectProblemReason("Car was not scanned! We didn't scan one of the cars");
            }
            if (testCar.getNameHash() == null || testCar.getNameHash().isEmpty()) {
                building.setSuspsectProblemReason("Missing Car detected");
            }
        }

        return !building.isSuspectProblem();
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);

        } catch (InterruptedException ex) {
            Logger.getLogger(BuildingFactory.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isValidBuilding() {
       if (building.isFriendlyScan()){
           return building.getTosb().numberWeOwn()>0;
       } else {
           return building.getTosb().numberTheyOwn()>0;
       }
    }

}
