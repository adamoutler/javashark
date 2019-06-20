/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.adamoutler.cats.battlefield.building.Building;
import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.babysharks.datastructures.Gang;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class Battlefield {



    public enum buildingData {
        BUILDING1NAME(new PicCoords(178, 345, 110, 25)),
        BUILDING1SIZE(new PicCoords(314, 345, 36, 22)),
        BUILDING2NAME(new PicCoords(122, 495, 108, 25)),
        BUILDING2SIZE(new PicCoords(258, 495, 36, 22)),
        BUILDING3NAME(new PicCoords(366, 584, 109, 25)),
        BUILDING3SIZE(new PicCoords(504, 585, 36, 22)),
        BUILDING4NAME(new PicCoords(641, 595, 110, 25)),
        BUILDING4SIZE(new PicCoords(779, 595, 36, 22)),
        BUILDING5NAME(new PicCoords(848, 500, 110, 25)),
        BUILDING5SIZE(new PicCoords(984, 499, 36, 22)),
        BUILDING6NAME(new PicCoords(832, 328, 110, 25)),
        BUILDING6SIZE(new PicCoords(968, 329, 36, 22));
        private PicCoords coords;

        buildingData(PicCoords coords) {
            this.coords = coords;
        }

        /**
         * @return the coords
         */
        public PicCoords getCoords() {
            return coords;
        }

    }
    private String ocrName="";
    private final long time = System.currentTimeMillis();
    private boolean friendly;
    private LinkedList<Building> buildings;
    private byte[] screenshot;
    private String excelFile = "";
    private String jsonFile = "";
    private boolean reportContainsFlaws = false;
    private Gang gang;
    private CityKingsBattlefieldAnalysis battlefield;

    transient private BufferedImage boardScreenshot;

    Battlefield(boolean friendly) {
        this.friendly = friendly;
    }

    /**
     * @param friendly the friendly to set
     */
    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    /**
     * @return the buildings
     */
    public LinkedList<Building> getBuildings() {
        return buildings;
    }

    /**
     * @param buildings the buildings to set
     */
    public void setBuildings(LinkedList<Building> buildings) {
        this.buildings = buildings;
    }

    /**
     * @return the screenshot
     */
    public byte[] getScreenshot() {
        return screenshot;
    }

    /**
     * @param screenshot the screenshot to set
     */
    public void setScreenshot(byte[] screenshot) {
        this.screenshot = screenshot;
    }

    /**
     * @return the excelFile
     */
    public String getExcelFile() {
        return excelFile;
    }

    /**
     * @param excelFile the excelFile to set
     */
    public void setExcelFile(String excelFile) {
        this.excelFile = excelFile;
    }

    /**
     * @return the jsonFile
     */
    public String getJsonFile() {
        return jsonFile;
    }

    /**
     * @param jsonFile the jsonFile to set
     */
    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    /**
     * @return the reportContainsFlaws
     */
    public boolean isReportContainsFlaws() {
        return reportContainsFlaws;
    }

    /**
     * @param reportContainsFlaws the reportContainsFlaws to set
     */
    public void setReportContainsFlaws(boolean reportContainsFlaws) {
        this.reportContainsFlaws = reportContainsFlaws;
    }

    /**
     * @return the gang
     */
    public Gang getGang() {
        return gang;
    }

    /**
     * @param gang the gang to set
     */
    public void setGang(Gang gang) {
        this.gang = gang;
    }

    /**
     * @return the battlefield
     */
    public CityKingsBattlefieldAnalysis getBattlefield() {
        return battlefield;
    }

    /**
     * @param battlefield the battlefield to set
     */
    public void setBattlefield(CityKingsBattlefieldAnalysis battlefield) {
        this.battlefield = battlefield;
    }

    /**
     * @return the boardScreenshot
     */
    public BufferedImage getBoardScreenshot() {
        return boardScreenshot;
    }

    /**
     * @param boardScreenshot the boardScreenshot to set
     */
    public void setBoardScreenshot(BufferedImage boardScreenshot) {
        this.boardScreenshot = boardScreenshot;
    }

    public void clearScreenshots() {
        for (Building building : buildings) {
            building.clearBuildingPics();
        }
    }

    /**
     * set the ocr name of the opponent
     * @param opponentName
     * @return 
     */
    public Battlefield setOcrName(String opponentName) {
        ocrName=opponentName;
        return this;
    }
    
    /**
     * 
     * @return the OCR recognized name.
     */
    public String getOcrName(){
        return ocrName;
    }


}
