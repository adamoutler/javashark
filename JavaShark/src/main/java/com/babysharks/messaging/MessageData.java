/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author adamo
 */
public class MessageData {

    @SerializedName("args")
    @Expose
    private String args;
    private String method;
    private String weOwn;
    private String theyOwn;
    private String nobodyOwns;
    private String alertBuilding;
    private String text;
    private String[] buildingStates;
    private String reason;
    private String[] buildings;
    private Object resultObject;
    private String username;
    private String url;
    private String gang;
    private String active;
    private String available;
    private String location;
    
            

    /**
     * @return the weOwn
     */
    public String getWeOwn() {
        return weOwn;
    }

    /**
     * @param weOwn the weOwn to set
     * @return representation of this MessageData object
     */
    public MessageData setWeOwn(String weOwn) {
        this.weOwn = weOwn;
        return this;
    }

    /**
     * @return the theyOwn
     */
    public String getTheyOwn() {
        return theyOwn;
    }

    /**
     * @param theyOwn the theyOwn to set
     * @return representation of this MessageData object
     */
    public MessageData setTheyOwn(String theyOwn) {
        this.theyOwn = theyOwn;
        return this;
    }

    /**
     * @return the nobodyOwns
     */
    public String getNobodyOwns() {
        return nobodyOwns;
    }

    /**
     * @param nobodyOwns the nobodyOwns to set
     * @return representation of this MessageData object
     */
    public MessageData setNobodyOwns(String nobodyOwns) {
        this.nobodyOwns = nobodyOwns;
        return this;
    }

    /**
     * @return the alertBuilding
     */
    public String getAlertBuilding() {
        return alertBuilding;
    }

    /**
     * @param alertBuilding the alertBuilding to set
     * @return representation of this MessageData object
     */
    public MessageData setAlertBuilding(String alertBuilding) {
        this.alertBuilding = alertBuilding;
        return this;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     * @return representation of this MessageData object
     */
    public MessageData setText(String text) {
        this.text = text;
        return this;
    }

     /**
     * @return the buildingStates
     */
    public String[] getBuildingStates() {
        return buildingStates;
    }

    /**
     * @param buildingStates the buildingStates to set
     */
    public void setBuildingStates(String[] buildingStates) {
        this.buildingStates = buildingStates;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     * @return representation of this MessageData object
     */
    public MessageData setReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * @return the buildings
     */
    public String[] getBuildings() {
        return buildings;
    }

    /**
     * @param buildings the buildings to set
     * @return representation of this MessageData object
     */
    public MessageData setBuildings(String[] buildings) {
        this.buildings = buildings;
        return this;
    }

    /**
     * @return the resultObject
     */
    public Object getResultObject() {
        return resultObject;
    }

    /**
     * @param resultObject the resultObject to set
     * @return representation of this MessageData object
     */
    public MessageData setResultObject(Object resultObject) {
        this.resultObject = resultObject;
        return this;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     * @return reference to this object
     */
    public MessageData setMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * @return the args
     */
    public String getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     * @return reference to this object
     */
    public MessageData setArgs(String args) {
        this.args = args;
        return this;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     * @return reference to this object
     */
    public MessageData setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     * @return
     */
    public MessageData setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return the gang
     */
    public String getGang() {
        return gang;
    }

    /**
     * @param gang the gang to set
     * @return reference to this object
     */
    public MessageData setGang(String gang) {
        this.gang = gang;
        return this;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     * @return
     */
    public MessageData setActive(String active) {
        this.active = active;
        return this;
    }

    /**
     * @return the available
     */
    public String getAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     * @return reference to this object
     */
    public MessageData setAvailable(String available) {
        this.available = available;
        return this;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     * @return reference to this object
     */
    public MessageData setLocation(String location) {
        this.location = location;
        return this;
    }
}
