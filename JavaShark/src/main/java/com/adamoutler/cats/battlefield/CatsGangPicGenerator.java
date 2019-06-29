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
import com.adamoutler.records_search.SearchException;
import com.adamoutler.records_search.search_assemblers.HistoricalImageSearch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public static boolean saveAllReconFromTodayToFile(File targetLocation, Battlefield battlefield) {

        LinkedList<Battlefield> battlefields = new LinkedList<>();
        battlefields.add(battlefield);

        try {
            LinkedList<File> records = HistoricalImageSearch.getReconRecordsFromDayBasedOnOpponent(battlefield.getOcrName(), "/var/www/baby-sharks/excel/", new Date());
            Gson gson = new GsonBuilder().create();
            for (File record : records) {
                try {
                    FileReader fr = new FileReader(record);
                    battlefields.add(gson.fromJson(fr, Battlefield.class));

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CatsGangPicGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            BufferedImage bi = organizePictures(battlefields.toArray(new Battlefield[battlefields.size()]));

            try (FileOutputStream fos = new FileOutputStream(targetLocation)) {
                ImageIO.write(bi, "PNG", fos);
                System.out.println("Wrote group picture to " + targetLocation.getAbsolutePath());
            }
            return true;
        } catch (IOException | SearchException ex) {
            Logger.getLogger(CatsGangPicGenerator.class.getName()).log(Level.SEVERE, null, ex);

        }
        return false;
    }

    public static boolean saveGangPicToFile(File f, Battlefield battlefield) {

        LinkedList<Battlefield> battlefields = new LinkedList<>();
        battlefields.add(battlefield);

        BufferedImage bi = organizePictures(battlefields.toArray(new Battlefield[battlefields.size()]));
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

    public static BufferedImage organizePictures(Battlefield... battlefields) {
        Battlefield battlefield = battlefields[0];
        Color color = (battlefield.getBuildings().get(0).isFriendlyScan()) ? new Color(0xa9, 0xca, 0xfa) : new Color(0xff, 0x60, 0x5a);

        LinkedList<Car> cars = new LinkedList<>();
        battlefield.getBuildings().forEach(b -> {
            cars.addAll(b.getCars());
        });
        Collections.sort(cars, (Car car1, Car car2) -> Collator.getInstance().compare(car1.getNameHash(), car2.getNameHash()));

        PicCoords pc;
        if (battlefield.getBuildings()
                .get(0).isFriendlyScan()) {
            pc = Car.friendlyCarBadgeLocation;
        } else {
            pc = Car.foeCarBadgeLocation;
        }
        LinkedList<BufferedImage> carRows = getCarRows(cars, pc, color);
        int width = 1551;
        BufferedImage pic = null;
        if (carRows.size() > 0) {
            pic = ImageJoiner.joinImagesBelow(carRows);
        }
        BufferedImage header = WritePicture.createImage(new PicCoords(0, 0, width, 80), new XYCoords(20, 65), " Baby Sharks " + (battlefield.getBuildings().get(0).isFriendlyScan() ? "Scan" : "Recon") + " Report " + new Date().toGMTString(), false, color, Color.DARK_GRAY, new java.awt.Font("courrier", java.awt.Font.BOLD, 49));
        BufferedImage footer = WritePicture.createImage(new PicCoords(0, 0, width, 80), new XYCoords(20, 50), "Opponent identifier: \"" + battlefield.getOcrName() + "\"", false, color, Color.DARK_GRAY, new java.awt.Font("courrier", java.awt.Font.BOLD, 21));

        BufferedImage bi = scale(battlefield.getOcrNamePic(), (int) Math.round(BattlefieldFactoryOcr.opponentName.getW() * .75), (int) Math.round(BattlefieldFactoryOcr.opponentName.getH() * .75));

        if (bi != null) {
            WritePicture.replaceColor(bi, 0xfffffF, color.getRGB(), 100);
            WritePicture.addImage(footer, bi, 1, footer.getWidth() - BattlefieldFactoryOcr.opponentName.getW(), 15);
        }
        LinkedList<BufferedImage> imageCombinations = new LinkedList<>();

        imageCombinations.add(header);

        if (pic != null) {
            imageCombinations.add(pic);
        }
        LinkedList<BufferedImage> additional = new LinkedList<>();
        if (battlefields.length > 1) {
            if (battlefields[1] != null) {
                additional.add(WritePicture.createImage(new PicCoords(0, 0, width, 80), new XYCoords(20, 65), "Previous results", false, color, Color.DARK_GRAY, new java.awt.Font("courrier", java.awt.Font.BOLD, 49)));
                additional.addAll(processAdditionalBattlefields(pc, color, battlefields));
                BufferedImage additionalPics = ImageJoiner.joinImagesBelow(additional);
                imageCombinations.add(additionalPics);
            }
        }
        imageCombinations.add(footer);
        BufferedImage combinedImage = ImageJoiner.joinImagesBelow(imageCombinations);
        return combinedImage;

    }

    private static LinkedList<BufferedImage> getCarRows(LinkedList<Car> cars, PicCoords pc, Color color) {
        int columns = 5;
        LinkedList<BufferedImage> rowOfCars = new LinkedList<>();
        LinkedList<BufferedImage> carRows = new LinkedList<>();
        for (int i = 1;
                i < cars.size()
                + 1; i++) {
            rowOfCars.add(ByteImage.getImageFromBytes(cars.get(i - 1).getCarJpg()));
            if (i % columns == 0 && i > 0) {
                carRows.add(ImageJoiner.joinImagesSide(rowOfCars));
                rowOfCars = new LinkedList<>();
            }

        }
        if (rowOfCars.size() < columns) {
            if (rowOfCars.size() > 0) {
                while (rowOfCars.size() < columns) {
                    BufferedImage bi = WritePicture.createImage(pc, pc, "", false, color, Color.blue, new java.awt.Font("Verdana", java.awt.Font.BOLD, 20));
                    rowOfCars.add(bi);
                }
                carRows.add(ImageJoiner.joinImagesSide(rowOfCars));
            }

        }
        return carRows;
    }

    public static BufferedImage toBufferedImage(PicCoords pc, Color color, Image img) {
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

    private static LinkedList<BufferedImage> processAdditionalBattlefields(PicCoords pc, Color color, Battlefield[] battlefields) {
        ArrayList<Car> existingCars = battlefields[0].getAllCars();

        LinkedList<Car> newCars = new LinkedList<>();
        for (int i = 1; i < battlefields.length; i++) {
            Battlefield battlefield = battlefields[1];
            if (battlefield == null || battlefield.getAllCars() == null || battlefield.getAllCars().size() == 0) {
                continue;
            }
            ArrayList<Car> testCars = battlefield.getAllCars();
            for (Car test : testCars) {
                boolean add = true;
                for (Car existing : existingCars) {
                    if (existing.getUniqueHash().equals(test.getUniqueHash())) {
                        add = false;
                        break;
                    }
                }
                 for (Car existing : newCars) {
                    if (existing.getUniqueHash().equals(test.getUniqueHash())) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    newCars.add(test);
                }
            }
        }
        return getCarRows(newCars, pc, color);

    }
}
