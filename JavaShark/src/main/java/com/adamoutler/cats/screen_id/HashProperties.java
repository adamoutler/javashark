/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.PicCoords;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class HashProperties  {
    private static Properties props = Hashes.PROPERTIES;

    

    /**
     * @return the props
     */
    public static Properties getProps() {
        return props;
    }

    /**
     * @param aProps the props to set
     */
    public static void setProps(Properties aProps) {
        props = aProps;
    }
}
