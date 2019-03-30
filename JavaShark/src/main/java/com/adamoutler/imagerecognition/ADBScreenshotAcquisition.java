/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;


import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

/**
 *
 * @author adamo
 */
public class ADBScreenshotAcquisition {

    
    
    
    //For swiping on Gang Fights Screen: adb shell input swipe 300 760 300 289 3000
    
    
    public static final String ADB = "C:\\Users\\adamo\\OneDrive\\Desktop\\platform-tools\\adb.exe";
    private transient JadbDevice jadb;

    
    public CityKingsCarRaw acquireCityKingsImage(JadbDevice jadb){
        this.jadb=jadb;
        try {
            ADBScreenshotAcquisition asa = new ADBScreenshotAcquisition();
            BufferedImage bi=asa.acquire(ADB, "fuzy");
            CityKingsCarRaw images=asa.createCarProfile(bi,jadb);
            return images;
        } catch (IOException ex) {
            Logger.getLogger(ADBScreenshotAcquisition.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    File[] writeImages(CityKingsCarRaw car, String dir, int id) throws IOException{
        File[] f=new File[3];
        f[0]=new File("building"+id+".png");
        f[1]=new File("name"+id+".png");
        f[2]=new File("unique"+id+".png");
        ImageIO.write(car.getNamePic(), "png",f[1]);
        ImageIO.write(car.getUniquePic(), "png",f[2]);
        
        return f;
    }
    
    CityKingsCarRaw createCarProfile(BufferedImage screenShot,JadbDevice jadb){
       
        
        CityKingsCarRaw car=new CityKingsCarRaw(jadb,ByteImage.getPngBytes(new BufferedImage(0,0,0)),1,1);

        return car;
    }
    
    private static int rgbToGray(int rgb, MODE mode) {
    // split rgb integer into R, G and B components
    int r = (rgb >> 16) & 0xff;
    int g = (rgb >> 8) & 0xff;
    int b = rgb & 0xff;
    int gray;
    // Select mode
    switch (mode) {
        case LIGHTNESS:
            gray = Math.round((Math.max(r, Math.max(g, b)) + Math.min(r, Math.min(g, b))) / 2);
            break;
        case LUMINOSITY:
            gray = Math.round(0.21f * r + 0.72f * g + 0.07f * b);
            break;
        case AVERAGE:
        default:
            gray = Math.round((r + g + b) / 3);
            break;
    }
    return gray;
}
    
    private enum MODE {
    LIGHTNESS, AVERAGE, LUMINOSITY
}
    private BufferedImage acquire(String adb, String name) throws IOException {

        File f = File.createTempFile("name", ".png");
        RemoteFile rf = new RemoteFile("/sdcard/scr.png");
        
        try {
            jadb.executeShell("screencap", "-p", rf.getPath());
            Thread.sleep(400);
            jadb.pull(rf, f);
            
        } catch (JadbException | InterruptedException ex) {
            Logger.getLogger(ADBScreenshotAcquisition.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedImage bi = ImageIO.read(f);

        return bi;
    }

    

}
