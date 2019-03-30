/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public class ADB {

    public static String ADB = "adb";

    public static String runAdbShellCommand(int sleepSeconds, JadbDevice device, String command, String[] args) throws JadbException, IOException {
        int sleepMillis = sleepSeconds * 1000;
        InputStream bis = (InputStream) device.executeShell(command, args);
        try {
            Thread.sleep(sleepMillis);
            return streamToString(bis);
        } catch (InterruptedException ex) {
            Logger.getLogger(ADB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private static String streamToString(InputStream bis) {
        Scanner s = new Scanner(bis).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

}
