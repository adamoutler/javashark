/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import static com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw.DEFENDBUTTON;
import com.adamoutler.imagerecognition.Navigation;
import static com.adamoutler.imagerecognition.Navigation.BACKBUTTON;
import static com.adamoutler.imagerecognition.Navigation.sleep;
import static com.adamoutler.imagerecognition.Navigation.tapLocation;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public class CityKingsNavigation {

    public static void navigateToHome(JadbDevice jadb) throws JadbException {
        try {
            tapLocation(jadb, BACKBUTTON);
            sleep(500);
            tapLocation(jadb, BACKBUTTON);
            sleep(500);
            tapLocation(jadb, BACKBUTTON);
            sleep(500);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsNavigation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void navigateToHome(VirtualBoxMachine vm) {
        pressBackButton(vm);
        pressBackButton(vm);
        pressBackButton(vm);
    }

    public static void pressBackButton(VirtualBoxMachine vm) {
        vm.longTap(BACKBUTTON);
        sleep(500);
    }

    public static void navigateToCityKings(JadbDevice jadb) throws JadbException {
        navigateToHome(jadb);
        //tapLocation(jadb, CATSMainScreen.CITYKINGSBUTTON);
        sleep(1000);
    }

    public void navigateToBuilding(JadbDevice jadb, XYCoords building) throws IOException, JadbException {
        //Navigation.adbNavigateToCityKings(jadb);
        Navigation.tapLocation(jadb, building);
        sleep(1000);
        Navigation.tapLocation(jadb, DEFENDBUTTON);
        sleep(1000);
    }

}
