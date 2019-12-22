/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class BattlefieldNavigator {

    final public static XYCoords BACKBUTTON = new XYCoords(38, 747);
    final public static XYCoords GANGBUTTON = new XYCoords(911, 137);
    final public static XYCoords COLLECTPRIZES = new XYCoords(500, 520);
    final public static XYCoords VICTORYDEFEATBUTTON = new XYCoords(500, 725);

    boolean reported = false;

    public enum BuildingData {
        BUILDING1NAME(new PicCoords(178, 345, 110, 25)),
        BUILDING1SIZE(new PicCoords(314, 345, 36, 22)),
        BUILDING2NAME(new PicCoords(122, 495, 108, 25)),
        BUILDING2SIZE(new PicCoords(258, 495, 36, 22)),
        BUILDING3NAME(new PicCoords(366, 584, 109, 25)),
        BUILDING3SIZE(new PicCoords(504, 585, 36, 22)),
        BUILDING4NAME(new PicCoords(641, 595, 110, 25)),
        BUILDING4SIZE(new PicCoords(779, 595, 36, 22)),
        BUILDING5NAME(new PicCoords(848, 500, 110, 25)),
        BUILDING5SIZE(new PicCoords(984, 499, 36, 22)),
        BUILDING6NAME(new PicCoords(832, 328, 110, 25)),
        BUILDING6SIZE(new PicCoords(968, 329, 36, 22));
        private PicCoords coords;

        BuildingData(PicCoords coords) {
            this.coords = coords;
        }

        /**
         * @return the coords
         */
        public PicCoords getCoords() {
            return coords;
        }
    }

    int iterations = 0;

    public boolean navigateToCK(VirtualBoxMachine vm) {
        if (vm.isKillThread()) {
            return false;
        }

        if (iterations++ == 15) {
            System.err.println("ERROR: Could not navigate to city kings!  help!");
            vm.setKillThread(true);
            return false;
        }
        ScreenData id = vm.getScreen();
        if (id == ScreenData.CITYKINGS || id == ScreenData.CITYKINGSBEGINTHEATTACK || id == ScreenData.CITYKINGSENLIST) {
            System.out.println(vm.getUsername() + ": I'm at " + id + " screen!");

            return true;
        }
        if (id.toString().startsWith("CITYKINGSBUILDING")) {
            id = ScreenData.CITYKINGSBUILDINGSCREEN;
        }

        switch (id) {
            case CATSLOADEDULTIMATESCREEN:
            case CATSLOADEDCHAMPIONSHIPSCREEN:
                System.out.println(vm.getUsername() + ": I'm at the home screen - Entering Battlefield");
           //     vm.longTap(GANGBUTTON);
                sleep(6000);
                break;
            case CITYKINGSBUILDINGSCREEN:
                System.out.println(vm.getUsername() + ": I'm in a building, pressing in-game back button");
                vm.longTap(BACKBUTTON);
                sleep(2000);
                break;
            case CITYKINGSVICTORY:
            case CITYKINGSDEFEAT:
             //   vm.longTap(VICTORYDEFEATBUTTON);
                break;

            case UNKNOWN:
                vm.longTap(COLLECTPRIZES);
                if (!reported) {
                    vm.writeBabySharksDebug(vm, "Attempting to navigate to CK couldn't recognize screen");
                    reported = true;
                    vm.longTap(BACKBUTTON);
                }

            case WHATCANYOUDOTODAY:
                vm.tap(BattlefieldNavigator.BACKBUTTON);
                vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_BACK"});
                vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_BACK"});
                break;
            case BLACKSCREEN://do nothing. we are loading.
                break;
            default:
                vm.adbShell("input", "keyevent", "KEYCODE_BACK");
                sleep(4000);
                System.out.println(vm.getUsername() + ": I'm at " + id.toString() + " screen. pressing back.");
        }

        return (navigateToCK(vm));

    }

    public boolean navigateToBuildingScreen(VirtualBoxMachine vm, int buildingNumber) {
        if (!navigateToCK(vm)) {
            return false;
        }
        BuildingData bd = BuildingData.valueOf("BUILDING" + (buildingNumber) + "NAME");
        vm.tap(bd.getCoords());
        sleep(2000);
        if (checkIfInBuilding(vm.getScreen())) {
            return true;
        } else {
            ScreenData sd = vm.getScreen();

            if (checkIfInBuilding(vm.getScreen())) {
                return true;
            } else {
                new BattlefieldNavigator().navigateToCK(vm);
                return new BattlefieldNavigator().navigateToBuildingScreen(vm, buildingNumber);
            }
        }

    }

    private boolean checkIfInBuilding(ScreenData sd) {
        return sd.toString().startsWith("CITYKINGSBUILDING");
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(BattlefieldNavigator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
