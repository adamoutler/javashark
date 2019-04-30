/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class DirectTesseract {

    /**
     * path to expected location of Windows Tesseract
     */
    public static File tesseractWindows = new File("C:\\Users\\adamo\\OneDrive\\Desktop\\jTessBoxEditor\\tesseract-ocr\\tesseract.exe");
    /**
     * path to expected location of Linux Tesseract
     */
    public static File tesseractLinux = new File("/usr/bin/tesseract");

    public static String runTesseractSingleWord(BufferedImage bi) {
        try {
            File inputFile = File.createTempFile("TesseractJavaShark", ".png");
            File textFile = File.createTempFile("TesseractJavaShark", "");
            FileOutputStream fos = new FileOutputStream(inputFile);
            ImageIO.write(bi, "PNG", fos);
            fos.close();
            ProcessBuilder pb = new ProcessBuilder(getTesseractPathFile().getAbsolutePath(), "--psm", "4", inputFile.getAbsolutePath(), textFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            inputFile.deleteOnExit();
            textFile.deleteOnExit();
            InputStream is = p.getInputStream();
            p.waitFor(5, TimeUnit.SECONDS);
            Thread.sleep(4);
            while (is.available() > 0) {
                System.out.print((char) is.read());
            }
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(textFile + ".txt");
            while (fis.available() > 0 || !sb.toString().endsWith("\n")) {
                sb.append((char) fis.read());
            }
            fis.close();

            return sb.toString().trim();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(DirectTesseract.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Provide references to the location of Tesseract
     *
     * @return expected location to Tesseract.
     */
    private static File getTesseractPathFile() {

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return tesseractWindows;
        }

        return tesseractLinux;
    }
}
