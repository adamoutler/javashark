/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

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

    public static synchronized File getScreenshotFile(String vmName, File f) throws IOException, InterruptedException {
        System.out.println(f.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "controlvm", vmName, "screenshotpng", f.getAbsolutePath()});
        Process p = pb.start();
        p.waitFor(5, TimeUnit.SECONDS);
        Thread.sleep(50);
        f.setReadable(true, false);
        f.setExecutable(true, false);
        f.setWritable(true, false);

        return f;
    }

    public static synchronized BufferedImage getScreenshot(VirtualBoxMachine vm) throws IOException, InterruptedException {
        File f = getVboxScreenshot(vm.getVMName());
        BufferedImage bi;
        try (FileInputStream fis = new FileInputStream(f)) {
            bi = ImageIO.read(fis);
        }
        handleDeletion(f);
        return bi;
    }

    private static void handleDeletion(File f) {
        try {
            f.deleteOnExit();
            f.delete();
        } catch (Exception ex) {
            // do nothing. it will be deleted eventually.
        }
    }

    private static synchronized File getVboxScreenshot(String vmName) throws IOException, InterruptedException {
        File f = File.createTempFile("screenshot", ".png");
        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "controlvm", vmName, "screenshotpng", f.getAbsolutePath()});
        Process p = pb.start();
        p.waitFor(3, TimeUnit.SECONDS);

        return f;
    }
}
