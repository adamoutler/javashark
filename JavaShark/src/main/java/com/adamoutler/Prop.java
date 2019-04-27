/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class Prop {

    static Properties p = new Properties();

    static {
        try {
            String folder = getPropertiesFileFolder();
            File f = new File(folder + "/properties.prop");
            System.out.println("Searching for properties in " + f.getAbsolutePath());
            if (!f.exists()) {
                throw new FileNotFoundException("could not locate properties file at " + f.getAbsolutePath());
            }
            FileInputStream is = new FileInputStream(folder+"properties.prop");
            p.load(is);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(Prop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getPropertiesFileFolder() throws URISyntaxException {
        String folder = new File(Prop.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getParent();
        if (folder.endsWith("target")) {
            folder = folder.replace("target", "");
        }
        return folder;
    }

    public static String getProp(String prop) {
        return p.getProperty(prop);
    }

    public static Properties getProperties() {
        return p;
    }

}
