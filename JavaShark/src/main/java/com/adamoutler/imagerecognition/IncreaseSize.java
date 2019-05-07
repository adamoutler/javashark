/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class IncreaseSize {
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
}  
    public static BufferedImage doubleSize(BufferedImage img){
        return resize(img,img.getWidth()*2,img.getHeight()*2);
    }
    
    public static BufferedImage quadrupleSize(BufferedImage img){
        return doubleSize(doubleSize(img));
    }
    
    
}
