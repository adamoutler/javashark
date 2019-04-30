/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

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
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
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

    String targetScore;
    String ourScore;
    String theirScore;

    public CityKingsBattlefieldAnalysis(BufferedImage battlefield) {
        this.battlefield = battlefield;
        picColors = new PicColors(battlefield);
        targetScore = getTargetScore();
        ourScore = getOurScore();
        theirScore = getTheirScore();
        System.out.println("Us: "+ourScore+" them:"+theirScore+" target:"+targetScore);

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
        PicCoords pc = new PicCoords(leftSide.getX(), leftSide.getY(), width, scoreHeight);
        return pc.getW() == 0 ? null : pc;
    }

    PicCoords getCoordinatesOurScore() {
        PicCoords ourGaining = getCoordinatesOurGaining();
        if (ourGaining == null) {
            return OURSCORECOORDS;
        }
        int startXPos = ourGaining.getX() + ourGaining.getW() + arrowHeadWidth;
        int startYPos = ourGaining.getY();
        int width = xCoordOfOurScoreWall - startXPos;
        PicCoords pc = new PicCoords(startXPos, startYPos - 10, width, scoreHeight + 15);
        return pc;
    }

    XYCoords getLeftSideOfTheirGainingScore() {
        XYCoords scanStart = new XYCoords(xCoordOfTheirScoreWall, topOfScore);
        int theirGainingStart = picColors.scanRightXCoordinateForColorAtYCoordinate(RED, scanStart.getX(), topOfScore, xCoordOfTheirScoreAwayFromWall,10);

        return (theirGainingStart == -1) ? null : new XYCoords(theirGainingStart, scanStart.getY());
    }

    PicCoords getCoordinatesTheirGaining() {
        XYCoords leftSide = getLeftSideOfTheirGainingScore();
        int endOfGainingScore = picColors.scanRightXCoordinateForLackOfColorAtYCoordinate(new int[]{RED}, leftSide.getX(), leftSide.getY(), xCoordOfTheirScoreAwayFromWall, 10);
        int width = endOfGainingScore - leftSide.getX();
        PicCoords pc = new PicCoords(leftSide.getX(), leftSide.getY(), width, scoreHeight);
        return pc.getW() == 0 ? null : pc;
    }

    PicCoords getCoordinatesTheirScore() {
        PicCoords theirGaining = getCoordinatesTheirGaining();
        if (theirGaining == null) {
            return THEIRSCORECOORDS;
        }
        int startXPos = xCoordOfTheirScoreWall;
        int startYPos = theirGaining.getY();
        int width = theirGaining.getX() - arrowHeadWidth - startXPos;
        PicCoords pc = new PicCoords(startXPos, startYPos - 5, width, scoreHeight + 10);
        return pc;
    }

    private String getTargetScore() {
        PicCoords coords = TARGETSCORECOORDS;
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        //writePicture(coords, battlefield);
        //String value = new OCR().discern(toOCR).trim();
        String value=DirectTesseract.runTesseractSingleWord(toOCR);
        return value;
    }

    private String getOurScore() {
        PicCoords coords = getCoordinatesOurScore();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        toOCR = positivePrepForOCR(toOCR, true);
        //PicCoords xy = new PicCoords(0, 0, toOCR.getWidth(), toOCR.getHeight());
        //writePicture(xy, toOCR);
        //String value = new OCR().discern(toOCR).trim();
        String value=DirectTesseract.runTesseractSingleWord(toOCR);
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
        PicCoords coords = getCoordinatesTheirScore();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);
        toOCR = positivePrepForOCR(toOCR, false);
        //PicCoords xy = new PicCoords(0, 0, toOCR.getWidth(), toOCR.getHeight());
        //writePicture(xy, toOCR);
        //String value = new OCR().discern(toOCR).trim();
        String value=DirectTesseract.runTesseractSingleWord(toOCR);
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
        BufferedImage bi=IncreaseSize.resize(increasedHeight, increasedHeight.getWidth()*3,increasedHeight.getHeight()*3 );
        return bi;


    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
