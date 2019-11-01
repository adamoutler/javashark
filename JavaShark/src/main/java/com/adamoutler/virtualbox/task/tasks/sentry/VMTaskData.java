/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class VMTaskData {

    int nullCount = 0;
    public OwnedBy[] previous;
    public OwnedBy[] current;
    long machineBootUpTime = 0;
    long gameBootUpTime = 0;
    private long lastStartCats = 0;
    private long idleTime = System.currentTimeMillis();
    public int startCatsRetries = 0;
    private int[] buildingsToScan = new int[]{1, 2, 3, 4, 5, 6};
    private boolean friendlyScan = true;
    BufferedImage lastValidScreenshot;
    public int myCarsStage;
    public LinkedList<BufferedImage> carsScreenshots;
    public int retries;
    private String telegramid;
    Float building1PercentOurCars;
    Float building2PercentOurCars;
    Float building3PercentOurCars;
    Float building4PercentOurCars;
    Float building5PercentOurCars;
    Float building6PercentOurCars;
    Float building1PreviousPercentOurCars;
    Float building2PreviousPercentOurCars;
    Float building3PreviousPercentOurCars;
    Float building4PreviousPercentOurCars;
    Float building5PreviousPercentOurCars;
    Float building6PreviousPercentOurCars;

    public void clearBuildingData() {
        building1PercentOurCars = null;
        building2PercentOurCars = null;
        building3PercentOurCars = null;
        building4PercentOurCars = null;
        building5PercentOurCars = null;
        building6PercentOurCars = null;
        building1PreviousPercentOurCars = null;
        building2PreviousPercentOurCars = null;
        building3PreviousPercentOurCars = null;
        building4PreviousPercentOurCars = null;
        building5PreviousPercentOurCars = null;
        building6PreviousPercentOurCars = null;
        previous=null;
        current=null;
    }

    /**
     * @return the lastStartCats
     */
    public long getLastStartCats() {
        return lastStartCats;
    }

    /**
     * @param lastStartCats the lastStartCats to set
     */
    public void setLastStartCats(long lastStartCats) {
        this.lastStartCats = lastStartCats;
    }

    public void setIdleNow() {
        this.idleTime = System.currentTimeMillis();
    }

    /**
     * @return the idleTime
     */
    public long getIdleTime() {
        return idleTime;
    }

    /**
     * @return the buildingsToScan
     */
    public int[] getBuildingsToScan() {
        return buildingsToScan;
    }

    /**
     * @param buildingsToScan the buildingsToScan to set
     */
    public void setBuildingsToScan(int[] buildingsToScan) {
        this.buildingsToScan = buildingsToScan;
    }

    public void setFriendlyScan(boolean friendly) {
        this.friendlyScan = true;
    }

    /**
     * @return the friendlyScan
     */
    public boolean isFriendlyScan() {
        return friendlyScan;
    }

    /**
     * @return the nullCount
     */
    public int getNullCount() {
        return nullCount;
    }

    /**
     * @param nullCount the nullCount to set
     */
    public void setNullCount(int nullCount) {
        this.nullCount = nullCount;
    }

    /**
     * @return the telegramid
     */
    public String getTelegramid() {
        return telegramid;
    }

    /**
     * @param telegramid the telegramid to set
     */
    public void setTelegramid(String telegramid) {
        this.telegramid = telegramid;
    }

}
