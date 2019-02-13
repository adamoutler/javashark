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
@SerializedName("0")
    @Expose
    private String arg0;
    @SerializedName("1")
    @Expose
    private String arg1;
    @SerializedName("2")
    @Expose
    private String arg2;
    @SerializedName("3")
    @Expose
    private String arg3;
    @SerializedName("4")
    @Expose
    private String arg4;
    @SerializedName("5")
    @Expose
    private String arg5;
    @SerializedName("args")
    @Expose
    private String args;
   
    @SerializedName("method")
    @Expose
    private String method;
    private String weOwn;
    private String theyOwn;
    private String nobodyOwns;
    private String alertBuilding;
    private String text;
    private String[] buildingStates;
    private String emoji;
    private String reason;
    private String[] buildings;
    private Object resultObject;

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
     * @return the arg0
     */
    public String getArg0() {
        return arg0;
    }

    /**
     * @param arg0 the arg0 to set
     */
    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * @return the arg1
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * @param arg1 the arg1 to set
     */
    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    /**
     * @return the arg2
     */
    public String getArg2() {
        return arg2;
    }

    /**
     * @param arg2 the arg2 to set
     */
    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    /**
     * @return the arg3
     */
    public String getArg3() {
        return arg3;
    }

    /**
     * @param arg3 the arg3 to set
     */
    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    /**
     * @return the arg4
     */
    public String getArg4() {
        return arg4;
    }

    /**
     * @param arg4 the arg4 to set
     */
    public void setArg4(String arg4) {
        this.arg4 = arg4;
    }

    /**
     * @return the arg5
     */
    public String getArg5() {
        return arg5;
    }

    /**
     * @param arg5 the arg5 to set
     */
    public void setArg5(String arg5) {
        this.arg5 = arg5;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the args
     */
    public String getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(String args) {
        this.args = args;
    }
}
