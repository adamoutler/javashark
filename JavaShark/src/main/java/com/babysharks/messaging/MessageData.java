/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author adamo
 */
public class MessageData {

    private String weOwn;
    private String theyOwn;
    private String nobodyOwns;
    private String alertBuilding;
    private String text;
    private String[] buildingStates;
    private String emoji;
    private String reason;
    private String[] buildings;

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
     * @return the emoji
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * @param emoji the emoji to set
     * @return representation of this MessageData object
     */
    public MessageData setEmoji(String emoji) {
        this.emoji = emoji;
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
}
