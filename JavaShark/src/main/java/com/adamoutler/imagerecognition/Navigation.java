/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;
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

   public Navigation(JadbDevice jadb) {
        this.jadb = jadb;
    }


  

    public static void tapLocation(JadbDevice jadb, XYCoords xy) throws IOException, JadbException {
        jadb.execute("input", "tap", Integer.toString(xy.getX()), Integer.toString(xy.getY()));
    }

    public  static void swipe(JadbDevice jadb, Swipe s) throws IOException, JadbException {
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

    
    
    
    
  

    public static void navigateToHome(JadbDevice jadb1) throws JadbException, IOException {
        Navigation.tapLocation(jadb1, BACKBUTTON);
        sleep(1000);
        Navigation.tapLocation(jadb1, BACKBUTTON);
        sleep(1000);
        Navigation.tapLocation(jadb1, BACKBUTTON);
        sleep(1000);
        Navigation.tapLocation(jadb1, BACKBUTTON);
        sleep(1000);
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
