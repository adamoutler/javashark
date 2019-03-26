/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class Screenshottable {

    private static String virtualMachineName;
    
    public static void setVMName(String vmName){
        virtualMachineName=vmName;
    }
    
    
    final public static String PICSDIR =  System.getProperty("java.io.tmpdir")+"/catsscores/";

    private static String getTempDir() {
       File tempdir=new File(PICSDIR);
       if (!tempdir.isDirectory()){
           tempdir.mkdirs();
       }
       return tempdir.getAbsolutePath()+"/";
    }

    final public static BufferedImage getScreenshot() throws IOException, InterruptedException {
        Thread.sleep(200);
        BufferedImage screenshot = VirtualBoxScreenShot.obtainVBoxScreenshot(getVirtualMachineName());
        writePic(screenshot, "lastscreenshot");
        return screenshot;
    }

    final public static  BufferedImage getFromPic(BufferedImage pic, PicCoords coords) {
        return pic.getSubimage(coords.getX(), coords.getY(), coords.getW(), coords.getH());
    }

 

    final static public File[] writePics(List<BufferedImage> bi, String name){
        ArrayList<File> files =new ArrayList<>();
        for (int i=0; i<bi.size(); i++){
            files.add(writePic(bi.get(i),name+(i+1)));
        }
        return files.toArray(new File[files.size()]);
    }
    final static public File writePic(BufferedImage bi, String name) {
        File out = new File(getTempDir()+name+".png");
        try {
            try (FileOutputStream fos = new FileOutputStream(out)) {
                ImageIO.write(bi, "png", fos);
                fos.flush();
            }
            return out;
        } catch (IOException ex) {
            Logger.getLogger(Screenshottable.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void writePics(String... s) {
        Class clazz = this.getClass();
        String identifier = "";
        if (s.length > 0) {
            identifier = s[0];
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getType().getName().equals(BufferedImage.class.getTypeName())) {
                f.setAccessible(true);
                try {
                    BufferedImage bi = (BufferedImage) f.get(this);
                    File out = new File(getTempDir()+ getClass().getSimpleName() + identifier + f.getName() + ".jpg");
                    if (!new File(out.getParent()).exists()) {
                        new File(out.getParent()).mkdirs();
                    }
                    ImageIO.write(bi, "jpg", out);
                    //src/test/java/com/adamoutler/cats/citykings/

                } catch (IllegalArgumentException | IllegalAccessException | IOException ex) {
                    Logger.getLogger(Screenshottable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public int getPixelColor(XYCoords xy) {
        int i=0;
        try {
            i = getScreenshot().getRGB(xy.getX(), xy.getY());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Screenshottable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    /**
     * @return the virtualMachineName
     */
    public static String getVirtualMachineName() {
        return virtualMachineName;
    }

}
