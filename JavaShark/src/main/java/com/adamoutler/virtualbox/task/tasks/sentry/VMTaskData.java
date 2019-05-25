/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;

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
    private boolean friendlyScan=true;

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

}
