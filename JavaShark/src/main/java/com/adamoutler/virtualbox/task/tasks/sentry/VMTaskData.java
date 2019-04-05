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
    long machineBootUpTime=0;
    long gameBootUpTime=0;
    private long lastStartCats=0;
    private long idleTime;
    public int startCatsRetries=0;

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
    
    public void setIdleNow(){
        this.idleTime=System.currentTimeMillis();
    }

    /**
     * @return the idleTime
     */
    public long getIdleTime() {
        return idleTime;
    }
   
}
