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

    public enum IncidentType{
        WEBUFFED("We buffed the building "),
        THEYBUFFED("They buffed the building "),
        LOSTBUFF("We lost a buff in building ");
        String message;
        IncidentType (String s){
            this.message=s;
        }
        public String getMessage(){
            return message;
        }
    }
    
    private IncidentType type;
    String message;
    private final int building;
    private final float before;
    private final float after;
   public BuildingChangeIncident(int building, float oldValue, float newValue, IncidentType type){
        this.building=building;
        this.after=newValue;
        this.before=oldValue;
        this.type=type;
        message=type.getMessage()+ building+1;
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

    /**
     * @return the type
     */
    public IncidentType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(IncidentType type) {
        this.type = type;
    }
}
