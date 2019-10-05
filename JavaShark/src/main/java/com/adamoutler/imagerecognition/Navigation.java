/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import com.adamoutler.cats.screen_id.ScreenData;
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
public abstract class Navigation extends Screenshottable {

    transient private JadbDevice jadb;
    final public static XYCoords BACKBUTTON = new XYCoords(38, 747);
    final public static XYCoords GANGBUTTON = new XYCoords(911, 137);

    public Navigation(JadbDevice jadb) {
        this.jadb = jadb;
    }

    public static void navigateToCityKings(VirtualBoxMachine vm) throws JadbException, IOException {
        navigateToHome(vm);
        tapLocation(vm.getJadb(), GANGBUTTON);

    }

    public static void tapLocation(JadbDevice jadb, XYCoords xy) throws IOException, JadbException {
        jadb.execute("input", "tap", Integer.toString(xy.getX()), Integer.toString(xy.getY()));
    }

    public static void pressLocation(JadbDevice jadb, XYCoords xy) throws IOException, JadbException {
        jadb.execute("input", "swipe", Integer.toString(xy.getX()), Integer.toString(xy.getY()), Integer.toString(xy.getX()), Integer.toString(xy.getY()));
    }

    public static void swipe(JadbDevice jadb, Swipe s) throws IOException, JadbException {
        jadb.execute("input", "swipe",
                Integer.toString(s.getFromX()),
                Integer.toString(s.getFromY()),
                Integer.toString(s.getToX()),
                Integer.toString(s.getToY()),
                Integer.toString(s.getSpeed()));

    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Navigation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void navigateToHome(VirtualBoxMachine vm) throws JadbException, IOException {
        for (int retries = 0; retries < 4; retries++) {
            for (int i = 0; i < 4; i++) {
                Navigation.tapLocation(vm.getJadb(), BACKBUTTON);
                sleep(1000);
            }
            ScreenData sd = vm.getScreen();
            if (sd.equals(ScreenData.CATSLOADEDCHAMPIONSHIPSCREEN)
                    || sd.equals(ScreenData.CATSLOADEDULTIMATESCREEN)) {
                break;
            }
        }
    }

    /**
     * @return the jadb
     */
    public JadbDevice getJadb() {
        return jadb;
    }

    /**
     * @param jadb the jadb to set
     */
    public void setJadb(JadbDevice jadb) {
        this.jadb = jadb;
    }

}
