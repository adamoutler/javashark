/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler;

import java.io.File;
import java.io.FileInputStream;
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

            File f = new File("./properties.prop");
            System.out.println("Looking for properties file in " + f.getAbsolutePath());
            FileInputStream is = new FileInputStream(f.getAbsolutePath());
            p.load(is);
        } catch (IOException ex) {
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
