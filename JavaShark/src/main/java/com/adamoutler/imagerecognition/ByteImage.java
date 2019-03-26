/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class ByteImage {

        public static BufferedImage getImageFromBytes(byte[] bytes) {

        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException ex) {
            Logger.getLogger(ByteImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static byte[] getPngBytes(BufferedImage bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ByteImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static byte[] getJpgBytes(byte[] bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            ImageIO.write(getImageFromBytes(bi), "JPG", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ByteImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



}
