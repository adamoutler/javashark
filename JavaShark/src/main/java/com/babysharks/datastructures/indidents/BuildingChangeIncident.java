/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures.indidents;

/**
 *
 * @author adamo
 */
public class BuildingChangeIncident {

    private final int building;
    private final float before;
    private final float after;
   public BuildingChangeIncident(int building, float oldValue, float newValue){
        this.building=building;
        this.after=oldValue;
        this.before=newValue;
        
    }

    /**
     * @return the building
     */
    public int getBuilding() {
        return building;
    }

    /**
     * @return the before
     */
    public float getBefore() {
        return before;
    }

    /**
     * @return the after
     */
    public float getAfter() {
        return after;
    }
}
