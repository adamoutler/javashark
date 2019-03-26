/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class Screens {
    
    public static ScreenData identify(BufferedImage bi){
        return doIdentification(bi);
        
    }

    private static ScreenData doIdentification(BufferedImage bi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
