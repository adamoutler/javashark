/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import cn.easyproject.easyocr.EasyOCR;
import com.adamoutler.cats.citykings.hash_gathering.CityKingsMatchRaw;
import com.google.common.io.Files;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Uses EasyOCR to perform OCR operations on a mobile device.
 *
 * @author aoutler
 */
public class OCR extends EasyOCR {

    /**
     * Default constructor for OCR
     */
    public OCR() {
        tempdir = Files.createTempDir();
        tempdir.deleteOnExit();
        setTesseractPath(getTesseractPathFile());
    }

    public void main(String[] args) {  //main method for testing
        OCR ocr = new OCR();
        ocr.setTesseractPath(ocr.getTesseractPathFile());
        File f = new File("src\\test\\java\\com\\adamoutler\\cats\\catsobjects\\resources\\allthescreens\\CITYKINGS.3.png");
        try {
            BufferedImage bi = ImageIO.read(f);
            String ourScore = ocr.discern(CityKingsMatchRaw.OURSCORECOORDS.getImageAtCoords(bi));
            String theirScore = ocr.discern(CityKingsMatchRaw.THEIRSCORECOORDS.getImageAtCoords(bi));
            String discern = new OCR().discern(bi);
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * path to expected location of Windows Tesseract
     */
    public File tesseractWindows = new File("C:\\Users\\adamo\\OneDrive\\Desktop\\jTessBoxEditor\\tesseract-ocr\\tesseract.exe");
    /**
     * path to expected location of Linux Tesseract
     */
    public File tesseractLinux = new File("/usr/bin/tesseract");

    /**
     * location for tesseract binary
     */
    public File tesseractLocation = getTesseractPathFile();

    /**
     * set the path to the tesseract binary if required
     *
     * @param path the File object representing Tesseract
     * @return reference to this object
     * @throws TesseractNotFoundException if tesseract is not found
     */
    File tempdir;

    final public OCR setTesseractPath(File path) {

        this.setTesseractPath(path.getAbsolutePath());
        return this;
    }

    /**
     * Convert the selected picture to text.
     *
     * @param f File reference to the picture to be OCR'd
     * @return text representation of the picture provided
     */
    public String getTextFromPicture(File f) {
        this.setTesseractPath(tesseractLocation);
        return this.discern(f);
    }

    public String discern(BufferedImage bi) {

        try {
            File temp = File.createTempFile(tempdir.getAbsolutePath(), ".PNG");
            ImageIO.write(bi, "PNG", temp);
            return discern(temp);
        } catch (IOException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    /**
     * Provide references to the location of Tesseract
     *
     * @return expected location to Tesseract.
     */
    private File getTesseractPathFile() {

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return tesseractWindows;
        }

        return tesseractLinux;
    }

}
