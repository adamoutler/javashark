/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.Convert;
import com.adamoutler.imagerecognition.DirectTesseract;
import com.adamoutler.imagerecognition.ImageJoiner;
import com.adamoutler.imagerecognition.IncreaseSize;
import com.adamoutler.imagerecognition.OCR;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.WritePicture;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class CityKingsBattlefieldAnalysis {

    transient final BufferedImage battlefield;
    transient final PicColors picColors;
    transient public final static PicCoords OURSCORECOORDS = new PicCoords(150, 135, 244, 45);
    transient public final static PicCoords THEIRSCORECOORDS = new PicCoords(632, 135, 244, 45);
    transient public final static PicCoords TARGETSCORECOORDS = new PicCoords(457, 148, 141, 42);
    transient public final static PicCoords TIMEREMAININGCOORDS = new PicCoords(447, 81, 101, 32);
//        transient public final static PicCoords TARGETSCORECOORDS = new PicCoords(447, 148, 141, 42);

    transient final int xCoordOfOurScoreAwayFromWall = 128;
    transient final int xCoordOfOurScoreWall = 390;
    transient final int xCoordOfTheirScoreWall = 632;
    transient final int xCoordOfTheirScoreAwayFromWall = 896;
    transient final int topOfScore = 136;
    transient final int scoreHeight = 45;
    transient final int arrowHeadWidth = 8;
    transient final int arrowHeadHeightToCenter = 21;
    transient final int BLUE = 0x4179d9;
    transient final int RED = 0xeb4c47;

    transient PicCoords ourGainingCoords;
    transient PicCoords theirGainingCoords;
    String targetScore;
    String ourScore;
    String theirScore;
    String ourGaining;
    String theirGaining;
    String hours;
    String minutes;
    String seconds;
    String formattedTime;

    public CityKingsBattlefieldAnalysis(BufferedImage battlefield) {
        this.battlefield = battlefield;
        picColors = new PicColors(battlefield);
        targetScore = getTargetScore();
        ourScore = getOurScore();
        theirScore = getTheirScore();
        theirGaining = getGaining(doSingleWordTesseract(theirGainingCoords));
        ourGaining = getGaining(doSingleWordTesseract(ourGainingCoords));
        formattedTime = parseTime(doMultiWordTesserace(TIMEREMAININGCOORDS));
        System.out.println(this);
    }

    public static void writePicture(PicCoords pc, BufferedImage bi) {

        try {

            FileOutputStream fos = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/citykingsbattlefieldanalysis.png");
            ImageIO.write(pc.getImageAtCoords(bi), "PNG", fos);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writePicture(BufferedImage bi) {

        try {

            FileOutputStream fos = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/citykingsbattlefieldanalysis.png");
            ImageIO.write(bi, "PNG", fos);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    XYCoords getLeftSideOfOurGainingScore() {
        XYCoords scanStart = new XYCoords(xCoordOfOurScoreAwayFromWall, topOfScore);
        int ourGainingStart = picColors.scanRightXCoordinateForColorAtYCoordinate(BLUE, scanStart.getX(), scanStart.getY(), xCoordOfOurScoreWall);
        return new XYCoords(ourGainingStart, scanStart.getY());
    }

    PicCoords getCoordinatesOurGaining() {
        XYCoords leftSide = getLeftSideOfOurGainingScore();
        int endOfGainingScore = picColors.scanRightXCoordinateForLackOfColorAtYCoordinate(new int[]{BLUE}, leftSide.getX(), leftSide.getY(), xCoordOfOurScoreWall, 10);
        int width = endOfGainingScore - leftSide.getX();
        ourGainingCoords = new PicCoords(leftSide.getX(), leftSide.getY(), width, scoreHeight);
        return (ourGainingCoords.getW() < 1 || ourGainingCoords.getW() > 100) ? null : ourGainingCoords;
    }

    PicCoords getCoordinatesOurScore() {
        ourGainingCoords = getCoordinatesOurGaining();
        if (ourGainingCoords == null) {
            return OURSCORECOORDS;
        }
        int startXPos = ourGainingCoords.getX() + ourGainingCoords.getW() + arrowHeadWidth;
        int width = xCoordOfOurScoreWall - startXPos;
        PicCoords pc = new PicCoords(startXPos, topOfScore - 10, width, scoreHeight + 15);
        return pc;
    }

    XYCoords getLeftSideOfTheirGainingScore() {
        XYCoords scanStart = new XYCoords(xCoordOfTheirScoreWall, topOfScore);
        int theirGainingStart = picColors.scanRightXCoordinateForColorAtYCoordinate(RED, scanStart.getX(), topOfScore, xCoordOfTheirScoreAwayFromWall, 10);
        int theirGainingStop = picColors.scanRightXCoordinateForLackOfColorAtYCoordinate(new int[]{RED}, theirGainingStart, topOfScore, xCoordOfTheirScoreAwayFromWall, 10);
        theirGainingCoords = new PicCoords(theirGainingStart, topOfScore, theirGainingStop - theirGainingStart, scoreHeight);
        return (theirGainingStart == -1) ? null : new XYCoords(theirGainingStart, scanStart.getY());
    }

    PicCoords getCoordinatesTheirGaining() {
        XYCoords leftSide = getLeftSideOfTheirGainingScore();
        int endOfGainingScore = 0;
        try {
            endOfGainingScore = picColors.scanRightXCoordinateForLackOfColorAtYCoordinate(new int[]{RED}, leftSide.getX(), leftSide.getY(), xCoordOfTheirScoreAwayFromWall, 10);
        } catch (NullPointerException ex) {
            return null;
        }

        int width = endOfGainingScore - leftSide.getX();
        PicCoords pc = new PicCoords(leftSide.getX(), leftSide.getY(), width, scoreHeight);
        return (pc.getW() < 1 || pc.getW() > 100) ? null : pc;
    }

    PicCoords getCoordinatesTheirScore() {
        PicCoords theirGaining = getCoordinatesTheirGaining();
        if (theirGaining == null) {
            return THEIRSCORECOORDS;
        }
        int startXPos = xCoordOfTheirScoreWall;
        int width = theirGaining.getX() - arrowHeadWidth - startXPos;
        PicCoords pc = new PicCoords(startXPos, topOfScore - 5, width, scoreHeight + 10);
        return pc;
    }

    private String getTargetScore() {
        System.out.println("trying to get target score");
        PicCoords coords = TARGETSCORECOORDS;
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        toOCR = IncreaseSize.quadrupleSize(Convert.invertImage(toOCR));
        writePicture(toOCR);
        //String value = new OCR().discern(toOCR).trim();
        String value = DirectTesseract.runTesseractSingleWord(toOCR);
        System.out.println("got target score");
        return value;
    }

    private String getOurScore() {
        System.out.println("trying to get our score");
        PicCoords coords = getCoordinatesOurScore();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        toOCR = positivePrepForOCR(toOCR, true);
        //PicCoords xy = new PicCoords(0, 0, toOCR.getWidth(), toOCR.getHeight());
        //writePicture(xy, toOCR);
        //String value = new OCR().discern(toOCR).trim();
        String value = DirectTesseract.runTesseractSingleWord(toOCR);
        System.out.println("got our score");
        return value;
    }

    private String getOurGaining() {
        PicCoords coords = getCoordinatesOurGaining();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        toOCR = negativePrepForOCR(toOCR, true);
        writePicture(toOCR);
        String value = new OCR().discern(toOCR).trim();
        return value;
    }

    private String getTheirScore() {
        System.out.println("trying to get their score");
        PicCoords coords = getCoordinatesTheirScore();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        toOCR = positivePrepForOCR(toOCR, false);
        //PicCoords xy = new PicCoords(0, 0, toOCR.getWidth(), toOCR.getHeight());
        //writePicture(xy, toOCR);
        //String value = new OCR().discern(toOCR).trim();
        String value = DirectTesseract.runTesseractSingleWord(toOCR);
        System.out.println("got their score");
        return value;
    }

    private String getTheirGaining() {
        PicCoords coords = getCoordinatesTheirGaining();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);

        String value = new OCR().discern(toOCR).trim();
        return value;
    }

    private BufferedImage positivePrepForOCR(BufferedImage picture, boolean ours) {
        BufferedImage toOCR = copyImage(picture);

// public static BufferedImage createImage(PicCoords size, XYCoords xyLocation, String text, boolean skipBackground, Color backColor, Color foreColor, Font font) {
        BufferedImage blackSide = WritePicture.createImage(new PicCoords(0, 0, 15, toOCR.getHeight()), new XYCoords(0, 0), "", false, Color.BLACK, Color.BLACK, new Font("", 1, 1));
        WritePicture.replaceAllButColor(toOCR, (ours) ? BLUE : RED, 0x000000, 50);
        WritePicture.replaceColor(toOCR, (ours) ? BLUE : RED, 0xFFFFFF, 40);
        writePicture(toOCR);

        LinkedList<BufferedImage> sideIncrease = new LinkedList<>();
        sideIncrease.add(blackSide);
        sideIncrease.add(toOCR);
        sideIncrease.add(blackSide);
        BufferedImage increasedWidth = ImageJoiner.joinImagesSide(sideIncrease);

        BufferedImage blackTop = WritePicture.createImage(new PicCoords(0, 0, increasedWidth.getWidth(), 10), new XYCoords(0, 0), "", false, Color.BLACK, Color.BLACK, new Font("", 1, 1));
        LinkedList<BufferedImage> heightIncrease = new LinkedList<>();
        heightIncrease.add(blackTop);
        heightIncrease.add(increasedWidth);
        heightIncrease.add(blackTop);
        BufferedImage increasedHeight = ImageJoiner.joinImagesBelow(heightIncrease);
        return increasedHeight;
    }

    private BufferedImage negativePrepForOCR(BufferedImage picture, boolean ours) {
        BufferedImage toOCR = copyImage(picture);
        WritePicture.replaceAllButColor(toOCR, (ours) ? BLUE : RED, 0xFFFFFF, 50);
        WritePicture.replaceColor(toOCR, (ours) ? BLUE : RED, 0x000000, 40);

        LinkedList<BufferedImage> sideIncrease = new LinkedList<>();
        BufferedImage blackSide = WritePicture.createImage(new PicCoords(0, 0, 15, toOCR.getHeight()), new XYCoords(0, 0), "", false, Color.BLACK, Color.BLACK, new Font("", 1, 1));

        sideIncrease.add(blackSide);
        sideIncrease.add(toOCR);
        sideIncrease.add(blackSide);
        BufferedImage increasedWidth = ImageJoiner.joinImagesSide(sideIncrease);

        BufferedImage blackTop = WritePicture.createImage(new PicCoords(0, 0, increasedWidth.getWidth(), 10), new XYCoords(0, 0), "", false, Color.BLACK, Color.BLACK, new Font("", 1, 1));
        LinkedList<BufferedImage> heightIncrease = new LinkedList<>();
        heightIncrease.add(blackTop);
        heightIncrease.add(increasedWidth);
        heightIncrease.add(blackTop);
        BufferedImage increasedHeight = ImageJoiner.joinImagesBelow(heightIncrease);
        BufferedImage bi = IncreaseSize.resize(increasedHeight, increasedHeight.getWidth() * 3, increasedHeight.getHeight() * 3);
        return bi;

    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public String toString() {
        return "Us: " + ourScore + " +:" + ourGaining + " " + " time:" + formattedTime + " them:" + theirScore + " +:" + theirGaining + " target:" + targetScore + " ";
    }

    private String doMultiWordTesserace(PicCoords coords) {
        if (coords == null || coords.getX() == 0 || coords.getX() == -1) {
            return null;
        }

        BufferedImage image = coords.getImageAtCoords(battlefield);
        writePicture(image);

        Convert.invertImage(IncreaseSize.quadrupleSize(image));

        writePicture(image);
        String s = DirectTesseract.runTesseractMultiWord(image);
        return s;
    }

    private String doSingleWordTesseract(PicCoords coords) {
        if (coords == null || coords.getX() == 0 || coords.getX() == -1) {
            return null;
        }

        BufferedImage image = coords.getImageAtCoords(battlefield);
        writePicture(image);

        PicColors pc = new PicColors(image);
        image = IncreaseSize.resize(image, 500, 300);
        Convert.invertImage(IncreaseSize.quadrupleSize(image));
        writePicture(image);
        String s = DirectTesseract.runTesseractSingleWord(image);
        return s;

    }

    private String getGaining(String gaining) {
        try {
        if (gaining.startsWith("+")) {
            gaining = gaining.replace("+", "");
        }
        } catch (NullPointerException ex){
            return "0";
        }
        return gaining;
    }

    private String parseTime(String time) {
        String[] timeSplit = time.split(" ");
        for (String t : timeSplit) {
            char c = t.charAt(t.length()-1);
            switch (c) {
                case 'h':
                    hours = t.replace("h", "");
                    break;
                case 'm':
                    minutes = t.replace("m", "");
                    break;

                case 's':
                    seconds =t.replace("s", "");
                    break;
            }

        }
        return time;
    }
}
