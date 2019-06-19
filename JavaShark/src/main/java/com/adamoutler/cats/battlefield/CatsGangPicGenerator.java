/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.adamoutler.cats.battlefield.building.car.Car;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.ImageJoiner;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.WritePicture;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class CatsGangPicGenerator {
    
    public static boolean saveGangPicToFile(File f, Battlefield battlefield){
        BufferedImage bi=organizePictures(battlefield);
        try {
            ImageIO.write(bi, "PNG", new FileOutputStream(f));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(CatsGangPicGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    
     public static BufferedImage organizePictures(Battlefield battlefield) {
        LinkedList<Car> cars = new LinkedList<>();
        battlefield.getBuildings().forEach(b -> {
            cars.addAll(b.getCars());
        });
        PicCoords pc = (ByteImage.getImageFromBytes(cars.get(0).getCarBadge()).getHeight() == Car.foeCarBadgeLocation.getH() ? Car.foeCarBadgeLocation : Car.friendlyCarBadgeLocation);
        int columns = 5;
        LinkedList<BufferedImage> rowOfCars = new LinkedList<>();
        LinkedList<BufferedImage> carRows = new LinkedList<>();
        for (int i = 1; i < cars.size() + 1; i++) {
            rowOfCars.add(ByteImage.getImageFromBytes(cars.get(i - 1).getCarJpg()));
            if (i % columns == 0 && i > 0) {
                carRows.add(ImageJoiner.joinImagesSide(rowOfCars));
                rowOfCars = new LinkedList<>();
            }

        }
        if (rowOfCars.size() < columns) {
            while (rowOfCars.size() < columns) {
                BufferedImage bi = WritePicture.createImage(pc, pc, "joined babysharks", false, new Color(0xff, 0x60, 0x5a), Color.blue, new java.awt.Font("Verdana", java.awt.Font.BOLD, 20));
                try {
                    BufferedImage shark = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("sharks.jpg"));
                    BufferedImage smallShark = toBufferedImage(shark.getScaledInstance(200, 200, Image.SCALE_DEFAULT));

                    WritePicture.addImage(bi, smallShark, 0.1f, 62, 20);
                } catch (IOException ex) {
                    Logger.getLogger(BattlefieldExcelGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
                rowOfCars.add(bi);
            }
            carRows.add(ImageJoiner.joinImagesSide(rowOfCars));

        }
        BufferedImage pic = ImageJoiner.joinImagesBelow(carRows);
        BufferedImage smallShark = WritePicture.createImage(new PicCoords(0, 0, pic.getWidth(), 80), new XYCoords(20, 65), " Baby Sharks Recon Report " + new Date().toGMTString(), false, new Color(0xff, 0x60, 0x5a), Color.DARK_GRAY, new java.awt.Font("courrier", java.awt.Font.BOLD, 57));
        LinkedList<BufferedImage> ll = new LinkedList<>();
        ll.add(smallShark);
        ll.add(pic);
        BufferedImage returnValue = ImageJoiner.joinImagesBelow(ll);
        return scale(returnValue,965, 1280);

    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }
}
