/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class TempFolderImage {

    public static void writePicture(BufferedImage bi, String name) {
        try {
            String filename = System.getProperty("java.io.tmpdir") + File.separator + "tempImage/" + name + ".png";
            new File(filename).getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                ImageIO.write(bi, "PNG", fos);
            }
            System.out.println("wrote " + filename);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
