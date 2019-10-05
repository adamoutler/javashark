/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import static com.adamoutler.imagerecognition.Navigation.sleep;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbDevice;

/**
 *
 * @author adamo
 */
public abstract class OcrScreenshot extends Navigation {

    public OcrScreenshot(JadbDevice jadb) {
        super(jadb);
    }

    private AtomicBoolean ocr = new AtomicBoolean(false);

    abstract public void setStringsOCR();

    public void runOCR() {
        try {
            BufferedImage image = getScreenshot();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(OcrScreenshot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the ocr
     */
    public boolean isOcr() {
        return ocr.get();
    }

    public void waitForOcr() {
        while (!ocr.get()) {
            sleep(50);
        }
    }

    /**
     * @param ocr the ocr to set
     */
    public void setOcr(boolean ocr) {
        this.ocr.set(ocr);
    }

}
