/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.adamoutler.cats.battlefield.building.car.Car;
import com.adamoutler.cats.screen_id.BattlefieldFactoryOcr;
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

    public static boolean saveGangPicToFile(File f, Battlefield battlefield) {
        BufferedImage bi = organizePictures(battlefield);
        try {
            try (FileOutputStream fos = new FileOutputStream(f)) {
                ImageIO.write(bi, "PNG", fos);
                System.out.println("Wrote group picture to " + f.getAbsolutePath());
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(CatsGangPicGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static BufferedImage organizePictures(Battlefield battlefield) {
        Color color = (battlefield.getBuildings().get(0).isFriendlyScan()) ? new Color(0xa9, 0xca, 0xfa) : new Color(0xff, 0x60, 0x5a);

        LinkedList<Car> cars = new LinkedList<>();
        battlefield.getBuildings().forEach(b -> {
            cars.addAll(b.getCars());
        });
        PicCoords pc;
        if (battlefield.getBuildings().get(0).isFriendlyScan()) {
            pc = Car.friendlyCarBadgeLocation;
        } else {
            pc = Car.foeCarBadgeLocation;
        }
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
                BufferedImage bi = WritePicture.createImage(pc, pc, "", false, color, Color.blue, new java.awt.Font("Verdana", java.awt.Font.BOLD, 20));
                rowOfCars.add(bi);
            }
            carRows.add(ImageJoiner.joinImagesSide(rowOfCars));

        }
        BufferedImage pic = ImageJoiner.joinImagesBelow(carRows);
        BufferedImage header = WritePicture.createImage(new PicCoords(0, 0, pic.getWidth(), 80), new XYCoords(20, 65), " Baby Sharks " + (battlefield.getBuildings().get(0).isFriendlyScan() ? "Scan" : "Recon") + " Report " + new Date().toGMTString(), false, color, Color.DARK_GRAY, new java.awt.Font("courrier", java.awt.Font.BOLD, 49));
        BufferedImage footer = WritePicture.createImage(new PicCoords(0, 0, pic.getWidth(), 80), new XYCoords(20, 45), "Opponent OCR name: \"" + battlefield.getOcrName() + "\"", false, color, Color.DARK_GRAY, new java.awt.Font("courrier", java.awt.Font.BOLD, 30));
        BufferedImage bi= scale(battlefield.getOcrNamePic(), (int)Math.round(BattlefieldFactoryOcr.opponentName.getW()*.75),(int)Math.round(BattlefieldFactoryOcr.opponentName.getH()*.75));
        
        WritePicture.replaceColor(bi, 0xfffffF, color.getRGB(), 100);
        WritePicture.addImage(footer,bi, 1, footer.getWidth()-BattlefieldFactoryOcr.opponentName.getW(), 15);
        LinkedList<BufferedImage> imageCombinations = new LinkedList<>();
        imageCombinations.add(header);
        imageCombinations.add(pic);
        imageCombinations.add(footer);
        BufferedImage combinedImage = ImageJoiner.joinImagesBelow(imageCombinations);
        return combinedImage;

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
