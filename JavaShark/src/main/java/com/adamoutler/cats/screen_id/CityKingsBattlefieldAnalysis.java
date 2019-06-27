/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.TesseractBuilder;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CityKingsBattlefieldAnalysis {

    transient final BufferedImage battlefield;
    transient final PicColors picColors;
    transient public final static PicCoords OURSCORECOORDS = new PicCoords(150, 135, 242, 45);
    transient public final static PicCoords THEIRSCORECOORDS = new PicCoords(632, 135, 244, 45);
    transient public final static PicCoords TARGETSCORECOORDS = new PicCoords(457, 148, 141, 42);
    transient public final static PicCoords TIMEREMAININGCOORDS = new PicCoords(447, 81, 101, 32);
//        transient public final static PicCoords TARGETSCORECOORDS = new PicCoords(447, 148, 141, 42);

    transient final int xCoordOfOurScoreAwayFromWall = 128;
    transient final int xCoordOfOurScoreWall = 390;
    transient final int xCoordOfTheirScoreWall = 632;
    transient final int xCoordOfTheirScoreAwayFromWall = 896;
    transient final int topOfScore = 136;
    transient final int scoreHeight = 43;
    transient final int arrowHeadWidth = 8;
    transient final int arrowHeadHeightToCenter = 21;
    transient final int BLUE = 0x4179d9;
    transient final int RED = 0xeb4c47;

    transient PicCoords ourGainingCoords;
    transient PicCoords theirGainingCoords;
    private String targetScore;
    private String ourScore;
    private String theirScore;
    private String ourGaining;
    private String theirGaining;
    private String hours = "0";
    private String minutes = "0";
    private String seconds = "0";
    private String formattedTime;

    public CityKingsBattlefieldAnalysis(BufferedImage battlefield) {
        this.battlefield = battlefield;
        picColors = new PicColors(battlefield);
        targetScore = getTargetScore();
        ourScore = doOCRonOurScore();
        theirScore = getTheirScore();
        ourGaining = getGaining(doGainingScore(ourGainingCoords));
        theirGaining = getGaining(doGainingScore(theirGainingCoords));
        try {
            formattedTime = getTime(battlefield).replaceAll(" ","");
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getTime(BufferedImage battlefield1) throws IOException, InterruptedException {

        TesseractBuilder tb = new TesseractBuilder(battlefield1)
                .setCharset("0123456789hms")
                .crop(CityKingsBattlefieldAnalysis.TIMEREMAININGCOORDS)
                .setMode(TesseractBuilder.Mode.SINGLELLINE).resize(9, 5)
                .invert().greyScale()
                .brightenImage().brightenImage().brightenImage().brightenImage();
        String rawTime = tb.execute();
        tb.writePicture(rawTime);
        String myTime = parseTime(rawTime);
        return myTime;
    }

    XYCoords getLeftSideOfOurGainingScore() {
        XYCoords scanStart = new XYCoords(xCoordOfOurScoreAwayFromWall, topOfScore);
        int ourGainingStart = picColors.scanRightXCoordinateForColorAtYCoordinate(BLUE, scanStart.getX(), scanStart.getY(), xCoordOfOurScoreWall,10);
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
        int endOfGainingScore;
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
        PicCoords tgaining = getCoordinatesTheirGaining();
        if (tgaining == null) {
            return THEIRSCORECOORDS;
        }
        int startXPos = xCoordOfTheirScoreWall;
        int width = tgaining.getX() - arrowHeadWidth - startXPos;
        PicCoords pc = new PicCoords(startXPos, topOfScore - 5, width, scoreHeight + 10);
        return pc;
    }

    private String getTargetScore() {
        System.out.println("trying to get target score");

        String value = "";
        try {
            TesseractBuilder tb = new TesseractBuilder(battlefield)
                    .setCharset("0123456789")
                    .crop(CityKingsBattlefieldAnalysis.TARGETSCORECOORDS)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .resize(5).bicubic().invert().greyScale().brightenImage()
                    .brightenImage().brightenImage().brightenImage()
                    .brightenImage().brightenImage().brightenImage()
                    .brightenImage().bicubic().writePicture("Target Score");
            value = tb.writePicture().execute();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("got target score");
        return value;
    }

    private String doOCRonOurScore() {
        System.out.println("trying to get our score");
        PicCoords coords = getCoordinatesOurScore();
        if (coords == null) {
            return "0";
        }
        String value = "";
        try {
            TesseractBuilder tb = new TesseractBuilder(battlefield)
                    .setCharset("0123456789")
                    .crop(coords)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .resize(2).greyScale();
            value = tb.writePicture().execute();
            tb.writePicture(value);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("got our score");
        return value;
    }

    private String doOcrOnOurGaining() {
        PicCoords coords = getCoordinatesOurGaining();
        if (coords == null) {
            return "0";
        }
        String value = "";
        try {
            TesseractBuilder tb = new TesseractBuilder(battlefield)
                    .setCharset("+0123456789")
                    .crop(coords)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .invert().greyScale()
                    
                    .resize(7,3).brightenImage().brightenImage().brightenImage()
                    .resize(2).bicubic().resize(2).bicubic();
        
            value = tb.execute();
            tb.writePicture(value);
           System.out.println();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
 private String doOcrOnTheirGaining() {
        PicCoords coords = getCoordinatesTheirGaining();
        if (coords == null) {
            return "0";
        }
        String value = "";
        try {
            TesseractBuilder tb = new TesseractBuilder(battlefield)
                    .setCharset("+0123456789")
                    .crop(coords)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .invert().greyScale()
                    
                    .resize(7,3).brightenImage().brightenImage().brightenImage()
                    .brightenImage().brightenImage().brightenImage()
                    .brightenImage().brightenImage().brightenImage()
                    .resize(2).bicubic().resize(2).bicubic();
        
            value = tb.execute();
            tb.writePicture(value);
           System.out.println();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    private String getTheirScore() {
        System.out.println("trying to get their score");
        PicCoords coords = getCoordinatesTheirScore();
        if (coords == null) {
            return "0";
        }
        BufferedImage toOCR = coords.getImageAtCoords(battlefield);

        try {
            TesseractBuilder tb = new TesseractBuilder(battlefield)
                    .setCharset("+0123456789")
                    .crop(coords)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .resize(2).greyScale().brightenImage();
            String value = tb.execute();
            tb.writePicture(value);
            return value;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0";

    }



    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public String toString() {
        return "Us: " + doOCRonOurScore() + " +:" + doOcrOnOurGaining() + " " + " time:" + getFormattedTime() + " them:" + getTheirScore() + " +:" + doOcrOnTheirGaining() + " target:" + getTargetScore() + " ";
    }

    private String doGainingScore(PicCoords coords) {
        if (coords == null || coords.getX() == 0 || coords.getX() == -1) {
            return null;
        }

        String value = "";
        try {
            TesseractBuilder tb = new TesseractBuilder(battlefield)
                    .setCharset("8012345679+")
                    .crop(coords)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .resize(30, 10).invert().greyScale().brightenImage().brightenImage().brightenImage();
            value = tb.execute();
            tb.writePicture(value);

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;

    }

    private String getGaining(String gaining) {
        try {
            if (gaining.startsWith("+")) {
                gaining = gaining.replace("+", "");
            }
        } catch (NullPointerException ex) {
            return "0";
        }
        return gaining;
    }

    private String parseTime(String time) {
        String[] timeSplit = time.split(" ");
        for (String t : timeSplit) {
            if (t.length()<2) continue;
            char c = t.charAt(t.length() - 1);
            switch (c) {
                case 'h':
                    hours = t.replace("h", "");
                    break;
                case 'm':
                    minutes = t.replace("m", "");
                    break;

                case 's':
                    seconds = t.replace("s", "");
                    break;
            }

        }
        return time;
    }

    /**
     * @return the hours
     */
    public String getHours() {
        return hours;
    }

    /**
     * @return the minutes
     */
    public String getMinutes() {
        return minutes;
    }

    /**
     * @return the seconds
     */
    public String getSeconds() {
        return seconds;
    }

    /**
     * @return the formattedTime
     */
    public String getFormattedTime() {
        return formattedTime;
    }

    public String getTheirGainingScore() {
        return this.theirGaining;
    }

    public String getOurGainingScore() {
        return this.ourGaining;
    }

    public String getIVScore() {
        return this.targetScore;
    }

    public String getOurCurrentScore() {
        return this.ourScore;
    }

    public String getTheirCurrentScore() {
        return this.theirScore;
    }

}
