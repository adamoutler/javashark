/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class VirtualBoxScreenShot {

    String vmName;
    final static String VBOX = "VBoxManage";

    public static synchronized File getScreenshot(String vmName, File f) throws IOException, InterruptedException {
        System.out.println(f.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "controlvm", vmName, "screenshotpng", f.getAbsolutePath()});
        Process p = pb.start();
        p.waitFor(30000, TimeUnit.SECONDS);
        f.setReadable(true, false);
        f.setExecutable(true, false);
        f.setWritable(true, false);

        return f;
    }

}
