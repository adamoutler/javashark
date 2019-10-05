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
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class VirtualBoxScreenShot {

    String vmName;
    final static String VBOX = "VBoxManage";

    final public static synchronized File getScreenshotFile(String vmName, File f) throws IOException, InterruptedException {
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

    final public static synchronized BufferedImage getScreenshot(VirtualBoxMachine vm) throws IOException, InterruptedException {
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
        File ramdisk = new File("/tmp/ramdisk");
        File f;
        if (ramdisk.exists()) {
            f = File.createTempFile("screenshot", ".png", ramdisk);
        } else {
            f = File.createTempFile("screenshot", ".png");
        }
        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "controlvm", vmName, "screenshotpng", f.getAbsolutePath()});
        Process p = pb.start();
        p.waitFor(3, TimeUnit.SECONDS);

        return f;
    }

    private static void writeToFile(BufferedImage bi, String f) {
        try {
            ImageIO.write(bi, "PNG", new File(f));
        } catch (IOException ex) {
            Logger.getLogger(VirtualBoxScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static String writeScreenshot(VirtualBoxMachine vm, String baseFolder) {
        try {
            BufferedImage screenshot = getScreenshot(vm);
            StringBuilder sb = new StringBuilder();
            sb.append("/").append(vm.getUsername()).append("-").append(vm.getGang()).append("-").append(vm.getCurrentTask()).append(UUID.randomUUID()).append(".png");
            writeToFile(screenshot, baseFolder + sb.toString());
            return sb.toString();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(VirtualBoxScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

}
